package com.suntech.colorcall.helper

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.telecom.Call
import android.telecom.InCallService
import android.telecom.VideoProfile
import com.suntech.colorcall.R
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@SuppressLint("NewApi")
@Singleton
class CallHelper @Inject constructor(val context: Context) {
    var mCall: Call? = null
    var mInCallService: InCallService? = null
    var missingCall = true
    fun pickUp() {
        mCall?.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    fun reject() {
        if (mCall != null) {
            if (mCall!!.state == Call.STATE_RINGING) {
                mCall!!.reject(false, null)
            } else {
                mCall!!.disconnect()
            }
        }
        missingCall = false
    }

    fun getPhoneNumber(): String {
        return mCall?.details?.handle?.let {
            val uri = Uri.decode(it.toString())
            return@let uri.substringAfter("tel:")
        } ?: let { context.getString(R.string.unknow) }
    }

    fun getName(phoneNumber: String? = getPhoneNumber()): String? {
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
        var contactName = ""
        val cursor = context.contentResolver?.query(uri, projection, null, null, null)
        cursor?.run {
            if (moveToFirst()) {
                contactName = getString(0)
            }
            close()
        }
        return contactName
    }

    fun registerCallback(callback: Call.Callback) {
        if (mCall != null) {
            mCall!!.registerCallback(callback)
            missingCall = true
        }
    }

    fun unregisterCallback(callback: Call.Callback) {
        mCall?.unregisterCallback(callback)
    }

    fun getState() = if (mCall == null) {
        Call.STATE_DISCONNECTED
    } else {
        Timber.d("state : ${mCall!!.state}")
        mCall!!.state
    }

/*    fun keypad(c: Char) {
        mCall?.playDtmfTone(c)
        mCall?.stopDtmfTone()
    }*/
}