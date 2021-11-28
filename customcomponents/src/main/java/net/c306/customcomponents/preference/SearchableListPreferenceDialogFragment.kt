package net.c306.customcomponents.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceDialogFragmentCompat
import net.c306.customcomponents.R
import net.c306.customcomponents.databinding.DialogSearchableListPreferenceCustomcomponentsBinding
import net.c306.customcomponents.databinding.ItemSearchableListPrefCustomcomponentsBinding
import java.util.*
import kotlin.collections.ArrayList

internal class SearchableListPreferenceDialogFragment : PreferenceDialogFragmentCompat() {
    
    companion object {
        @Suppress("unused")
        fun newInstance(key: String): SearchableListPreferenceDialogFragment =
            SearchableListPreferenceDialogFragment().apply {
                arguments = Bundle(1).apply {
                    putString(ARG_KEY, key)
                }
            }
        
        private const val SAVE_STATE_SELECTED = "SearchableListPreference.selected"
        private const val SAVE_ENTRIES = "SearchableListPreference.entries"
        
        private val SELECTED_ITEM_BG_RES = R.color.bg_searchable_list_item_activated
        private val UNSELECTED_ITEM_BG_RES = R.drawable.sl_bg_item_searchable_pref_customcomponents
    }
    
    private var mEntries: Array<SearchableListPreference.Entry>? = null
    
    private val mEntriesList by lazy {
        mutableListOf<SearchableListPreference.Entry>().apply {
            mEntries?.let { addAll(it) }
        }
    }
    
    private val mSelectedEntries = mutableSetOf<String>()
    
    private val mListAdapter by lazy {
        SearchableListAdapter(
            requireContext(),
            mEntriesList
        )
    }
    
    private val mSearchTextWatcher by lazy { EditTextWatcher() }
    
    private var mEmptyView: TextView? = null
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (savedInstanceState == null) {
            check(preference.entries != null) { "SearchableListPreference requires an entries array." }
            mEntries = preference.entries
            mSelectedEntries.clear()
            mSelectedEntries.addAll(preference.values)
        } else {
            savedInstanceState.getStringArray(SAVE_STATE_SELECTED)?.let {
                mSelectedEntries.clear()
                mSelectedEntries.addAll(it)
            }
            savedInstanceState.getParcelableArray(SAVE_ENTRIES)?.let { savedEntries ->
                if (savedEntries.all { it is SearchableListPreference.Entry }) {
                    @Suppress("UNCHECKED_CAST")
                    mEntries = savedEntries as Array<SearchableListPreference.Entry>
                }
            }
        }
        
    }
    
    
    @SuppressLint("InflateParams")
    override fun onPrepareDialogBuilder(builder: AlertDialog.Builder?) {
        super.onPrepareDialogBuilder(builder)
        
        val binding = DialogSearchableListPreferenceCustomcomponentsBinding.inflate(layoutInflater)
        // Populate the list and add click listener
        with(binding.list) {
            choiceMode =
                if (preference.enableMultiSelect) ListView.CHOICE_MODE_MULTIPLE
                else ListView.CHOICE_MODE_SINGLE
            adapter = mListAdapter
            
            setOnItemClickListener { _, view, position, _ ->
                
                if (mEntries?.get(position)?.enabled == false) {
                    return@setOnItemClickListener
                }
                //item_searchable_list_pref_customcomponents
                val itemBinding = ItemSearchableListPrefCustomcomponentsBinding.bind(view)
                
                val selectedEntry = view.tag as SearchableListPreference.Entry
                
                // We are toggling entries, so using negation
                val isNotInSelected = selectedEntry.value !in mSelectedEntries
                
                @DrawableRes val drawableEnd: Int
                @DrawableRes val backgroundDrawable: Int
                
                if (!preference.enableMultiSelect) {
                    // If single select mode, clear all previous entries on new entry selection
                    mSelectedEntries.clear()
                }
                
                if (isNotInSelected) {
                    drawableEnd = R.drawable.ic_list_preference_item_checked_customcomponents
                    backgroundDrawable = SELECTED_ITEM_BG_RES
                    mSelectedEntries.add(selectedEntry.value)
                } else {
                    drawableEnd = 0
                    backgroundDrawable = UNSELECTED_ITEM_BG_RES
                    mSelectedEntries.remove(selectedEntry.value)
                }
                
                with(itemBinding.text) {
                    background = ContextCompat.getDrawable(context, backgroundDrawable)
                    
                    setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        drawableEnd,
                        0
                    )
                }
                
                if (!preference.enableMultiSelect) {
                    // Clicking on an item simulates the positive button click, and dismisses
                    // the dialog.
                    this@SearchableListPreferenceDialogFragment.onClick(
                        dialog,
                        DialogInterface.BUTTON_POSITIVE
                    )
                    dialog?.dismiss()
                }
                
            }
        }
        
        // If dialog message is set, show it below the list else hide it
        with(binding.message) {
            if (preference.message != null) {
                text = preference.message
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }
        
        // Filter on text change
        with(binding.searchEntries) {
            addTextChangedListener(mSearchTextWatcher)
        }
        
        // Hide filter search if preference attribute unset
        binding.searchWrapper.visibility = if (preference.showSearch) View.VISIBLE else View.GONE
        
        // Set empty view
        with(binding.emptyView) {
            mEmptyView = this
            text = preference.emptyViewText
        }
        
        builder?.apply {
            setTitle(preference.title)
            setView(binding.root)
            
            if (!preference.enableMultiSelect) {
                // The typical interaction for list-based dialogs is to have click-on-an-item dismiss the
                // dialog instead of the user having to press 'Ok'.
                setPositiveButton(null, null)
            }
        }
        
    }
    
    
    override fun getPreference(): SearchableListPreference {
        return super.getPreference() as SearchableListPreference
    }
    
    
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArray(SAVE_STATE_SELECTED, mSelectedEntries.toTypedArray())
        outState.putParcelableArray(SAVE_ENTRIES, mEntries)
        super.onSaveInstanceState(outState)
    }
    
    
    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            if (preference.callChangeListener(mSelectedEntries)) {
                preference.values = mSelectedEntries
            }
        }
    }
    
    
    inner class SearchableListAdapter(
        context: Context,
        list: MutableList<SearchableListPreference.Entry>
    ) : ArrayAdapter<SearchableListPreference.Entry>(
        context,
        R.layout.item_searchable_list_pref_customcomponents,
        list
    ) {
        
        private var mFilter = ProjectFilter(list)
        private val mInflater: LayoutInflater = LayoutInflater.from(context)
        
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val binding =
                convertView?.let { ItemSearchableListPrefCustomcomponentsBinding.bind(it) }
                    ?: ItemSearchableListPrefCustomcomponentsBinding.inflate(mInflater)
            
            val item = getItem(position) ?: return binding.root
            
            binding.root.isEnabled = item.enabled
            
            with(binding.text) {
                
                isEnabled = item.enabled
                
                text = item.entry
                
                val isInSelected = item.value in mSelectedEntries
                
                val drawableEnd =
                    if (isInSelected) R.drawable.ic_list_preference_item_checked_customcomponents
                    else 0
                setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, drawableEnd, 0)
                
                background = ContextCompat.getDrawable(
                    context,
                    if (isInSelected) SELECTED_ITEM_BG_RES
                    else if (!isEnabled) android.R.color.transparent
                    else UNSELECTED_ITEM_BG_RES
                )
            }
            
            // Set divider colour if `dividerBelow` is true, and not last item
            binding.divider.setBackgroundColor(
                if (item.dividerBelow && position < count - 1) context.getColor(R.color.upgraded_list_divider)
                else context.getColor(android.R.color.transparent)
            )
            
            binding.root.tag = item
            
            return binding.root
        }
        
        
        override fun getFilter(): Filter = mFilter
        
        
        private inner class ProjectFilter(objects: MutableList<SearchableListPreference.Entry>) :
            Filter() {
            
            private val sourceList: ArrayList<SearchableListPreference.Entry> =
                ArrayList(objects)
            
            override fun performFiltering(chars: CharSequence): FilterResults {
                val filterSeq = chars.toString().toLowerCase(Locale.getDefault())
                val result = FilterResults()
                
                if (filterSeq.isBlank()) {
                    // add all objects
                    synchronized(this) {
                        result.values = sourceList
                        result.count = sourceList.size
                    }
                    return result
                }
                
                return result.apply {
                    
                    // The filtering itself
                    val filteredList = sourceList.filter {
                        it.listSearchString.toLowerCase(Locale.getDefault()).contains(filterSeq)
                    }
                    
                    values = filteredList
                    count = filteredList.size
                }
            }
            
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                // NOTE: this function is *always* called from the UI thread.
                clear()
                
                @Suppress("UNCHECKED_CAST")
                addAll(results.values as MutableList<SearchableListPreference.Entry>)
                notifyDataSetChanged()
            }
        }
        
    }
    
    
    private inner class EditTextWatcher : TextWatcher {
        
        internal var previousText = ""
            private set
        
        private val onFilter = Filter.FilterListener {
            mEmptyView?.visibility = if (it > 0) View.GONE else View.VISIBLE
        }
        
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        
        override fun afterTextChanged(s: Editable?) {
            val inputText = s?.trim().toString()
            
            if (inputText == previousText) {
                return
            }
            
            previousText = inputText
            
            mListAdapter.filter.filter(previousText, onFilter)
        }
        
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        
    }
    
}