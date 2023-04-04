package com.suntech.colorcall.view.activity.apply

import android.net.Uri
import com.suntech.colorcall.ui_new.MainNewActivity
import javax.inject.Inject


class ApplyPresenter @Inject constructor() : ApplyContract.Presenter {
    private lateinit var mView: ApplyContract.View

    override fun attach(view: ApplyContract.View) {
        this.mView = view
    }

    override fun uriBackGround(): Uri {
        return Uri.parse(mView.getImage())
    }



}