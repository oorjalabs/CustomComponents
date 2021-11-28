package net.c306.customComponentsSample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import net.c306.customComponentsSample.databinding.FragmentStarterBinding
import net.c306.customcomponents.confirmation.ConfirmationDialog
import net.c306.customcomponents.updatenotes.UpdateNotesViewModel
import net.c306.customcomponents.utils.CommonUtils

/**
 * A simple [Fragment] subclass.
 */
class StarterFragment : Fragment(R.layout.fragment_starter) {
    
    private val myTag = this.javaClass.name
    
    private val confirmationViewModel by activityViewModels<ConfirmationDialog.ConfirmationViewModel>()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val binding = FragmentStarterBinding.bind(view)
        
        Log.d(LOG_TAG, "print this to log to read later with logcat")
        
        binding.openSettings.setOnClickListener {
            findNavController().navigate(R.id.action_openSettings)
        }
        
        binding.openUpdateNotes.setOnClickListener {
            val updateNotesViewModel = ViewModelProvider(requireActivity()).get(UpdateNotesViewModel::class.java)
            updateNotesViewModel.setTitle("What's new!")
            updateNotesViewModel.setShowOwnToolbar(false)
            // updateNotesViewModel.setContentResourceId(R.raw.…)
            findNavController().navigate(R.id.action_open_updateNotes)
        }
        
        binding.confirmSomething.setOnClickListener {
            val confirmationDetails = ConfirmationDialog.Details(
                callerTag = myTag,
                dialogTitle = "Confirm your being",
                dialogMessage = "To be, or not to be, that is the question",
                iconResourceId = R.drawable.ic_help,
                requestCode = 1098,
                positiveButtonTitle = "Be",
                negativeButtonTitle = "Not to be"
            )
            findNavController().navigate(R.id.action_global_confirmationDialog, bundleOf(ConfirmationDialog.KEY_CONFIRMATION_DETAILS to confirmationDetails))
        }
        
        binding.printLogcat.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                binding.tvLog.text = CommonUtils.getLogCat() ?: "No log received :("
            }
        }
        
        binding.printDeviceName.setOnClickListener {
            val deviceName = CommonUtils.getDeviceName()
            binding.tvDeviceName.text = deviceName
            CommonUtils.copyToClipboard(it.context, deviceName, "Device name")
        }
        
        binding.printRandomString.setOnClickListener {
            binding.tvRandomString.text = CommonUtils.generateRandomString()
        }
    
        binding.shareModelAndRandomString.setOnClickListener {
            val modelName = CommonUtils.getDeviceName()
            val randomString = CommonUtils.generateRandomString()
            CommonUtils.sendSharingIntent(
                it.context,
                "Model name from Sample app",
                "$modelName\n$randomString"
            )
        }
        
        binding.toastClipboard.setOnClickListener {
            val clipboardText = CommonUtils.readFromClipboard(it.context)
            Toast.makeText(it.context, clipboardText, Toast.LENGTH_SHORT).show()
        }
        
        binding.logcatAsFile.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
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
        
        confirmationViewModel.result.observe(viewLifecycleOwner, Observer {
            // Only act on results for confirmation requests made by us
            if (it == null || it.callerTag != myTag) return@Observer
            
            if (it.requestCode == 1098) {
                // Filter our own requests with request codes
                val choice = "You chose ${if (it.result) "to be" else "not to be"}"
                Toast.makeText(requireContext(), choice, Toast.LENGTH_SHORT).show()
            }
            
            // Reset the view model once done
            confirmationViewModel.reset()
        })
    }
    
    companion object {
        private const val LOG_TAG = "SAMPLE_APP"
    }
}
