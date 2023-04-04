package com.suntech.colorcall.view.activity.apply

import android.os.Bundle
import androidx.core.view.ViewCompat
import com.suntech.colorcall.base.BaseActivity
import com.suntech.colorcall.constant.AppConstant
import com.suntech.colorcall.constant.CommonConstant.IMAGE_URL
import com.suntech.colorcall.constant.CommonConstant.VIDEO_URL
import com.suntech.colorcall.databinding.ActivityApplyBinding
import com.suntech.colorcall.di.GlideApp
import com.suntech.colorcall.extentions.launchActivity
import com.suntech.colorcall.helper.AssetsButtonHelper
import com.suntech.colorcall.helper.PreferencesHelper
import com.suntech.colorcall.view.activity.select.SelectActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ApplyActivity : BaseActivity<ActivityApplyBinding>(), ApplyContract.View {
    @Inject
    lateinit var mPresenter: ApplyContract.Presenter

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    @Inject
    lateinit var assetsButtonHelper: AssetsButtonHelper
    override fun layout() = ActivityApplyBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attach(this)
        loadImage()
        initView()
    }

    private fun initView() {
        binding.run {
            ViewCompat.setTranslationZ(actionBar.root, 10f)
            actionBar.btnBack.setOnClickListener { onBackPressed() }
            btnApply.setOnClickListener { openSelect() }

            val callButtonType = preferencesHelper.getInt(AppConstant.BUTTON_CALL)
            assetsButtonHelper.setButtonCall(
                callButtonType,
                updatePickUp = { bitmap1, bitmap2 ->
                    GlideApp.with(root.context).load(bitmap1).into(callButtons.btnPickUp)
                    GlideApp.with(root.context).load(bitmap2).into(callButtons.btnReject)
                }
            )
        }

    }

    private fun openSelect() {
        launchActivity<SelectActivity> {
            putExtra(IMAGE_URL, getImage())
            putExtra(VIDEO_URL, getVideo())
        }
    }

    private fun loadImage() {
        GlideApp.with(this).load(getImage()).into(binding.imgBackground)
    }

    override fun getImage(): String = intent.getStringExtra(IMAGE_URL) ?: ""
    override fun getVideo(): String = intent.getStringExtra(VIDEO_URL) ?: ""

}