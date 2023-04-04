package com.suntech.colorcall.view.activity.incoming

import com.suntech.colorcall.base.ActivityContract


interface InComingContract {
    interface View : ActivityContract.View {
        fun pickUpped(name: String, phone: String, avatar: String)
        fun ringing(name: String, phone: String, avatar: String)
        fun disconnect(missingCall: Boolean, name: String, phoneNumber: String, id: Long)
        fun setData(name: String, phone: String, avatar: String?, image: String?, video: String?)
        fun dialing(name: String, phoneNumber: String, s: String)
    }

    interface Presenter : ActivityContract.Presenter<View> {
        fun pickUp()
        fun reject()
        fun unSubscribe()
        fun isFlashOn(): Boolean
    }
}