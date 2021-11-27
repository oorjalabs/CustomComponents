package net.c306.customcomponents.preference

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.MultiSelectListPreference
import kotlinx.parcelize.Parcelize

/**
 * Searchable MultiSelectListPreference with a search field added to search through large list of
 * entries.
 * Uses @see [SearchableListPreferenceDialogFragment] to display preference.
 */
class SearchableListPreference(context: Context, attrs:AttributeSet?, defStyleAttr: Int, defStyleRes: Int): MultiSelectListPreference(context, attrs, defStyleAttr, defStyleRes) {
    
    @Suppress("unused")
    constructor(context : Context) : this(context, null)

    @SuppressLint("RestrictedApi")
    constructor(context : Context, attrs : AttributeSet?) : this(context, attrs, TypedArrayUtils.getAttr(context, androidx.preference.R.attr.dialogPreferenceStyle, android.R.attr.dialogPreferenceStyle))

    constructor(context: Context, attrs:AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    
    companion object {
        private const val NAMESPACE = "http://schemas.android.com/apk/res-auto"
    }
    
    var entries: Array<Entry>? = null
    /** Optional message to display at bottom of list in preference dialog. */
    var message: String? = null
    /** Optional string to display in preference dialog when there are no items */
    var emptyViewText: String? = null
    /** Optional summary to display when preference is enabled but no entry is selected. */
    var noneSelectedSummary: String? = null
    /** Optional summary to display when preference is disabled. */
    var disabledSummary: CharSequence? = null
    /** Whether to show search box in preference dialog. Default `true` */
    var showSearch: Boolean = true
    /** Default `true` */
    var enableMultiSelect: Boolean = true
    
    /**
     * Initialise variables from xml attributes, if available
     */
    init {
        attrs?.run {
            
            showSearch = getAttributeBooleanValue(
                NAMESPACE,
                "showSearch",
                true
            )
        
            enableMultiSelect = getAttributeBooleanValue(
                NAMESPACE,
                "multiSelect",
                true
            )
        
            getAttributeValue(NAMESPACE, "defaultValues")
                ?.also { setDefaultValue(it.split(",").toSet()) }
            
            getAttributeValue(NAMESPACE, "emptyViewText")
                ?.also { emptyViewText = it }
        
            getAttributeValue(NAMESPACE, "disabledSummary")
                ?.also { disabledSummary = it }
        
            getAttributeValue(NAMESPACE, "noneSelectedSummary")
                ?.also { noneSelectedSummary = it }
        
            getAttributeValue(NAMESPACE, "message")
                ?.also { message = it }
        
        }
    }
    
    @Parcelize
    data class Entry(
        /** String that is displayed in list in dialog */
        val entry: String,
        /** Value that's saved to storage, equivalent of entryValues in ListPreference.  Defaults to [entry]. */
        val value: String = entry,
        /** Optional display name to show in summary. Defaults to [entry]. */
        val summary: String = entry,
        /** Optional search string to use for searching. Defaults to [entry].  */
        val listSearchString: String = entry,
        /** Whether this entry is enabled for selection  (Default `true`) */
        val enabled: Boolean = true,
        /** Enable to show divider below this entry in list (Default `false`). Useful for grouping entries. */
        val dividerBelow: Boolean = false
    ): Parcelable
    
    override fun getSummary(): CharSequence {
        return when {
            
            !isEnabled && disabledSummary != null -> disabledSummary!!
            
            values.isEmpty() -> noneSelectedSummary ?: ""
            
            else             -> {
                entries
                    ?.filter { it.value in values }
                    ?.joinToString(", ") { it.summary }
                    ?: values.joinToString(", ")
            }
        }
    }
    
    override fun setDefaultValue(defaultValue: Any?) {
        
        if (defaultValue !is String) {
            super.setDefaultValue(defaultValue)
            return
        }
        
        val previousValue = values
    
        // Set default value
        if (previousValue.isNullOrEmpty()) {
            values = setOf(defaultValue)
        }
        
    }
    
}