package net.c306.customcomponents.utils

import java.util.*

fun String.firstLower(): String {
    val input = trim()
    
    if (input.isBlank()) return this
    
    val inLower = input.toLowerCase(Locale.getDefault())
    
    if (input.length == 1)
        return inLower
    
    return input.replaceRange(0, 1, inLower.substring(0, 1))
    
}

fun String.firstUpper(): String {
    val input = trim()
    
    if (input.isBlank()) return this
    
    val inUpper = input.toUpperCase(Locale.getDefault())
    
    if (input.length == 1)
        return inUpper
    
    return input.replaceRange(0, 1, inUpper.substring(0, 1))
    
}


/**
 * Puts ellipses in input strings that are longer than than maxCharacters. Shorter strings or
 * null is returned unchanged.
 * @param maxCharacters the maximum characters that are acceptable for the unshortended string. Must be at least 3, otherwise a string with ellipses is too long already.
 * @param charactersAfterEllipsis the number of characters that should appear after the ellipsis (0 or larger)
 * @return the truncated string with trailing ellipses
 */
fun String.ellipsize(
    maxCharacters: Int,
    charactersAfterEllipsis: Int
): String? {
    require(maxCharacters >= 3) { "maxCharacters must be at least 3 because the ellipsis already take up 3 characters" }
    require(maxCharacters - 3 >= charactersAfterEllipsis) { "charactersAfterEllipsis must be less than maxCharacters... $maxCharacters, $charactersAfterEllipsis" }
    return if (length < maxCharacters) (this + "") else substring(
        0,
        maxCharacters - 3 - charactersAfterEllipsis
    ) + "..." + substring(length - charactersAfterEllipsis)
}
