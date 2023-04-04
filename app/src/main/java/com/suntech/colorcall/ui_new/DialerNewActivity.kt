package com.suntech.colorcall.ui_new

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.PhoneAccount
import android.telecom.TelecomManager
import androidx.appcompat.app.AppCompatActivity
import com.suntech.colorcall.constant.RequestCodeConstant
import com.suntech.colorcall.extentions.isDefaultDialer
import com.suntech.colorcall.extentions.setDefaultDialerIntent
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
@AndroidEntryPoint
class DialerNewActivity : AppCompatActivity() {
    private var phoneNumber: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.action == Intent.ACTION_CALL && intent.data != null) {
            phoneNumber = intent.data
            if (!isDefaultDialer()) setDefaultDialerIntent()
            else
                initOutgoingCall()
        }
    }

    @SuppressLint("MissingPermission")
    private fun initOutgoingCall() {
        try {
            val telecomsManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val defaultHandle = telecomsManager.getDefaultOutgoingPhoneAccount(PhoneAccount.SCHEME_TEL)
                Bundle().apply {
                    putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, defaultHandle)
                    putBoolean(TelecomManager.EXTRA_START_CALL_WITH_VIDEO_STATE, false)
                    putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, false)
                    telecomsManager.placeCall(phoneNumber, this)
                }
            } else {
                Timber.tag("DialerActivity:48").d("initOutgoingCall / not support for old api for stable")
            }
            finish()
        } catch (e: Exception) {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCodeConstant.SET_DEFAULT_DIALER_CODE -> checkDefaultApp(resultCode)
        }
    }

    private fun checkDefaultApp(resultCode: Int) {
        when (resultCode) {
            RESULT_OK -> initOutgoingCall()
            else -> finish()
        }
    }
}