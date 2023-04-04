package com.suntech.colorcall.helper

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.suntech.colorcall.R
import com.suntech.colorcall.constant.AppConstant
import com.suntech.colorcall.receiver.CallActionReceiver
import com.suntech.colorcall.view.activity.incoming.InComingActivity
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    private val context: Context,
    private val notificationManager: NotificationManagerCompat
) {
    companion object {
        const val COMING_CALL_CHANNEL_ID = "COMING_CALL_CHANNEL_ID"
        const val COMING_CALL_NOTIFICATION_ID: Int = -1
        const val COMING_CALL_NOTIFICATION_GROUP = "COMING_CALL_NOTIFICATION_GROUP"
    }

    fun showInComingNotification(
        name: String = context.getString(R.string.unknow),
        phone: String = context.getString(R.string.unknow),
        avatar: String
    ) {
        notificationManager.notify(COMING_CALL_NOTIFICATION_ID, createComingCallNotification(name, phone, avatar))
    }

    fun showReceivedCall(name: String, phone: String, avatar: String) {
        notificationManager.notify(COMING_CALL_NOTIFICATION_ID, createReceiveCallNotification(name, phone, avatar))
    }

    fun clearComingCallNotification() {
        notificationManager.cancel(COMING_CALL_NOTIFICATION_ID)
    }

    private fun createInComingIntent(): PendingIntent {
        val intentActivity = Intent(context, InComingActivity::class.java)
        intentActivity.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        return PendingIntent.getActivity(context, 0, intentActivity, 0)
    }

    private fun createInComingCallRemoteView(name: String, phone: String, avatar: String, hideView: Boolean = false): RemoteViews {
        val acceptCallIntent = Intent(context, CallActionReceiver::class.java)
        acceptCallIntent.action = AppConstant.ACCEPT_CALL
        val acceptPendingIntent = PendingIntent.getBroadcast(context, 0, acceptCallIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val rejectCallIntent = Intent(context, CallActionReceiver::class.java)
        rejectCallIntent.action = AppConstant.DECLINE_CALL
        val rejectPendingIntent = PendingIntent.getBroadcast(context, 1, rejectCallIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        return RemoteViews(context.packageName, R.layout.notifi_coming_call).apply {
            setTextViewText(R.id.tv_name, name)
            setTextViewText(R.id.tv_phone, phone)
            setImageViewUri(R.id.img_avatar, Uri.parse(avatar))
            setOnClickPendingIntent(R.id.btn_pick_up, acceptPendingIntent)
            setOnClickPendingIntent(R.id.btn_reject, rejectPendingIntent)
            if (hideView) {
                setViewVisibility(R.id.btn_pick_up, View.GONE)
            }
        }
    }

    private fun createComingCallNotification(name: String, phone: String, avatar: String): Notification {

        return NotificationCompat.Builder(context, COMING_CALL_CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(Notification.CATEGORY_CALL)
            .setGroup(COMING_CALL_NOTIFICATION_GROUP)
            .setSmallIcon(R.drawable.icon_call_button)
            .setContentIntent(createInComingIntent())
            .setCustomContentView(createInComingCallRemoteView(name, phone, avatar))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setAutoCancel(true)
            .setSound(null)
            .build()
    }

    private fun createReceiveCallNotification(name: String, phone: String, avatar: String): Notification {
        return NotificationCompat.Builder(context, COMING_CALL_CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_call_button)
            .setContentTitle(name)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(createInComingIntent())
            .setGroup(COMING_CALL_NOTIFICATION_GROUP)
            .setOngoing(true)
            .setCustomContentView(createInComingCallRemoteView(name, phone, avatar, true))
            .setCategory(Notification.CATEGORY_CALL)
            .setUsesChronometer(true)
            .build()
    }
}

fun Context.createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //Channel1
        val comingCallChannel1 = NotificationChannel(
            NotificationHelper.COMING_CALL_CHANNEL_ID,
            getString(R.string.coming_call_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        comingCallChannel1.setSound(null, null)
        comingCallChannel1.description = getString(R.string.comingCallChannelDescription)

        //Channel2
        val comingCallChannel2 = NotificationChannel(
            NotificationHelper.COMING_CALL_CHANNEL_ID,
            getString(R.string.coming_call_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        comingCallChannel2.setSound(null, null)
        comingCallChannel2.description = getString(R.string.comingCallChannelDescription)

        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(comingCallChannel1)
        manager?.createNotificationChannel(comingCallChannel2)
    }
}