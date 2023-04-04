package com.suntech.colorcall.view.activity.main

import androidx.appcompat.app.AppCompatActivity
import com.suntech.colorcall.base.ActivityContract
import com.suntech.colorcall.model.BaseItem

interface MainContract {
    interface View : ActivityContract.View {
        fun updateAdapter(adapter: List<BaseItem>)
    }

    interface Presenter : ActivityContract.Presenter<View> {
        fun loadContact(activity: AppCompatActivity, internetAvailable: Boolean)
    }
}