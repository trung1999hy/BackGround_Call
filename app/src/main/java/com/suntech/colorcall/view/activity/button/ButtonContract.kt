package com.suntech.colorcall.view.activity.button

import com.suntech.colorcall.base.ActivityContract

interface ButtonContract {
    interface View : ActivityContract.View

    interface Presenter : ActivityContract.Presenter<View> {
        fun saveButtonCall(position: Int)
    }
}