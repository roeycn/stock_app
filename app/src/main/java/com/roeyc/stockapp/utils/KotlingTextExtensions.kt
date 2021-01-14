package com.roeyc.stockapp.utils

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.util.Patterns
import java.util.*

fun Spanned?.trimTrailingWhitespace(): CharSequence? {
    this?.let {
        var i = this.length
        // loop back to the first non-whitespace character
        while (--i >= 0 && Character.isWhitespace(this[i])) {
        }

        return this.subSequence(0, i + 1)
    }
    return null
}

fun String.fromHtml(): CharSequence? {
    val spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this,
            Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
    return spanned.trimTrailingWhitespace()
}

fun SpannableString.toHtml(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.toHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.toHtml(this)
    }
}

/**
 * Media server placeholders have the format %{param_name}
 * This extension replaces the entire placeholder with the arg
 */
fun String.replacingServerPlaceholderWith(arg: String): String {
    val start = this.indexOf("%{")
    if (start != -1) {
        val end = this.indexOf(string = "}", startIndex = start)
        if (start != -1 && end != -1 && start < end) {
            val range = IntRange(start, end)
            return this.replaceRange(range, arg)
        }
    }
    return this
}

fun String.isValidUrl(): Boolean {
    return Patterns.WEB_URL.matcher(this).matches()
}

fun String.enforceHttps(): String {
    return if (isValidUrl()) {
        replace("http://", "https://")
    } else {
        this
    }
}

fun String.getUrlWithHttp(): String {
    if (this.startsWith("http://") || this.startsWith("https://")) {
        return this
    }
    return "https://$this"
}

@JvmOverloads
fun String.maskPhoneNumber(startIndex: Int = 0, result: String = this, phoneLength: Int): String {
    val SPACE = ' '
    val LINE_SPACE = '-'
    val DIGIT_REPLACEMENT = "*"

    if (startIndex >= length-1 || phoneLength <= 0){
        return result
    }
    var index = startIndex
    var subDigits = ""
    val subToCheck = substring(startIndex)
    var tempResult = result
    val digitsIndexes = Stack<Int>()
    run breaker@ {
        subToCheck.forEach {
            if (Character.isDigit(it)) {
                subDigits += it
                digitsIndexes.push(index)
            } else {
                if (subDigits.isNotEmpty()) {
                    if (it == SPACE || it == LINE_SPACE) {
                        subDigits += it
                    } else {
                        return@breaker
                    }
                }
            }
            index ++
        }
    }
    val cleanNumber = subDigits
        .replace(SPACE.toString(), "")
        .replace(LINE_SPACE.toString(), "")
    if (cleanNumber.length >= phoneLength) {
        var replacement = ""
        var digitCounter = 0

        run breaker@ {
            for(i in subDigits.length-1 downTo 0 step 1) {
                val temp = subDigits[i]
                if (digitCounter == 4) {
                    return@breaker
                }
                if (Character.isDigit(temp)) {
                    replacement += DIGIT_REPLACEMENT
                    digitCounter++
                } else if (temp == SPACE || temp == LINE_SPACE) {
                    replacement += temp
                }
            }
        }

        val finalReplacement = subDigits
            .substring(0, subDigits.length - replacement.length) + replacement.reversed()
        tempResult = result.replace(subDigits, finalReplacement)
    }
    return maskPhoneNumber(index, tempResult, phoneLength)
}