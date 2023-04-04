package com.suntech.colorcall.view.activity.history

import com.suntech.colorcall.ui_new.MainNewActivity
import javax.inject.Inject


class HistoryPresenter @Inject constructor() : HistoryContract.Presenter {
    private lateinit var mView: HistoryContract.View

    override fun attach(view: HistoryContract.View) {
        this.mView = view
    }
}