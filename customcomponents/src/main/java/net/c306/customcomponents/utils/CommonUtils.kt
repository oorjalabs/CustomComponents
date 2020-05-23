package net.c306.customcomponents.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
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
    
}