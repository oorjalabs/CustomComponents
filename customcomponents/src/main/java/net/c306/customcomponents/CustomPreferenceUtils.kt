package net.c306.customcomponents

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.preference.Preference
import net.c306.customcomponents.listPreference.UpgradedListPreference
import net.c306.customcomponents.listPreference.UpgradedListPreferenceDialogFragment
import net.c306.customcomponents.searchableMultiSelectPreference.SearchableMultiSelectListPreference
import net.c306.customcomponents.searchableMultiSelectPreference.SearchableMultiSelectListPreferenceDialogFragment
import net.c306.customcomponents.timePreference.TimePreference
import net.c306.customcomponents.timePreference.TimePreferenceDialogFragment

object CustomPreferenceUtils {
    
    /**
     * Displays dialog for selected preference if it is a `CustomComponents` preference.
     * @return true if it was a custom preference and been displayed
     */
    fun displayPreferenceDialog(
        preference: Preference?,
        fragmentManager: FragmentManager?,
        targetFragment: Fragment
    ): Boolean {
        /**
         * If this is our custom preference, inflate and show relevant dialog
         */
        when (preference) {
            is TimePreference                      -> {
                TimePreferenceDialogFragment.newInstance(preference.key).run {
                    fragmentManager?.let {
                        setTargetFragment(targetFragment, 0)
                        show(it, "android.support.v7.preference.PreferenceFragment.DIALOG")
                    }
                }
            }
            
            is UpgradedListPreference              -> {
                UpgradedListPreferenceDialogFragment.newInstance(preference.key).run {
                    fragmentManager?.let {
                        setTargetFragment(targetFragment, 0)
                        show(it, "android.support.v7.preference.PreferenceFragment.DIALOG")
                    }
                }
            }
            
            is SearchableMultiSelectListPreference -> {
                SearchableMultiSelectListPreferenceDialogFragment.newInstance(preference.key).run {
                    fragmentManager?.let {
                        setTargetFragment(targetFragment, 0)
                        show(it, "android.support.v7.preference.PreferenceFragment.DIALOG")
                    }
                }
            }
            
            else                                   -> return false
            
        }
        
        return true
    }
}