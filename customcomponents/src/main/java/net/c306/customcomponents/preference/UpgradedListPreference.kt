package net.c306.customcomponents.preference


import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import androidx.annotation.Keep
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.ListPreference
import kotlinx.android.parcel.Parcelize

/**
 * List preference with space for message, and option to display some items after a divider.
 * Useful for showing default and custom list items.
 * Uses @see [UpgradedListPreferenceDialogFragment] to display preference.
 */
class UpgradedListPreference : ListPreference {
    
    constructor(context : Context) : this(context, null)
    
    @SuppressLint("RestrictedApi")
    constructor(context : Context, attrs : AttributeSet?) : this(context, attrs, TypedArrayUtils.getAttr(context, androidx.preference.R.attr.dialogPreferenceStyle, android.R.attr.dialogPreferenceStyle))
    
    constructor(context: Context, attrs:AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    
    constructor(context: Context, attrs:AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
    
    @Keep
    @Parcelize
    data class Entry(
        val entry: CharSequence,
        val value: CharSequence,
        val enabled: Boolean = true,
        val summary: CharSequence? = null,
        val dividerBelow: Boolean = false
    ) : Parcelable
    
    var entries: Array<Entry>? = null
    private var mValue: String? = null
    private var mSummary: String? = null
    private var mValueSet: Boolean = false
    var message: String? = null
    
    init {
        summaryProvider = SimpleSummaryProvider.getInstance()
    }
    
    
    override fun setSummary(summary: CharSequence?) {
        super.setSummary(summary)
        if (summary == null && mSummary != null) {
            mSummary = null
        }
        else if (summary != null && summary != mSummary) {
            mSummary = summary.toString()
        }
    }
    
    override fun getSummary(): CharSequence {
        if (summaryProvider != null) {
            return summaryProvider!!.provideSummary(this)
        }
        val entry = entry
        val summary = super.getSummary()
        if (mSummary == null) {
            return summary
        }
        val formattedString = String.format(mSummary!!, entry ?: "")
        if (TextUtils.equals(formattedString, summary)) {
            return summary
        }
        return formattedString
    }
    
    /**
     * Sets the value of the key. This should be one of the entries in [.getEntryValues].
     *
     * @param value The value to set for the key
     */
    override fun setValue(value: String?) {
        // Always persist/notify the first time.
        val changed = !TextUtils.equals(mValue, value)
        if (changed || !mValueSet) {
            mValue = value
            mValueSet = true
            persistString(value)
            if (changed) {
                notifyChanged()
            }
        }
    }
    
    /**
     * Returns the value of the key. This should be one of the entries in [.getEntryValues].
     *
     * @return The value of the key
     */
    override fun getValue(): String? {
        return mValue
    }
    
    /**
     * Returns the entry corresponding to the current value.
     *
     * @return The entry corresponding to the current value, or `null`
     */
    override fun getEntry(): CharSequence? {
        val index = getValueIndex()
        return if (index >= 0 && entries != null) {
            entries!![index].summary ?:
            entries!![index].entry
        }
        else null
    }
    
    /**
     * Returns the index of the given value (in the entry values array).
     *
     * @param value The value whose index should be returned
     * @return The index of the value, or -1 if not found
     */
    override fun findIndexOfValue(value: String?): Int {
        if (value != null && entries != null) {
            return entries!!.indexOfLast {
                it.value == value
            }
        }
        return -1
    }
    
    /**
     * Sets the value to the given index from the entry values.
     *
     * @param index The index of the value to set
     */
    override fun setValueIndex(index: Int) {
        entries?.let {
            value = it[index].value.toString()
        }
    }
    
    private fun getValueIndex(): Int {
        return findIndexOfValue(mValue)
    }
    
    override fun onGetDefaultValue(a: TypedArray, index: Int): Any? {
        return a.getString(index)
    }
    
    override fun onSetInitialValue(defaultValue: Any?) {
        value = getPersistedString(defaultValue as String?)
    }
    
    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        if (isPersistent) {
            // No need to save instance state since it's persistent
            return superState
        }
        
        val myState = SavedState(superState)
        myState.mValue = value
        return myState
    }
    
    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state == null || state.javaClass != SavedState::class.java) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state)
            return
        }
        
        val myState = state as SavedState?
        super.onRestoreInstanceState(myState!!.superState)
        value = myState.mValue
    }
    
    private class SavedState : BaseSavedState {
        
        internal var mValue: String? = null
        
        internal constructor(source: Parcel) : super(source) {
            mValue = source.readString()
        }
        
        internal constructor(superState: Parcelable) : super(superState)
        
        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeString(mValue)
        }
        
        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }
                
                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
    
}
