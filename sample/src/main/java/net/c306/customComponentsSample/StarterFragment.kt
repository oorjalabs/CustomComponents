package net.c306.customComponentsSample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_starter.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    
        Log.d("SAMPLE_APP", "print this to logcat to read later")
        
        open_settings.setOnClickListener {
            findNavController().navigate(R.id.action_openSettings)
        }
        
        print_logcat.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                // Ironic. Reading log and testing it by printing it back to logcat!
                val log = CommonUtils.getLogCat()
    
                tv_log.text = log ?: "No log received :("
                
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
    }
}
