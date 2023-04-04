package com.suntech.colorcall.view.activity.select

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.suntech.colorcall.view.adapter.SelectContactAdapter
import com.suntech.colorcall.base.BaseActivity
import com.suntech.colorcall.constant.CommonConstant.IMAGE_URL
import com.suntech.colorcall.constant.CommonConstant.RELOAD
import com.suntech.colorcall.constant.CommonConstant.VIDEO_URL
import com.suntech.colorcall.databinding.ActivitySelectBinding
import com.suntech.colorcall.extentions.launchActivity
import com.suntech.colorcall.model.Contact
import com.suntech.colorcall.ui_new.MainNewActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectActivity : BaseActivity<ActivitySelectBinding>(), SelectContract.View, SelectContactAdapter.CheckBoxListener {
    @Inject
    lateinit var mPresenter: SelectContract.Presenter
    private var mAdapter = SelectContactAdapter(this)

    override fun layout() = ActivitySelectBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attach(this)
        initView()
    }

    private fun initView() {
        with(binding) {
            ViewCompat.setTranslationZ(actionBar.root, 10f)
            rcvListContact.layoutManager = LinearLayoutManager(this@SelectActivity)
            actionBar.btnBack.setOnClickListener { goBack() }
            actionBar.btnPlus.setOnClickListener { selectAll() }
            btnApply.setOnClickListener { applyContacts() }
        }
    }

    private fun applyContacts() {
        mPresenter.applyContact(mAdapter.currentList)
    }

    private fun selectAll() {
        mPresenter.selectAll(mAdapter.currentList)
        mAdapter.notifyDataSetChanged()
    }

    private fun goBack() {
        onBackPressed()
    }

    override fun updateAdapter(list: List<Contact>) {
        mAdapter = SelectContactAdapter(this)
        mAdapter.submitList(list)
        binding.rcvListContact.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
        updateButtonCount()
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun getImage(): String {
        return intent.getStringExtra(IMAGE_URL).toString()
    }

    override fun goBackMain() {
        launchActivity<MainNewActivity> {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(RELOAD, true)
        }
        finish()
    }

    override fun getVideo(): String? {
        return intent.getStringExtra(VIDEO_URL)
    }

    override fun onBackPressed() {
        setResult(AppCompatActivity.RESULT_CANCELED)
        super.onBackPressed()
    }

    override fun onItemSelect(position: Int) {
        mAdapter.currentList[position].apply {
            checked = !checked
        }
        mAdapter.notifyItemChanged(position)
        updateButtonCount()
    }

    private fun updateButtonCount() {
        val count = mAdapter.currentList.count { it.checked }
        binding.tvCount.text = "($count)"
    }
}