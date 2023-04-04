package com.suntech.colorcall.extentions

import androidx.appcompat.app.AppCompatActivity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.widget.Toast
import com.suntech.colorcall.constant.RequestCodeConstant
inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Any> AppCompatActivity.launchActivityForResult(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

inline fun <reified T : Any> newIntent(context: Context): Intent = Intent(context, T::class.java)

fun AppCompatActivity.sToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.lToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun AppCompatActivity.setDefaultDialerIntent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val roleManager = getSystemService(RoleManager::class.java)
        if (roleManager!!.isRoleAvailable(RoleManager.ROLE_DIALER) && !roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
            startActivityForResult(intent, RequestCodeConstant.SET_DEFAULT_DIALER_CODE)
        }
    } else {
        Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
            .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
            .apply {
                if (resolveActivity(packageManager) != null) {
                    startActivityForResult(this, RequestCodeConstant.SET_DEFAULT_DIALER_CODE)
                }
            }
    }
}

fun AppCompatActivity.isDefaultDialer(): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        false
    else {
        val telecomsManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        telecomsManager.defaultDialerPackage == packageName
    }
}