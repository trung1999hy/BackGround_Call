package com.suntech.colorcall.view.activity.incoming.old

import com.suntech.colorcall.base.ActivityContract

interface OldComingContract {
    interface View : ActivityContract.View {
        fun pickUpped(name: String, phone: String, avatar: String)
        fun ringing(name: String, phone: String, avatar: String)
        fun disconnect()
        fun setData(name: String, phone: String, avatar: String?, image: String?, video: String?)
        fun dialing(name: String, phoneNumber: String, s: String)
    }

    interface Presenter : ActivityContract.Presenter<View> {
        fun pickUp()
        fun reject()
        fun isFlashOn(): Boolean
        fun updateState(state: String, phone: String)
    }
}