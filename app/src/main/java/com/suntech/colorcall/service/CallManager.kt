package com.suntech.colorcall.service

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.os.Build
import android.service.notification.NotificationListenerService
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.view.KeyEvent
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ActivityContext
import java.io.IOException
import java.lang.reflect.Method
import javax.inject.Inject


class CallManager @Inject constructor(@ActivityContext private val context: Context) {
    private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    fun acceptCall() {
        try {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    val telecomsManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
                        return
                    }
                    telecomsManager?.acceptRingingCall()
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                    throughMediaController(context)
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                    throughAudioManager()
                }
            }
        } catch (e: Exception) {
            throughReceiver(context)
        }
    }

    private fun getTelephonyService(context: Context): ITelephony? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        try {
            val c = Class.forName(tm.javaClass.name)
            val m: Method = c.getDeclaredMethod("getITelephony")
            m.setAccessible(true)
            return m.invoke(tm) as ITelephony
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun throughTelephonyService(context: Context) {
        val telephonyService: ITelephony? = getTelephonyService(context)
        if (telephonyService != null) {
            telephonyService.silenceRinger()
            telephonyService.answerRingingCall()
        }
    }

    private fun throughAudioManager() {
        val downEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK)
        val upEvent = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK)
        audioManager.dispatchMediaKeyEvent(downEvent)
        audioManager.dispatchMediaKeyEvent(upEvent)
    }

    private fun throughReceiver(context: Context) {
        try {
            throughTelephonyService(context)
        } catch (exception: Exception) {
            val broadcastConnected = ("HTC".equals(Build.MANUFACTURER, ignoreCase = true)
                    && !audioManager.isWiredHeadsetOn)
            if (broadcastConnected) {
                broadcastHeadsetConnected(false, context)
            }
            try {
                Runtime.getRuntime().exec("input keyevent " + KeyEvent.KEYCODE_HEADSETHOOK)
            } catch (ioe: IOException) {
                throughPhoneHeadsetHook(context)
            } finally {
                if (broadcastConnected) {
                    broadcastHeadsetConnected(false, context)
                }
            }
        }
    }

    private fun broadcastHeadsetConnected(connected: Boolean, context: Context) {
        val intent = Intent(Intent.ACTION_HEADSET_PLUG)
        intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY)
        intent.putExtra("state", if (connected) 1 else 0)
        intent.putExtra("name", "mysms")
        try {
            context.sendOrderedBroadcast(intent, null)
        } catch (e: Exception) {
        }
    }

    private fun throughMediaController(context: Context) {
        val mediaSessionManager: MediaSessionManager = context.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
        try {
            val controllers: List<MediaController> = mediaSessionManager.getActiveSessions(
                ComponentName(
                    context,
                    NotificationListenerService::class.java
                )
            )
            for (controller in controllers) {
                if ("com.android.server.telecom" == controller.getPackageName()) {
                    controller.dispatchMediaButtonEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK))
                    break
                }
            }
        } catch (e: Exception) {
            throughAudioManager()
        }
    }

    private fun throughPhoneHeadsetHook(context: Context) {
        val buttonDown = Intent(Intent.ACTION_MEDIA_BUTTON)
        buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK))
        context.sendOrderedBroadcast(buttonDown, "android.permission.CALL_PRIVILEGED")
        val buttonUp = Intent(Intent.ACTION_MEDIA_BUTTON)
        buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK))
        context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED")
    }

    companion object {
        private val TAG = CallManager::class.java.simpleName
    }

}

interface ITelephony {
    fun silenceRinger()
    fun answerRingingCall()
}
