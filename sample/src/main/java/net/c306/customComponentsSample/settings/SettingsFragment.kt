package net.c306.customComponentsSample.settings

import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import net.c306.customComponentsSample.R
import net.c306.customcomponents.listPreference.UpgradedListPreference
import net.c306.customcomponents.listPreference.UpgradedListPreferenceDialogFragment
import net.c306.customcomponents.searchablemultiselectpreference.SearchableMultiSelectListPreference
import net.c306.customcomponents.searchablemultiselectpreference.SearchableMultiSelectListPreferenceDialogFragment

/**
 * Test settings fragment
 */
class SettingsFragment : PreferenceFragmentCompat() {
    
    private val dummyList by lazy {
        listOf(
            "Hermione",
            "Ginny",
            "Elsa",
            "Granny",
            "Professor McGonagall",
            "Severus Snape",
            "Ove",
            "Luna Lovegood",
            "Neville Longbottom",
            "Fred Weasley",
            "George Weasley"
        )
    }
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Inflate preferences from resource file
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
    
    override fun onDisplayPreferenceDialog(preference: Preference?) {
        val fm = parentFragment?.childFragmentManager
        
        when (preference) {
            /**
             * If this is our custom Time Preference, inflate and show Time picker dialog
             */
//            is TimePreference                      -> {
//                TimePreferenceDialogFragmentCompat.newInstance(preference.key).run {
//                    fm?.let {
//                        setTargetFragment(this@SettingsFragment, 0)
//                        show(it, "android.support.v7.preference.PreferenceFragment.DIALOG")
//                    }
//                }
//            }
            
            is UpgradedListPreference              -> {
                UpgradedListPreferenceDialogFragment.newInstance(preference.key).run {
                    fm?.let {
                        setTargetFragment(this@SettingsFragment, 0)
                        show(it, "android.support.v7.preference.PreferenceFragment.DIALOG")
                    }
                }
            }
            
            is SearchableMultiSelectListPreference -> {
                SearchableMultiSelectListPreferenceDialogFragment.newInstance(preference.key).run {
                    fm?.let {
                        setTargetFragment(this@SettingsFragment, 0)
                        show(it, "android.support.v7.preference.PreferenceFragment.DIALOG")
                    }
                }
            }
            
            else                                   -> {
                super.onDisplayPreferenceDialog(preference)
            }
            
        }
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Test list preference
        findPreference<UpgradedListPreference>("test_list_preference")?.run {
            val disableEntryIndex = dummyList.size - 2
            val entryList: List<UpgradedListPreference.Entry> =
                dummyList.mapIndexed { index, s ->
                    UpgradedListPreference.Entry(
                        entry = s,
                        value = s,
                        enabled = index != disableEntryIndex,
                        dividerBelow = index == 2
                    )
                }
            
            entries = entryList.toTypedArray()
            message = "Test message to show at bottom of preference dialog"
        }
        
        // Test multi select list preference
        findPreference<SearchableMultiSelectListPreference>("test_searchable_list_preference")?.run {
            
            val entryList = dummyList.map {
                    SearchableMultiSelectListPreference.Entry(
                        listDisplayString = it,
                        saveString = it,
                        listSearchString = it
                    )
                }
            
            
            // Set as entries
            entries = entryList.toTypedArray()
            
            // Set as EntryValues
            entryValues = entryList.map { it.saveString }.toTypedArray()
            noneSelectedSummary = "Nothing selected."
            message = "Test message to show at bottom of preference dialog"
            
        }
        
    }
    
}
