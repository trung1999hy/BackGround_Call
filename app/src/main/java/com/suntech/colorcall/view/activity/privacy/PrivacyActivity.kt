package com.suntech.colorcall.view.activity.privacy

import android.content.res.AssetManager
import android.os.Bundle
import androidx.core.view.ViewCompat
import com.suntech.colorcall.base.BaseActivity
import com.suntech.colorcall.constant.AppConstant.ABOUT_OR_PRIVACY
import com.suntech.colorcall.databinding.ActivityPrivacyBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedReader
import javax.inject.Inject
@AndroidEntryPoint
class PrivacyActivity : BaseActivity<ActivityPrivacyBinding>() {
    companion object {
        const val ABOUT = "about.txt"
        val PRIVACY = "privacy.txt"
    }

    override fun layout() = ActivityPrivacyBinding.inflate(layoutInflater)

    @Inject
    lateinit var assetsManager: AssetManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        intent?.let {
            val privacy = it.getBooleanExtra(ABOUT_OR_PRIVACY, false)
            if (privacy) {
                binding.tvPrivacy.text = readText(PRIVACY)
            } else {
                binding.tvPrivacy.text = readText(ABOUT)
            }
        }
    }

    private fun initView() {
        ViewCompat.setTranslationZ(binding.actionBar.root, 10f)
        binding.actionBar.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun readText(path: String): String {
        return assets.open(path).bufferedReader().use(BufferedReader::readText)
    }
}