package com.suntech.colorcall.view.activity.apply

import android.net.Uri
import com.suntech.colorcall.base.ActivityContract

interface ApplyContract {
    interface View : ActivityContract.View {
        fun getImage(): String
        fun getVideo(): String
    }

    interface Presenter : ActivityContract.Presenter<View> {
        fun uriBackGround(): Uri
    }
}