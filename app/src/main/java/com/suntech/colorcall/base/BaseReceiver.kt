package com.suntech.colorcall.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper
import com.suntech.colorcall.view.MainApp

/***
 * Hilt in beta version, so this it the trick to inject feild to BroadcastReceiver - Haitrvn
 */
abstract class BaseReceiver : BroadcastReceiver() {
    @CallSuper //<<<-this is the trick
    override fun onReceive(context: Context, intent: Intent) {
        val baseApp =  context.applicationContext as MainApp
    }
}
