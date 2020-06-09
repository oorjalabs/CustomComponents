package net.c306.customcomponents.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import kotlin.math.max

/**
 * Converts provided dp value to float pixels for device's display density
 */
fun Resources.dpToFloat(dp: Int): Float {
    return dp * displayMetrics.density
}


/**
 * Whether night mode is switched on based on resourced being used
 * [Source](https://medium.com/androiddevelopers/appcompat-v23-2-daynight-d10f90c83e94#c47a)
 */
val Resources.isNight: Boolean
    get() {
        return when (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            // Night mode is active, we're at night!
            Configuration.UI_MODE_NIGHT_YES       -> true
            
            // Night mode is not active, we're in day time
            Configuration.UI_MODE_NIGHT_NO        -> false
            
            // We don't know what mode we're in, assume notnight
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else                                  -> false
            
        }
    }


fun Context.getFloatFromXml(id: Int): Float {
    TypedValue().let {
        resources.getValue(id, it, true)
        return it.float
    }
}


fun Context.getAndroidAttributeId(id: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(id, typedValue, true)
    return typedValue.resourceId
}


enum class Direction {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT
}


fun View.addMargin(newMargin: Int, direction: Direction) {
    val menuLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
    when (direction) {
        Direction.TOP    -> menuLayoutParams.setMargins(
            marginLeft,
            marginTop + newMargin,
            marginRight,
            marginBottom
        )
        Direction.BOTTOM -> menuLayoutParams.setMargins(
            marginLeft,
            marginTop,
            marginRight,
            marginBottom + newMargin
        )
        Direction.LEFT   -> menuLayoutParams.setMargins(
            marginLeft + newMargin,
            marginTop,
            marginRight,
            marginBottom
        )
        Direction.RIGHT  -> menuLayoutParams.setMargins(
            marginLeft,
            marginTop,
            marginRight + newMargin,
            marginBottom
        )
    }
    layoutParams = menuLayoutParams
}

fun View.removeMargin(newMargin: Int, direction: Direction) {
    val menuLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
    when (direction) {
        Direction.TOP    -> menuLayoutParams.setMargins(
            marginLeft,
            max(marginTop - newMargin, 0),
            marginRight,
            marginBottom
        )
        Direction.BOTTOM -> menuLayoutParams.setMargins(
            marginLeft,
            marginTop,
            marginRight,
            max(marginBottom - newMargin, 0)
        )
        Direction.LEFT   -> menuLayoutParams.setMargins(
            max(marginLeft - newMargin, 0),
            marginTop,
            marginRight,
            marginBottom
        )
        Direction.RIGHT  -> menuLayoutParams.setMargins(
            marginLeft,
            marginTop,
            max(marginRight - newMargin, 0),
            marginBottom
        )
    }
    layoutParams = menuLayoutParams
}

fun View.setMargin(newMargin: Int, direction: Direction) {
    val menuLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
    when (direction) {
        Direction.TOP    -> menuLayoutParams.setMargins(
            marginLeft,
            newMargin,
            marginRight,
            marginBottom
        )
        Direction.BOTTOM -> menuLayoutParams.setMargins(
            marginLeft,
            marginTop,
            marginRight,
            newMargin
        )
        Direction.LEFT   -> menuLayoutParams.setMargins(
            newMargin,
            marginTop,
            marginRight,
            marginBottom
        )
        Direction.RIGHT  -> menuLayoutParams.setMargins(
            marginLeft,
            marginTop,
            newMargin,
            marginBottom
        )
    }
    layoutParams = menuLayoutParams
}


fun View.addPadding(newPadding: Int, direction: Direction) {
    when (direction) {
        Direction.TOP    -> setPadding(
            paddingLeft,
            paddingTop + newPadding,
            paddingRight,
            paddingBottom
        )
        Direction.BOTTOM -> setPadding(
            paddingLeft,
            paddingTop,
            paddingRight,
            paddingBottom + newPadding
        )
        Direction.LEFT   -> setPadding(
            paddingLeft + newPadding,
            paddingTop,
            paddingRight,
            paddingBottom
        )
        Direction.RIGHT  -> setPadding(
            paddingLeft,
            paddingTop,
            paddingRight + newPadding,
            paddingBottom
        )
    }
}

fun View.removePadding(newPadding: Int, direction: Direction) {
    when (direction) {
        Direction.TOP    -> setPadding(
            paddingLeft,
            max(paddingTop - newPadding, 0),
            paddingRight,
            paddingBottom
        )
        Direction.BOTTOM -> setPadding(
            paddingLeft,
            paddingTop,
            paddingRight,
            max(paddingBottom - newPadding, 0)
        )
        Direction.LEFT   -> setPadding(
            max(paddingLeft - newPadding, 0),
            paddingTop,
            paddingRight,
            paddingBottom
        )
        Direction.RIGHT  -> setPadding(
            paddingLeft,
            paddingTop,
            max(paddingRight - newPadding, 0),
            paddingBottom
        )
    }
}

fun View.setPadding(newPadding: Int, direction: Direction) {
    when (direction) {
        Direction.TOP    -> setPadding(paddingLeft, newPadding, paddingRight, paddingBottom)
        Direction.BOTTOM -> setPadding(paddingLeft, paddingTop, paddingRight, newPadding)
        Direction.LEFT   -> setPadding(newPadding, paddingTop, paddingRight, paddingBottom)
        Direction.RIGHT  -> setPadding(paddingLeft, paddingTop, newPadding, paddingBottom)
    }
}

