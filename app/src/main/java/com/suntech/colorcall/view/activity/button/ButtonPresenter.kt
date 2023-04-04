package com.suntech.colorcall.view.activity.button

import com.suntech.colorcall.constant.AppConstant.BUTTON_CALL
import com.suntech.colorcall.helper.PreferencesHelper
import com.suntech.colorcall.ui_new.MainNewActivity
import javax.inject.Inject


class ButtonPresenter @Inject constructor( private val preferencesHelper: PreferencesHelper) :
    ButtonContract.Presenter {
    private lateinit var mView: ButtonContract.View

    override fun attach(view: ButtonContract.View) {
        this.mView = view
    }

    override fun saveButtonCall(position: Int) {
        preferencesHelper.save(BUTTON_CALL, position)
    }
}