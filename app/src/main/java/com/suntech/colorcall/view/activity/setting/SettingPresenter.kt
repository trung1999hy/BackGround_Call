package com.suntech.colorcall.view.activity.setting

import com.suntech.colorcall.constant.CommonConstant.FLASH_SETTING
import com.suntech.colorcall.helper.PreferencesHelper
import com.suntech.colorcall.ui_new.MainNewActivity
import javax.inject.Inject

class SettingPresenter @Inject constructor(private val mPreferences: PreferencesHelper) : SettingContract.Presenter {
    private lateinit var mView: SettingContract.View
    override fun turnFlash(checked: Boolean) {
        mPreferences.save(FLASH_SETTING, checked)
    }

    override fun getFlashSetting() = mPreferences.getBoolean(FLASH_SETTING)

    override fun attach(view: SettingContract.View) {
        this.mView = view
    }

}