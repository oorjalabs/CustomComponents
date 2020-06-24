package net.c306.customcomponents.confirmation

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.parcel.Parcelize
import net.c306.customcomponents.R
import net.c306.customcomponents.confirmation.ConfirmationDialog.ConfirmationViewModel


/**
 * ## How to use
 *
 * 1. Pass a [ConfirmationDialog.Details] object in arguments with key [ConfirmationDialog.KEY_CONFIRMATION_DETAILS] when navigating to the fragment.
 * 2. Calling fragments should observe [ConfirmationViewModel.result].
 *   1. Only act on results where [ConfirmationDialog.Result.callerTag] matches fragment's caller tag.
 *   2. Multiple confirmation requests from a fragment are identified using their [ConfirmationDialog.Result.requestCode].
 *   3. After consuming the result, call [ConfirmationViewModel.reset()].
 * 3. Colour of confirmation buttons is taken from app theme. It may be changed by adding an `alertDialogTheme` to app theme. Check the `styles.xml` in sample for how to.
 */
@Suppress("unused")
class ConfirmationDialog : DialogFragment() {
    
    companion object {
        const val KEY_CONFIRMATION_DETAILS = "confirmation_arguments"
    }

    private val confirmationViewModel: ConfirmationViewModel by activityViewModels()
    
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(requireContext())
        
        val args = arguments?.getParcelable<Details>(KEY_CONFIRMATION_DETAILS)
            ?: throw Exception("No arguments provided for Confirmation dialog")
        
        // Inflate and set the layout for the dialog (Pass null as the parent view because its going in the dialog layout)
        val contentView =
            activity?.layoutInflater?.inflate(R.layout.dialog_action_confirmation_customcomponents, null)?.apply {
                
                // Show message
                findViewById<TextView>(R.id.action_confirmation_message)?.text = args.dialogMessage
                
                findViewById<ListView>(R.id.action_confirmation_list)?.apply {
                    
                    if (args.list == null) {
                        visibility = View.GONE
                    } else {
                        // Create list view - add adapter
                        visibility = View.VISIBLE
                        adapter = ArrayAdapter(
                            context,
                            R.layout.item_action_confirmation_list_customcomponents,
                            args.list
                        )
                    }
                }
            }
        
        builder
            .setView(contentView)
            .setTitle(args.dialogTitle)
            .setPositiveButton(args.positiveButtonTitle ?: getString(android.R.string.ok)) { _, _ ->
                confirmationViewModel.setResult(
                    Result(
                        true,
                        args.callerTag,
                        args.requestCode,
                        args.returnBundle
                    )
                )
            }
            .setNegativeButton(
                args.negativeButtonTitle ?: getString(android.R.string.cancel)
            ) { _, _ ->
                confirmationViewModel.setResult(
                    Result(
                        false,
                        args.callerTag,
                        args.requestCode,
                        args.returnBundle
                    )
                )
            }
        
        if (args.iconResourceId != -1) {
            builder.setIcon(args.iconResourceId)
        }
        
        return builder.create()
    }
    
    @Parcelize
    data class Details(
        val callerTag: String,
        val requestCode: Int,
        val dialogTitle: String,
        val dialogMessage: String,
        val positiveButtonTitle: String? = null,
        val negativeButtonTitle: String? = null,
        val iconResourceId: Int = -1,
        val returnBundle: Bundle? = null,
        val list: List<String>? = null
    ) : Parcelable
    
    data class Result(
        val result: Boolean,
        val callerTag: String,
        val requestCode: Int,
        val returnBundle: Bundle? = null
    )
    
    class ConfirmationViewModel : ViewModel() {
        private val _result = MutableLiveData<Result>()
        val result: LiveData<Result> = _result
        
        // Internal so result can only be set from within
        internal fun setResult(result: Result?) {
            _result.value = result
        }
        
        // Public so the VM can be reset from observing apps
        fun reset() {
            _result.value = null
        }
    }
}
