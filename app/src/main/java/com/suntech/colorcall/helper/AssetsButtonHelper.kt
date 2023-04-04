package com.suntech.colorcall.helper

import android.content.Context
import com.suntech.colorcall.R
import javax.inject.Inject

/**
 * kiểm tra folder assets / đường dẫn (*)
 * */
class AssetsButtonHelper @Inject constructor(private val context: Context) {
    fun setButtonCall(position: Int, updatePickUp: (bitmap1: Int, bitmap2: Int) -> Unit) {
        val pickUpBitmap = getPickUpButton(position)
        val rejectBitmap = getRejectButton(position)
        updatePickUp(pickUpBitmap, rejectBitmap)
    }

    private fun getPickUpButton(position: Int): Int {
        return when (position) {
            0 -> R.drawable.call1
            1 -> R.drawable.call2
            2 -> R.drawable.call3
            3 -> R.drawable.call4
            4 -> R.drawable.call5
            5 -> R.drawable.call6
            else -> R.drawable.call1
        }
    }

    private fun getRejectButton(position: Int): Int {
        return when (position) {
            0 -> R.drawable.cancle1
            1 -> R.drawable.cancle2
            2 -> R.drawable.cancle3
            3 -> R.drawable.cancle4
            4 -> R.drawable.cancle5
            5 -> R.drawable.cancle6
            else -> R.drawable.cancle1
        }
    }
}
