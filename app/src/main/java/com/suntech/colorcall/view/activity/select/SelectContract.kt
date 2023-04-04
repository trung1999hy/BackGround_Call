package com.suntech.colorcall.view.activity.select

import com.suntech.colorcall.base.ActivityContract
import com.suntech.colorcall.model.Contact

interface SelectContract {
    interface View : ActivityContract.View {
        fun updateAdapter(list: List<Contact>)
        fun getImage(): String
        fun goBackMain()
        fun getVideo(): String?
    }

    interface Presenter : ActivityContract.Presenter<View> {
        fun selectAll(currentList: MutableList<Contact>)
        fun applyContact(currentList: MutableList<Contact>)
    }
}