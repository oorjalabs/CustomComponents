package net.c306.customcomponents.preference

import android.content.Context
import android.content.res.TypedArray
import android.text.format.DateFormat
import android.util.AttributeSet
import androidx.preference.DialogPreference
import net.c306.customcomponents.R
import java.util.*


/**
 * Custom preference to pick a time. Value is saved as minutes in [Int].
 * Uses @see [TimePreferenceDialogFragment] to display preference.
 */
class TimePreference(context : Context, attrs : AttributeSet? = null) : DialogPreference(context, attrs) {
    
    internal val calendar: Calendar = Calendar.getInstance()
    
    internal val mSummary: CharSequence?
        get() = DateFormat.getTimeFormat(context).format(Date(calendar.timeInMillis))
    
    
    override fun onGetDefaultValue(a: TypedArray, index: Int): Any? {
        // Read value as string, parse to int
        return a.getString(index)?.toInt()
    }
    
    
    override fun onSetInitialValue(restoreValue: Boolean, defaultValue: Any?) {
        
        val now = run {
            val c = Calendar.getInstance()
            c.get(Calendar.MINUTE) + 60 * c.get(Calendar.HOUR_OF_DAY)
        }
        
        // Set initial value to provided default value. If no default value provided, set to now
        val defaultFullMins = if (defaultValue != null) (defaultValue as Int) else now
        
        val fullMins: Int = if (restoreValue)
            getPersistedInt(defaultFullMins)
        else
            defaultFullMins
        
        val hours = fullMins / 60
        val mins = fullMins % 60
        
        calendar.set(Calendar.HOUR_OF_DAY, hours)
        calendar.set(Calendar.MINUTE, mins)
        calendar.set(Calendar.SECOND, 0)
        
        summary = mSummary
    }
    
    
    override fun getDialogLayoutResource() = R.layout.pref_dialog_time_customcomponents
    
    
    internal fun persistIntValue(value: Int) {
        persistInt(value)
    }
    
    
    companion object {
        
        fun calendarToSavedValue(cal: Calendar): Int {
            
            val hours = cal.get(Calendar.HOUR_OF_DAY)
            val minutes = cal.get(Calendar.MINUTE)
            
            return hours * 60 + minutes
        }
        
        
        fun savedValueToCalendar(fullMins: Int, cal: Calendar, forceInFuture: Boolean = false): Calendar {
            
            val hours = fullMins / 60
            val mins = fullMins % 60
            
            cal.set(Calendar.HOUR_OF_DAY, hours)
            cal.set(Calendar.MINUTE, mins)
            cal.set(Calendar.SECOND, 0)
            
            if (forceInFuture && cal.time.before(Date())) {
                cal.add(Calendar.HOUR_OF_DAY, 24)
            }
            
            return cal
        }
    }
}