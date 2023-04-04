package com.suntech.colorcall.view.activity.setting

import android.os.Bundle
import androidx.core.view.ViewCompat
import com.suntech.colorcall.base.BaseActivity
import com.suntech.colorcall.constant.AppConstant.ABOUT_OR_PRIVACY
import com.suntech.colorcall.databinding.ActivitySettingBinding
import com.suntech.colorcall.extentions.gone
import com.suntech.colorcall.extentions.launchActivity
import com.suntech.colorcall.helper.CommonHelper
import com.suntech.colorcall.view.activity.button.ButtonActivity
import com.suntech.colorcall.view.activity.privacy.PrivacyActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>(), SettingContract.View {
    @Inject
    lateinit var mPresenter: SettingContract.Presenter

    @Inject
    lateinit var mCommonHelper: CommonHelper

    override fun layout() = ActivitySettingBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attach(this)
        initView()
    }

    private fun initView() {
        binding.apply {
            //actionBar
            ViewCompat.setTranslationZ(actionBar.root, 10f)
            actionBar.btnMenu.setOnClickListener { goHome() }
            actionBar.btnPlus.gone()
            //Home
            btnHome.setOnClickListener { goHome() }
            tvHome.setOnClickListener { goHome() }
            //Flash
            swFlash.isChecked = mPresenter.getFlashSetting()
            btnFlash.setOnClickListener { swFlash.isChecked = !swFlash.isChecked }
            tvFlash.setOnClickListener { swFlash.isChecked = !swFlash.isChecked }
            swFlash.setOnCheckedChangeListener { _, isChecked -> flash(isChecked) }
            //callBotton
            btnCallbototns.setOnClickListener { callBotton() }
            tvCallBottons.setOnClickListener { callBotton() }
            //privacy
            btnPrivacy.setOnClickListener { privacy() }
            tvPrivacy.setOnClickListener { privacy() }
            //rateApp
            btnRateApp.setOnClickListener { rateApp() }
            tvRateApp.setOnClickListener { rateApp() }
            //moreApp
            btnMoreApp.setOnClickListener { moreApp() }
            tvMoreApp.setOnClickListener { moreApp() }
            //share
            btnShare.setOnClickListener { shareApp() }
            tvShare.setOnClickListener { shareApp() }
            //adbout
            btnAbout.setOnClickListener { about() }
            tvAbout.setOnClickListener { about() }
        }
    }

    private fun about() {
        launchActivity<PrivacyActivity> {
            putExtra(ABOUT_OR_PRIVACY, false)
        }
    }

    private fun shareApp() {
        mCommonHelper.shareApp()
    }

    private fun moreApp() {
        mCommonHelper.moreApp()
    }

    private fun rateApp() {
        mCommonHelper.rateApp()
    }

    private fun privacy() {
        launchActivity<PrivacyActivity> {
            putExtra(ABOUT_OR_PRIVACY, true)
        }
    }

    private fun callBotton() {
        launchActivity<ButtonActivity> { }
    }

    private fun flash(isChecked: Boolean) {
        mPresenter.turnFlash(isChecked)
    }

    private fun goHome() {
        onBackPressed()
    }
}