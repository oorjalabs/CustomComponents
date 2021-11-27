package net.c306.customcomponents.preference

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

abstract class CustomPreferenceFragment: PreferenceFragmentCompat() {
    
    override fun onDisplayPreferenceDialog(preference: Preference?) {
        val fm = parentFragment?.childFragmentManager
        
        if(displayPreferenceDialog(preference, fm, this))
            return
        
        super.onDisplayPreferenceDialog(preference)
    }
    
    companion object {
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
                
                is SearchableListPreference -> {
                    SearchableListPreferenceDialogFragment.newInstance(preference.key).run {
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
    
}