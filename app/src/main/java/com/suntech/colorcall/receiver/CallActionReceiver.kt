package com.suntech.colorcall.receiver

import android.content.Context
import android.content.Intent
import com.suntech.colorcall.base.BaseReceiver
import com.suntech.colorcall.constant.AppConstant.ACCEPT_CALL
import com.suntech.colorcall.constant.AppConstant.DECLINE_CALL
import com.suntech.colorcall.helper.CallHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallActionReceiver : BaseReceiver() {
    @Inject
    lateinit var callHelper: CallHelper

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACCEPT_CALL -> callHelper.pickUp()
            DECLINE_CALL -> callHelper.reject()
        }
    }
}