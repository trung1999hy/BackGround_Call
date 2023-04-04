package com.suntech.colorcall.extentions

import android.view.HapticFeedbackConstants
import android.view.View
import android.view.ViewTreeObserver

fun View.invisibleIf(beInvisible: Boolean) = if (beInvisible) invisible() else visible()

fun View.visibleIf(beVisible: Boolean) = if (beVisible) visible() else gone()

fun View.goneIf(beGone: Boolean) = visibleIf(!beGone)

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.onGlobalLayout(callback: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            callback()
        }
    })
}
fun mutableSetOf() : Set<String> = kotlin.collections.mutableSetOf()

fun View.isVisible() = visibility == View.VISIBLE

fun View.isInvisible() = visibility == View.INVISIBLE

fun View.isGone() = visibility == View.GONE

fun View.performHapticFeedback() = performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
fun Int.per(position: Int): Int {
    return (this * position) / 100
}