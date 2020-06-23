package net.c306.customcomponents.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Build
import android.text.Html
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


object CommonUtils {
    
    /**
     * Reads logcat for application and returns an input stream.
     */
    @Throws(IOException::class)
    suspend fun getLogcatStream(): InputStream = withContext(Dispatchers.IO) {
        val process = Runtime.getRuntime().exec("logcat -d -v long printable UTC")
        return@withContext process.inputStream
    }
    
    
    /**
     * Reads and returns logcat for application as a string.
     * @return Logcat for application as a string. Null if exception occurred while reading logcat
     */
    suspend fun getLogCat(): String? = withContext(Dispatchers.IO) {
        
        var log: String? = null
        var bufferedReader: BufferedReader? = null
        
        try {
            bufferedReader = BufferedReader(InputStreamReader(getLogcatStream()))
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                log += "\n" + line
            }
        } catch (e: Exception) {
            log = null
            e.printStackTrace()
        } finally {
            bufferedReader?.close()
        }
        
        return@withContext log
    }
    
    
    /**
     * Reads logcat and writes it to file in app's no_backup folder
     * @return [File] object with logcat written to it, or null if error occurred
     */
    suspend fun getLogCatFile(context: Context): File? = withContext(Dispatchers.IO) {
        
        var errors: String? = null
        var bufferedReader: BufferedReader? = null
        var file: File? = null
        
        try {
            
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(Date())
            file = File(context.filesDir, "logcat_${dateString}.txt")
            if (!file.exists()) file.createNewFile()
            
            val process = Runtime.getRuntime().exec("logcat -d -v long printable UTC -f ${file.absoluteFile}")
            
            // Need to read buffered stream to ensure the method waits till the process exits. Else it exits immediately and returns still empty file
            bufferedReader = BufferedReader(InputStreamReader(process.errorStream))
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                errors += "\n" + line
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            bufferedReader?.close()
        }
        
        return@withContext file
    }
    
    
    data class EmailDetails(
        val to: String,
        val subject: String,
        val body: String,
        val attachment: File?
    )
    
    
    /**
     * Create a text type sharing intent with given text and title
     */
    fun sendSharingIntent(
        context: Context,
        title: String,
        text: String,
        applyTitleFix: Boolean = false
    ) {
        /**
         * Why? Why, to give you a taste of your future, a preview of things to come.
         * Con permiso, Capitan. The hall is rented, the orchestra engaged.
         * It's now time to see if you can dance.
         */
        val isQorLater = applyTitleFix && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            if (isQorLater) {
                putExtra(Intent.EXTRA_TITLE, title)
            }
            type = MIME_TYPE_TEXT
        }
        
        context.startActivity(
            Intent.createChooser(sendIntent, if (isQorLater) "" else title)
        )
    }
    
    
    /**
     * Creates an email intent with given email details. Returns a chooser intent for the email intent.
     */
    fun createEmailIntent(context: Context, emailDetails: EmailDetails): Intent {
        
        val emailIntent = Intent(Intent.ACTION_SEND)
        
        // set the type to 'email'
        emailIntent.type = "message/rfc822"
        
        // to address
        val to = arrayOf(emailDetails.to)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to)
        
        // title and body
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailDetails.subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(emailDetails.body, Html.FROM_HTML_MODE_LEGACY))
        
        // attachment
        emailDetails.attachment?.let {
            val attachmentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.custom_components.email_file_provider",
                emailDetails.attachment
            )
            context.grantUriPermission(context.packageName, attachmentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            emailIntent.putExtra(Intent.EXTRA_STREAM, attachmentUri)
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        
        return Intent.createChooser(emailIntent, "Email logs")
    }
    
    
    /**
     * Returns an intent to parse the url string and open in a new task
     */
    fun getIntentForUrl(url: String): Intent {
        return getIntentForUrl(Uri.parse(url))
    }
    
    
    /**
     * Returns an intent to open the given Uri in a new task
     */
    fun getIntentForUrl(url: Uri): Intent {
        return Intent(ACTION_VIEW, url).apply {
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }
    }
    
    
    /**
     * Copies given text to clipboard, with an optional label
     */
    fun copyToClipboard(context: Context, text: String, label: String = "") {
        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(
                ClipData.newPlainText(label, text)
            )
    }
    
    
    /**
     * Returns the value of latest clipboard entry's text
     */
    fun readFromClipboard(context: Context): String? {
        val primary = (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .primaryClip?.getItemAt(0)
        
        return primary?.text?.toString()
    }
    
    
    /**
     * Generate a string of random characters of given length (default: 20).
     * Uses only alphanumeric characters by default. Suppy a list of characters to use others.
     */
    fun generateRandomString(stringLength: Int = 20, useCharacters: List<Char>? = null): String {
        val charPool: List<Char> = useCharacters ?: ('a'..'z') + ('A'..'Z') + ('0'..'9')
        
        return (1..stringLength)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
    
    
    /**
     * Returns name of manufacturer and model of the device the app is running on
     */
    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.toLowerCase(Locale.ENGLISH).startsWith(manufacturer.toLowerCase(Locale.ENGLISH)))
            model
        else "$manufacturer $model"
    }
    
    
    internal const val LOG_TAG = "CUSTOM_COMPONENTS"
    private const val MIME_TYPE_TEXT = "text/plain"
}