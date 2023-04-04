package com.suntech.colorcall.receiver

import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.suntech.colorcall.base.BaseReceiver
import com.suntech.colorcall.view.activity.incoming.old.OldComingActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CallPhoneReceiver : BaseReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val extras = intent.extras
        if (extras != null) {
            val state = extras.getString(TelephonyManager.EXTRA_STATE)
            val phoneNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
            phoneNumber?.let {
                startIntent(context, state, phoneNumber)
            }
        }
        resultData = null
    }

    private fun startIntent(context: Context, state: String?, phone: String) {
        val comingIntent = Intent(context, OldComingActivity::class.java)
        comingIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        comingIntent.putExtra("STATE", state)
        comingIntent.putExtra("PHONE", phone)
        context.startActivity(comingIntent)
    }
}