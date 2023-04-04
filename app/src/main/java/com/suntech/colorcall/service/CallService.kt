package com.suntech.colorcall.service

import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.telecom.Call
import android.telecom.InCallService
import android.util.Log
import androidx.annotation.RequiresApi
import com.suntech.colorcall.helper.CallHelper
import com.suntech.colorcall.helper.NotificationHelper
import com.suntech.colorcall.helper.PreferencesHelper
import com.suntech.colorcall.view.activity.incoming.InComingActivity
import com.suntech.colorcall.view.activity.incoming.InComingContract
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.M)
@AndroidEntryPoint
class CallService : InCallService() {
    @Inject
    lateinit var mCallHelper: CallHelper

    @Inject
    lateinit var mNotificationHelper: NotificationHelper

    @Inject
    lateinit var mPreferences : PreferencesHelper

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        if (call.state == Call.STATE_RINGING) {
            mNotificationHelper.showInComingNotification("nam", "0111", "gffghgh")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(this, InComingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            mCallHelper.mCall = call
            mCallHelper.mInCallService = this
        }
    }


    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        mCallHelper.mCall = null
        mCallHelper.mInCallService = null
    }
}
