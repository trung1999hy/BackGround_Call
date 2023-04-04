package com.suntech.colorcall.helper

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

class FlashHelper @Inject constructor(val context: Context) {
    private val mBlinkTime = 300L //ms
    private val mCameraManager: CameraManager by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        } else {
            TODO()
        }
    }
    private var mJob: Job? = null

    private fun getCamera(): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCameraManager.cameraIdList.forEach {
                if (checkFlashAvailable(it)) {
                    return it
                }
            }
        } else {

        }
        Timber.tag("FlashHelper:33").d("getCamera / Camera not support flash!")
        return null
    }

    private fun checkFlashAvailable(cameraId: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.FLASH_INFO_AVAILABLE)?.let {
                return it
            }
        } else {

        }
        return false
    }

    fun blinkOn() {
        getCamera()?.let { camera ->
            mJob = CoroutineScope(Dispatchers.IO).launch {
                var turn = false
                while (isActive) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mCameraManager.setTorchMode(camera, turn)
                    }
                    delay(mBlinkTime)
                    turn = !turn
                }
            }
        }
    }

    fun blinkOff() {
        getCamera()?.let { camera ->
            mJob?.cancel()
            mJob = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(camera, false)
            } else {

            }
        }
    }
}


