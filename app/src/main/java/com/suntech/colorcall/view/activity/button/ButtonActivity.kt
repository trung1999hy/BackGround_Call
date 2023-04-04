package com.suntech.colorcall.view.activity.button

import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.suntech.colorcall.view.adapter.ButtonAdapter
import com.suntech.colorcall.base.BaseActivity
import com.suntech.colorcall.databinding.ActivityButtonBinding
import com.suntech.colorcall.extentions.gone
import com.suntech.colorcall.helper.AssetsButtonHelper
import com.suntech.colorcall.model.Button
import com.suntech.colorcall.view.decoration.SpaceVerticalDecoration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class ButtonActivity : BaseActivity<ActivityButtonBinding>(), ButtonContract.View, ButtonAdapter.OnItemClickListener {
    @Inject
    lateinit var mPresenter: ButtonContract.Presenter

    @Inject
    lateinit var assetsButtonHelper: AssetsButtonHelper

    override fun layout() = ActivityButtonBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attach(this)
        initView()
        updateAdapter()
    }

    private fun initView() {
        with(binding) {
            ViewCompat.setTranslationZ(action0.root, 10f)
            action0.btnPlus.gone()
            action0.btnMenu.setOnClickListener { backToSetting() }
            rcvButtons.layoutManager = LinearLayoutManager(this@ButtonActivity)
            rcvButtons.addItemDecoration(SpaceVerticalDecoration())
        }
    }

    private fun backToSetting() {
        onBackPressed()
    }

    private fun updateAdapter() {
        val listButton = mutableListOf<Button>()
        for (i in 0..5) {
            assetsButtonHelper.setButtonCall(i) { id1, id2 ->
                listButton.add(Button(i, id1, id2))
            }
        }
        val buttonAdapter = ButtonAdapter()
        buttonAdapter.mListener = this
        buttonAdapter.submitList(listButton)
        binding.rcvButtons.adapter = buttonAdapter
    }

    override fun onClick(position: Int) {
        mPresenter.saveButtonCall(position)
        finish()
    }
}