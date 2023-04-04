package com.suntech.colorcall.helper

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject


class NetworkUtil @Inject constructor(val context: Context) {
    companion object {
        var TYPE_WIFI = 1
        var TYPE_MOBILE = 2
        var TYPE_NOT_CONNECTED = 0
    }

    private fun getConnectivityStatus(): Int {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (null != activeNetwork) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) return TYPE_WIFI
            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) return TYPE_MOBILE
        }
        return TYPE_NOT_CONNECTED
    }

    fun getConnectivityStatusString(): Int {
        val conn = getConnectivityStatus()
        var status = 0
        when (conn) {
            TYPE_WIFI -> status = TYPE_WIFI
            TYPE_MOBILE -> status = TYPE_MOBILE
            TYPE_NOT_CONNECTED -> status = TYPE_NOT_CONNECTED
        }
        return status
    }
}