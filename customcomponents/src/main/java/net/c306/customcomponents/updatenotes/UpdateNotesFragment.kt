package net.c306.customcomponents.updatenotes

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_update_notes.*
import net.c306.customcomponents.R
import net.c306.customcomponents.utils.CommonUtils.LOG_TAG
import net.c306.customcomponents.utils.dpToFloat
import net.c306.customcomponents.utils.isNight
import kotlin.math.roundToInt

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
class UpdateNotesFragment : Fragment() {
    
    private val viewModel: UpdateNotesViewModel by activityViewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_notes, container, false)
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        toolbar.setNavigationOnClickListener {
            dismiss()
        }
        
        with(update_notes_content) {
            // Set webView background based on system theme to prevent flashing white on load
            setBackgroundColor(context.getColor(R.color.update_notes_background))
            
            settings.javaScriptEnabled = true
            addJavascriptInterface(JSInterface(resources), "c306_custom_components")
        }
    
        // Set toolbar title
        viewModel.title.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            
            // Set toolbar title
            toolbar?.title = it
            
            // Also set activity title, in case using activity level toolbar
            activity?.title = it
        })
        
        // Show/hide top app bar
        viewModel.showOwnToolbar.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            
            appbar?.visibility = if (it) View.VISIBLE else View.GONE
        })
    
        // Set webView content
        viewModel.contentResourceId.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            
            update_notes_content?.loadDataWithBaseURL(
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