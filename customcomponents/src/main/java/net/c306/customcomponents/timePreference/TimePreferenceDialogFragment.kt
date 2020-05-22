package net.c306.customcomponents.timePreference

import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.TimePicker
import androidx.preference.DialogPreference
import androidx.preference.Preference
import androidx.preference.PreferenceDialogFragmentCompat
import net.c306.customcomponents.R
import java.util.*


class TimePreferenceDialogFragment : PreferenceDialogFragmentCompat(), DialogPreference.TargetFragment {
    private var timePicker: TimePicker? = null
    
    companion object {
        fun newInstance(key: String): TimePreferenceDialogFragment {
            
            val dialogFragment = TimePreferenceDialogFragment()
            val bundle = Bundle(1).apply {
                putString("key", key)
            }
            
            dialogFragment.arguments = bundle
            return dialogFragment
        }
    }
    
    
    override fun onBindDialogView(v: View) {
        super.onBindDialogView(v)
        
        timePicker = v.findViewById(R.id.edit_time) as TimePicker
        
        // Exception when there is no TimePicker
        checkNotNull(timePicker) { "Dialog view must contain a TimePicker with id 'edit_time'" }
        
        timePicker?.setIs24HourView(DateFormat.is24HourFormat(activity))
        val pref = preference as TimePreference
        
        timePicker?.hour = pref.calendar.get(Calendar.HOUR_OF_DAY)
        timePicker?.minute = pref.calendar.get(Calendar.MINUTE)
    }
    
    
    override fun onDialogClosed(positiveResult: Boolean) {
        
        if (!positiveResult) {
            return
        }
        
        val pref = preference as TimePreference
        pref.calendar.set(Calendar.HOUR_OF_DAY, timePicker!!.hour)
        pref.calendar.set(Calendar.MINUTE, timePicker!!.minute)
        
        pref.summary = pref.mSummary
        
        val value = TimePreference.calendarToSavedValue(pref.calendar)
        
        if (pref.callChangeListener(value)) {
            pref.persistIntValue(value)
        }
    }
    
    
    override fun <T : Preference?> findPreference(charSequence: CharSequence): T? {
        return preference as T
    }
}