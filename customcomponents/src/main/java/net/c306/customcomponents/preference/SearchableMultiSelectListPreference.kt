package net.c306.customcomponents.preference

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.MultiSelectListPreference
import kotlinx.android.parcel.Parcelize

/**
 * Searchable MultiSelectListPreference with a search field added to search through large list of
 * entries.
 * Uses @see [SearchableMultiSelectListPreferenceDialogFragment] to display preference.
 */
class SearchableMultiSelectListPreference: MultiSelectListPreference {
    constructor(context : Context) : this(context, null)

    @SuppressLint("RestrictedApi")
    constructor(context : Context, attrs : AttributeSet?) : this(context, attrs, TypedArrayUtils.getAttr(context, androidx.preference.R.attr.dialogPreferenceStyle, android.R.attr.dialogPreferenceStyle))

    constructor(context: Context, attrs:AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs:AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    var entries: Array<Entry>? = null
    var message: String? = null
    var emptyViewText: String? = null
    var noneSelectedSummary: String? = null
    
    @Parcelize
    data class Entry(
        /** String that is displayed in list in dialog */
        val listDisplayString: String,
        /** Value that's saved to storage, equivalent of entryValues in ListPreference. If not present, listDisplayName is used. */
        val saveString: String = listDisplayString,
        /** Optional display name to show in summary. If not present, listDisplayName is used. */
        val summaryDisplayName: String = listDisplayString,
        /** Optional search string to use for searching. If not present, listDisplayName is used.  */
        val listSearchString: String = listDisplayString
    ): Parcelable

    override fun getSummary(): CharSequence {
        return if (values.isEmpty()) {
            noneSelectedSummary ?: ""
        } else {
            entries
                ?.filter { it.saveString in values }
                ?.joinToString(", ") { it.summaryDisplayName }
                ?: values.joinToString(", ")
        }
    }

}