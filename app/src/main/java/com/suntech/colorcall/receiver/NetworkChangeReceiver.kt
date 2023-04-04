package com.suntech.colorcall.receiver

import android.content.Context
import android.content.Intent
import com.suntech.colorcall.base.BaseReceiver
import com.suntech.colorcall.helper.NetworkUtil
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class NetworkChangeReceiver @Inject constructor() : BaseReceiver() {
    @Inject
    lateinit var networkUtil: NetworkUtil

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Timber.d("NETWORK HAITRVN ABC")
    }
}