package net.c306.customcomponents

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

abstract class CustomPreferenceFragment: PreferenceFragmentCompat() {
    
    override fun onDisplayPreferenceDialog(preference: Preference?) {
        val fm = parentFragment?.childFragmentManager
        
        if(CustomPreferenceUtils.displayPreferenceDialog(preference, fm, this))
            return
        
        super.onDisplayPreferenceDialog(preference)
    }
    
}