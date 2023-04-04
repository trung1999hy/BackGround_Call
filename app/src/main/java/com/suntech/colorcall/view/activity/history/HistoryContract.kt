package com.suntech.colorcall.view.activity.history

import com.suntech.colorcall.base.ActivityContract

interface HistoryContract {
    interface View : ActivityContract.View
    interface Presenter : ActivityContract.Presenter<View>
}