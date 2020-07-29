package net.c306.customComponentsSample.settings

import android.os.Bundle
import android.view.View
import net.c306.customComponentsSample.R
import net.c306.customcomponents.preference.CustomPreferenceFragment
import net.c306.customcomponents.preference.UpgradedListPreference
import net.c306.customcomponents.preference.SearchableMultiSelectListPreference

/**
 * Test settings fragment
 */
class SettingsFragment : CustomPreferenceFragment() {
    
    private val dummyList by lazy {
        listOf(
            "Hermione",
            "Ginny",
            "Elsa",
            "Granny",
            "Professor McGonagall",
            "Severus Snape",
            "Jacinda Ardern",
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
        
        findPreference<UpgradedListPreference>("test_list_preference_disabled")?.run {
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
            disabledSummary = "This summary shows only when preference is disabled"
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
            disabledSummary = "This summary only shows when preference is disabled"
        }
        
    }
    
}
