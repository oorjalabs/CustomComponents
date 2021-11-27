package net.c306.customComponentsSample.settings

import android.os.Bundle
import android.view.View
import net.c306.customComponentsSample.R
import net.c306.customcomponents.preference.CustomPreferenceFragment
import net.c306.customcomponents.preference.SearchableListPreference

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
        
        // Searchable single select list preference, with attributes set from xml
        findPreference<SearchableListPreference>("searchable_list_preference_single_search")?.run {
            val disableEntryIndex = dummyList.size - 2
            val entryList: List<SearchableListPreference.Entry> =
                dummyList.mapIndexed { index, s ->
                    SearchableListPreference.Entry(
                        entry = s,
                        value = s,
                        enabled = index != disableEntryIndex,
                        dividerBelow = index == 2
                    )
                }
            
            entries = entryList.toTypedArray()
        }
        
        // Disabled non-searchable multi select list preference, with attributes set from code
        findPreference<SearchableListPreference>("searchable_list_preference_single_search_disabled")?.run {
            val disableEntryIndex = dummyList.size - 2
            val entryList: List<SearchableListPreference.Entry> =
                dummyList.mapIndexed { index, s ->
                    SearchableListPreference.Entry(
                        entry = s,
                        value = s,
                        enabled = index != disableEntryIndex,
                        dividerBelow = index == 2
                    )
                }
            
            entries = entryList.toTypedArray()
            message = "Test message to show at bottom of preference dialog"
            disabledSummary = "Preference is disabled."
            emptyViewText = "No entries. Tap here to create a new one!"
            noneSelectedSummary = "No entries selected"
            enableMultiSelect = true
            showSearch = false
            isEnabled = false
        }
        
        // Test list preference
        findPreference<SearchableListPreference>("test_list_preference")?.run {
            val disableEntryIndex = dummyList.size - 2
            val entryList: List<SearchableListPreference.Entry> =
                dummyList.mapIndexed { index, s ->
                    SearchableListPreference.Entry(
                        entry = s,
                        value = s,
                        enabled = index != disableEntryIndex,
                        dividerBelow = index == 2
                    )
                }
            
            entries = entryList.toTypedArray()
            message = "Test message to show at bottom of preference dialog"
        }
        
    }
    
}
