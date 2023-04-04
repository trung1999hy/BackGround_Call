package com.suntech.colorcall.helper

import timber.log.Timber

class CrashLogTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
//        if (priority == Log.ERROR || priority == Log.WARN)
//            YourCrashLibrary.log(priority, tag, message);
    }
}