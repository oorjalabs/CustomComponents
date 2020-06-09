package net.c306.customComponentsSample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_starter.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.c306.customcomponents.updatenotes.UpdateNotesViewModel
import net.c306.customcomponents.utils.CommonUtils

/**
 * A simple [Fragment] subclass.
 */
class StarterFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_starter, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        Log.d(LOG_TAG, "print this to log to read later with logcat")
        
        open_settings.setOnClickListener {
            findNavController().navigate(R.id.action_openSettings)
        }
        
        open_update_notes.setOnClickListener {
            val updateNotesViewModel = ViewModelProvider(requireActivity()).get(UpdateNotesViewModel::class.java)
            updateNotesViewModel.setTitle("What's new!")
            updateNotesViewModel.setShowOwnToolbar(false)
            // updateNotesViewModel.setContentResourceId(R.raw.…)
            findNavController().navigate(R.id.action_open_updateNotes)
        }
        
        print_logcat.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                tv_log.text = CommonUtils.getLogCat() ?: "No log received :("
            }
        }
        
        print_device_name.setOnClickListener {
            val deviceName = CommonUtils.getDeviceName()
            tv_device_name.text = deviceName
            CommonUtils.copyToClipboard(it.context, deviceName, "Device name")
        }
        
        print_random_string.setOnClickListener {
            tv_random_string.text = CommonUtils.generateRandomString()
        }
        
        toast_clipboard.setOnClickListener {
            val clipboardText = CommonUtils.readFromClipboard(it.context)
            Toast.makeText(it.context, clipboardText, Toast.LENGTH_SHORT).show()
        }
        
        logcat_as_file.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                Log.d(LOG_TAG, "print some more stuff to log tag")
                
                val file = CommonUtils.getLogCatFile(it.context)
                
                Log.d(LOG_TAG, "file size: '${file?.length() ?: "0 :("}'")
                
                val emailIntent = CommonUtils.createEmailIntent(
                    it.context,
                    CommonUtils.EmailDetails(
                        to = "ab.something@gmail.com",
                        subject = "Testing email from wherever",
                        body = "Sample body",
                        attachment = file
                    )
                )
                
                startActivity(emailIntent)
            }
        }
    }
    
    companion object {
        private const val LOG_TAG = "SAMPLE_APP"
    }
}
