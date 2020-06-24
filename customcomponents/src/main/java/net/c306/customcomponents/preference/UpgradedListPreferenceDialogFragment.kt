package net.c306.customcomponents.preference


import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceDialogFragmentCompat
import kotlinx.android.synthetic.main.item_upgraded_list_preference_customcomponents.view.*
import net.c306.customcomponents.R

internal class UpgradedListPreferenceDialogFragment : PreferenceDialogFragmentCompat() {
    
    companion object {
        fun newInstance(key: String): UpgradedListPreferenceDialogFragment =
            UpgradedListPreferenceDialogFragment().apply {
                arguments = Bundle(1).apply {
                    putString(ARG_KEY, key)
                }
            }
        
        private const val SAVE_STATE_INDEX = "CustomListPreferenceDialogFragment.index"
        private const val SAVE_STATE_ENTRIES = "CustomListPreferenceDialogFragment.entries"
    }
    
    /* synthetic access */
    private var mClickedDialogEntryIndex: Int = 0
    private var mEntries: Array<UpgradedListPreference.Entry>? = null
    
    private val listAdapter: EntryAdapter by lazy {
        context?.let {
            EntryAdapter(
                it,
                mEntries ?: emptyArray(),
                mClickedDialogEntryIndex
            )
        } ?: throw Exception("No Context found!")
    }
    
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (savedInstanceState == null) {
            
            check(preference.entries != null) { "CustomListPreference requires an entries array." }
            
            mClickedDialogEntryIndex = preference.findIndexOfValue(preference.value)
            
            mEntries = preference.entries
        }
        else {
            
            mClickedDialogEntryIndex = savedInstanceState.getInt(SAVE_STATE_INDEX, 0)
            
            val savedEntries = savedInstanceState.getParcelableArray(SAVE_STATE_ENTRIES)
            if (savedEntries != null && savedEntries.all { it is UpgradedListPreference.Entry }) {
                @Suppress("UNCHECKED_CAST")
                mEntries =  savedEntries as Array<UpgradedListPreference.Entry>
            }
        }
    }
    
    
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SAVE_STATE_INDEX, mClickedDialogEntryIndex)
        outState.putParcelableArray(SAVE_STATE_ENTRIES, mEntries)
        super.onSaveInstanceState(outState)
    }
    
    
    @SuppressLint("InflateParams")
    override fun onPrepareDialogBuilder(builder: AlertDialog.Builder?) {
        super.onPrepareDialogBuilder(builder)
        
        val contentView = activity?.layoutInflater?.inflate(R.layout.dialog_upgraded_list_preference_customcomponents, null)?.apply {
            
            // Populate the list and add click listener
            findViewById<ListView>(R.id.list)?.apply {
                adapter = listAdapter
                onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                    
                    if (mEntries?.get(position)?.enabled == false) {
                        return@OnItemClickListener
                    }
                    
                    mClickedDialogEntryIndex = position
                    
                    // Clicking on an item simulates the positive button click, and dismisses
                    // the dialog.
                    this@UpgradedListPreferenceDialogFragment.onClick(dialog,
                                                                      DialogInterface.BUTTON_POSITIVE)
                    dialog?.dismiss()
                }
            }
            
            // If dialog message is set, show it below the list else hide it
            findViewById<TextView>(R.id.message)?.let {
                if (preference.message != null) {
                    it.text = preference.message
                    it.visibility = View.VISIBLE
                }
                else {
                    it.visibility = View.GONE
                }
            }
            
        }
        
        builder?.apply {
            setTitle(preference.title)
            
            setView(contentView)
            
            // The typical interaction for list-based dialogs is to have click-on-an-item dismiss the
            // dialog instead of the user having to press 'Ok'.
            setPositiveButton(null, null)
        }
    }
    
    override fun getPreference(): UpgradedListPreference {
        return super.getPreference() as UpgradedListPreference
    }
    
    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult && mClickedDialogEntryIndex >= 0) {
            mEntries?.get(mClickedDialogEntryIndex)?.let {
                if (preference.callChangeListener(it)) {
                    preference.value = it.value.toString()
                }
            }
        }
    }
    
    
    internal class EntryAdapter(context: Context, list: Array<UpgradedListPreference.Entry>, private val selectedEntry: Int)
        : ArrayAdapter<UpgradedListPreference.Entry>(context, R.layout.item_upgraded_list_preference_customcomponents, list) {
        
        private val mInflater: LayoutInflater = LayoutInflater.from(context)
        
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return (convertView ?: mInflater.inflate(R.layout.item_upgraded_list_preference_customcomponents, null)).apply {
                
                val item = getItem(position) ?: return@apply
                
                with(text) {
                    // Set enabled/disabled
                    isEnabled = item.enabled
                    
                    isActivated = position == selectedEntry
                    
                    background = context.getDrawable(
                        if (isActivated)
                            R.color.bg_list_item_activated
                        else
                            android.R.color.transparent)
                    
                    // Set priority text
                    text = item.entry
                }
                
                // Set divider colour if `dividerBelow` is true, and not last item
                divider.setBackgroundColor(if (item.dividerBelow && position < count - 1) context.getColor(R.color.upgraded_list_divider) else context.getColor(android.R.color.transparent))
                
            }
        }
        
        
        
    }
    
}
