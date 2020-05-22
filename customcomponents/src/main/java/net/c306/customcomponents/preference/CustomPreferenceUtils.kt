package net.c306.customcomponents.preference

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.preference.Preference

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