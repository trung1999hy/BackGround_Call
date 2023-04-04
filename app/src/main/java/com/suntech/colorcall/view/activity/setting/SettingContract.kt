package com.suntech.colorcall.view.activity.setting

import com.suntech.colorcall.base.ActivityContract

interface SettingContract {
    interface View : ActivityContract.View

    interface Presenter : ActivityContract.Presenter<View> {
        fun turnFlash(checked: Boolean)
        fun getFlashSetting(): Boolean
    }
}