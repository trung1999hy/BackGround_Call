package com.suntech.colorcall.view.activity.history

import android.os.Bundle
import com.suntech.colorcall.base.BaseActivity
import com.suntech.colorcall.databinding.ActivityApplyBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class HistoryActivity : BaseActivity<ActivityApplyBinding>(), HistoryContract.View {
    @Inject
    lateinit var mPresenter: HistoryContract.Presenter

    override fun layout() = ActivityApplyBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attach(this)
    }
}