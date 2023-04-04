package com.suntech.colorcall.view

import android.app.Application
import android.content.Context
import com.downloader.BuildConfig
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.suntech.colorcall.helper.createNotificationChannel
import com.suntech.colorcall.view.inapp.Preference
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApp : Application() {
    private lateinit var preference: Preference

    companion object {
        private lateinit var instance: MainApp

        @JvmStatic
        fun getInstance(): MainApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        initPRDownload()
        initTimber()
        instance = this
        preference = Preference.buildInstance(this)
        if (!preference.fistInstallApp){
            preference.fistInstallApp = false
            preference.valueCoin = 5
        }
    }

    private fun initPRDownload() {
        val config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .build()
        PRDownloader.initialize(this, config)
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }


    fun getPreference(): Preference {
        return preference
    }

    fun context(): Context {
        return applicationContext
    }
}