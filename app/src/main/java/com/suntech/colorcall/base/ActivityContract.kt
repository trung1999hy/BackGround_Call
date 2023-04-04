package com.suntech.colorcall.base



class ActivityContract {
    interface Presenter<T> : BaseContract.Presenter<T>
    interface View : BaseContract.View
}

open class BaseContract {
    interface Presenter<in T> {
        fun attach(view: T)
    }
    interface View
}



