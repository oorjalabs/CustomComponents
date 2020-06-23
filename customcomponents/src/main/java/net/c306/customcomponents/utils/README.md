## CommonUtils

Kotlin object with methods:

```kotlin
suspend fun getLogCat(): String?  
suspend fun getLogcatStream(): InputStream  
suspend fun getLogCatFile(context: Context): File?  

fun getIntentForUrl(url: String): Intent  
fun getIntentForUrl(url: Uri): Intent  
fun createEmailIntent(context: Context, emailDetails: EmailDetails): Intent  
fun sendSharingIntent(context: Context, text: String, title: String)

fun copyToClipboard(context: Context, text: String, label: String = "")  
fun readFromClipboard(context: Context): String?  

fun getDeviceName(): String  

fun generateRandomString(stringLength: Int = 20, useCharacters: List<Char>? = null): String  
```

#### CommonUtils.EmailDetails

```kotlin
data class EmailDetails(
    val to: String,
    val subject: String,
    val body: String,
    val attachment: File?
)
```

## ViewExtensions

Kotlin extension functions for view related properties

```kotlin
fun Resources.dpToFloat(dp: Int): Float 
val Resources.isNight: Boolean


fun Context.getFloatFromXml(id: Int): Float 
fun Context.getAndroidAttributeId(id: Int): Int 

fun View.addMargin(newMargin: Int, direction: Direction) 
fun View.removeMargin(newMargin: Int, direction: Direction) 
fun View.setMargin(newMargin: Int, direction: Direction) 

fun View.addPadding(newPadding: Int, direction: Direction) 
fun View.removePadding(newPadding: Int, direction: Direction) 
fun View.setPadding(newPadding: Int, direction: Direction) 

fun View.getStatusBarHeight(): Int 
fun View.getNavigationBarHeight(): Int 
fun View.getActionBarHeight(): Int 

fun MaterialButton.setRightIcon() 

fun TextView.addBackButton(onTouchCallback: () -> Unit) 


```

#### CenteredToast

To create a toast with toast text horizontally centered in screen

```kotlin
fun makeText(context: Context, string: CharSequence, duration: Int): Toast 
fun makeText(context: Context, @StringRes stringRes: Int, duration: Int): Toast 
```

#### Direction

Used to specify margin or padding in a particular direction

```kotlin
enum class Direction {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT
}
```

## UtilityExtensions

Kotlin extension functions for non-view related properties

```kotlin
fun String.firstLower(): String
fun String.firstUpper(): String

fun String.ellipsize(maxCharacters: Int, charactersAfterEllipsis: Int): String?

```
