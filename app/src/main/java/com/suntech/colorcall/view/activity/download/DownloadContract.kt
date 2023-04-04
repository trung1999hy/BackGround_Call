package com.suntech.colorcall.view.activity.download

import com.suntech.colorcall.base.ActivityContract
import com.suntech.colorcall.model.Conf

interface DownloadContract {
    interface View : ActivityContract.View {
        fun updateAdapter(list: MutableList<Conf>)
        fun goToApply(image: String, video: String)
        fun noInternet()
        fun showLoading(bool: Boolean)
    }

    interface Presenter : ActivityContract.Presenter<View> {
        fun download(position: Int)
        fun reload()
    }
}