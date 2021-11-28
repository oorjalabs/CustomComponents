package net.c306.customcomponents.updatenotes

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import net.c306.customcomponents.R
import net.c306.customcomponents.utils.CommonUtils.LOG_TAG
import net.c306.customcomponents.utils.dpToFloat
import net.c306.customcomponents.utils.isNight
import kotlin.math.roundToInt
import net.c306.customcomponents.databinding.FragmentUpdateNotesCustomcomponentsBinding as FragmentUpdateNotesCustomcomponentsBinding1

/**
 * ## HOW TO USE
 * 1. Preferably use with navigation component.
 * 2. Before navigating to fragment, set title and notes resource Id in [UpdateNotesViewModel].
 * 3. To enable javascript interaction (day/night theme, link colour, padding), update javascript
 * interface to use `c306_custom_components`
 *
 * ## HOW TO CREATE A UPDATE NOTES FILE
 * 1. Copy contents of `/updatenotes` to root of project folder
 * 2. Write update notes in the `updatenotes.md` file.
 * 3. Run gulp file—it will generate updatenotes.html, adding js and css, and copy it to raw resources folder
 */
class UpdateNotesFragment : Fragment(R.layout.fragment_update_notes_customcomponents) {
    
    private val viewModel: UpdateNotesViewModel by activityViewModels()
    
    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val binding = FragmentUpdateNotesCustomcomponentsBinding1.bind(view)
        
        // Mark notes as seen. Apps can observe this variable to take action when notes are seen
        viewModel.setSeen(true)
        
        binding.toolbar.setNavigationOnClickListener {
            dismiss()
        }
        
        with(binding.updateNotesContent) {
            // Set webView background based on system theme to prevent flashing white on load
            setBackgroundColor(context.getColor(R.color.update_notes_background))
            
            settings.javaScriptEnabled = true
            addJavascriptInterface(JSInterface(resources), "c306_custom_components")
        }
    
        // Set toolbar title
        viewModel.title.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            
            // Set toolbar title
            binding.toolbar.title = it
            
            // Also set activity title, in case using activity level toolbar
            activity?.title = it
        })
        
        // Show/hide top app bar
        viewModel.showOwnToolbar.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            
            binding.appbar.visibility = if (it) View.VISIBLE else View.GONE
        })
    
        // Set webView content
        viewModel.contentResourceId.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            
            binding.updateNotesContent.loadDataWithBaseURL(
                null,
                resources.openRawResource(it).bufferedReader().readText(),
                "text/html; charset=utf-8",
                "UTF-8",
                null
            )
        })
        
    }
    
    @Suppress("unused")
    @Keep
    inner class JSInterface(private val resources: Resources) {
        
        @JavascriptInterface
        fun isDark(): Boolean {
            return resources.isNight
        }
        
        @JavascriptInterface
        fun getPadding(): Int {
            return resources.dpToFloat(4).roundToInt()
        }
        
        @SuppressLint("ResourceType")
        @JavascriptInterface
        fun getLinkColour(): String {
            return requireContext().getString(R.color.update_notes_link_colour)
        }
        
    }
    
    
    private fun dismiss() {
        try {
            findNavController().navigateUp()
        } catch (e: IllegalStateException) {
            Log.v(LOG_TAG,"Not using with navigation controller. Trying with fragment manager...")
            activity?.supportFragmentManager?.popBackStack()
        }
    }
    
}