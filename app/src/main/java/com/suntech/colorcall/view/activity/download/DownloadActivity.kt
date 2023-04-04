package com.suntech.colorcall.view.activity.download

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.leochuan.ScaleLayoutManager
import com.suntech.colorcall.R
import com.suntech.colorcall.base.BaseActivity
import com.suntech.colorcall.constant.CommonConstant.IMAGE_URL
import com.suntech.colorcall.constant.CommonConstant.VIDEO_URL
import com.suntech.colorcall.databinding.ActivityDownloadBinding
import com.suntech.colorcall.extentions.gone
import com.suntech.colorcall.extentions.launchActivity
import com.suntech.colorcall.extentions.visible
import com.suntech.colorcall.helper.AssetsButtonHelper
import com.suntech.colorcall.helper.PreferencesHelper
import com.suntech.colorcall.model.Conf
import com.suntech.colorcall.ui_new.download.DownLoadAdapter
import com.suntech.colorcall.view.activity.apply.ApplyActivity
import com.suntech.colorcall.view.snaphelper.SnapHelperCustom
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
@AndroidEntryPoint
class DownloadActivity : BaseActivity<ActivityDownloadBinding>(), DownloadContract.View, RequestListener<Drawable> {

    private var btnDownloadVisible = true
    private val dialog by lazy {
        val dl = Dialog(this)
        dl.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dl.setContentView(R.layout.dialog_progressbar)
        dl.setCancelable(false)
        return@lazy dl
    }

    @Inject
    lateinit var mAssetsButtonHelper: AssetsButtonHelper

    @Inject
    lateinit var mPreferencesHelper: PreferencesHelper

    @Inject
    lateinit var mPresenter: DownloadContract.Presenter

    override fun layout() = ActivityDownloadBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attach(this)
        initView()
        initPermission()
    }

    override fun onStart() {
        super.onStart()

    }


    private fun initPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                }

                override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                }

            })
    }

    private fun initView() {
        binding.apply {
            ViewCompat.setTranslationZ(include.root, 10f)
            val layoutManager = ScaleLayoutManager(root.context, 20)
            val hSnapHelper = SnapHelperCustom()

            layoutManager.infinite = true
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            rcvDownload.layoutManager = layoutManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rcvDownload.setOnScrollChangeListener { _, _, _, oldScrollX, _ ->
                    if (oldScrollX in -5..5)
                        btnDownload.visible()
                    else btnDownload.gone()
                }
            } else {
                rcvDownload.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        Timber.d("$dx $dy")
                    }
                })
            }

            hSnapHelper.attachToRecyclerView(rcvDownload)
            btnDownload.gone()
            btnDownload.setOnClickListener {
                hSnapHelper.findSnapView(rcvDownload.layoutManager as ScaleLayoutManager)?.also {
                    val position = rcvDownload.getChildAdapterPosition(it)
                    mPresenter.download(position)
                }
            }
            tvError.setOnClickListener {
                mPresenter.reload()
            }
            include.btnBack.setOnClickListener { onBackPressed() }
        }

    }

    override fun updateAdapter(list: MutableList<Conf>) {
        binding.apply {
            val mAdapter = DownLoadAdapter(mPreferencesHelper, mAssetsButtonHelper)
            mAdapter.listenerDownload = this@DownloadActivity
            mAdapter.submitList(list)
            rcvDownload.adapter = mAdapter
            btnDownload.visible()
            rcvDownload.visible()
            tvError.gone()
        }
    }


    override fun goToApply(image: String, video: String) {
        launchActivity<ApplyActivity> {
            putExtra(IMAGE_URL, image)
            putExtra(VIDEO_URL, video)
        }
    }

    override fun noInternet() {
        binding.apply {
            btnDownload.gone()
            rcvDownload.gone()
            tvError.visible()
        }
    }

    private fun onLoadFailed() {
        btnDownloadVisible = false
    }

    private fun onResourceReady() {
        btnDownloadVisible = true
    }

    override fun showLoading(bool: Boolean) {
        if (bool) {
            dialog.show()
        } else dialog.dismiss()
    }

    override fun onBackPressed() {
        setResult(AppCompatActivity.RESULT_OK)
        super.onBackPressed()
    }

    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
        onLoadFailed()
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        onResourceReady()
        return false
    }
}