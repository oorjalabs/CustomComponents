package net.c306.customcomponents.updatenotes

import androidx.annotation.RawRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.c306.customcomponents.R

class UpdateNotesViewModel : ViewModel() {
    
    
    private val _seen = MutableLiveData<Boolean>()
    val seen: LiveData<Boolean> = _seen
    
    fun setSeen(value: Boolean) {
        _seen.value = value
    }
    
    private val _showOwnToolbar = MutableLiveData<Boolean>()
    val showOwnToolbar: LiveData<Boolean> = _showOwnToolbar
    
    fun setShowOwnToolbar(value: Boolean) {
        _showOwnToolbar.value = value
    }
    
    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title
    
    fun setTitle(text: String) {
        _title.value = text
    }
    
    private val _contentResourceId = MutableLiveData<Int>()
    val contentResourceId: LiveData<Int> = _contentResourceId
    
    /**
     * Resource Id needs to be name of an html file that the system can read, e.g. `R.raw.update_notes`
     */
    fun setContentResourceId(@RawRes resId: Int) {
        _contentResourceId.value = resId
    }
    
    
    init {
        // Default behaviour
        
        // Show own toolbar
        setShowOwnToolbar(true)
        // Set title
        setTitle("Update notes")
        // Show demo update notes
        setContentResourceId(R.raw.updatenotes)
        // Set not seen
        setSeen(false)
    }
}
