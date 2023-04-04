package com.suntech.colorcall.ui_new.download

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.leochuan.ScaleLayoutManager
import com.suntech.colorcall.R
import com.suntech.colorcall.base.BaseActivity
import com.suntech.colorcall.constant.CommonConstant.IMAGE_URL
import com.suntech.colorcall.constant.CommonConstant.VIDEO_URL
import com.suntech.colorcall.databinding.ActivityDownloadNewBinding
import com.suntech.colorcall.extentions.gone
import com.suntech.colorcall.extentions.lToast
import com.suntech.colorcall.extentions.launchActivity
import com.suntech.colorcall.extentions.visible
import com.suntech.colorcall.helper.AssetsButtonHelper
import com.suntech.colorcall.helper.PreferencesHelper
import com.suntech.colorcall.model.Conf
import com.suntech.colorcall.view.MainApp
import com.suntech.colorcall.view.activity.download.DownloadContract
import com.suntech.colorcall.view.activity.select.SelectActivity
import com.suntech.colorcall.view.inapp.PurchaseInAppActivity
import com.suntech.colorcall.view.snaphelper.SnapHelperCustom
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_download_new.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DownloadActivity : BaseActivity<ActivityDownloadNewBinding>(), DownloadContract.View, RequestListener<Drawable> {
    @Inject
    lateinit var mPresentDownload: DownloadContract.Presenter

    @Inject
    lateinit var mDownloadHelper: PreferencesHelper

    @Inject
    lateinit var mAssetButtonDownload: AssetsButtonHelper
    private lateinit var mAdapterDownload: DownLoadAdapter
    var btnDownloadVisibleNew = false
    private val dialogDownload by lazy {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_progressbar)
        dialog.setCancelable(false)
        return@lazy dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresentDownload.attach(this)
        initView()
        binding.txtBuy.setOnClickListener{
            startActivity(Intent(this@DownloadActivity, PurchaseInAppActivity::class.java))
        }
        back.setOnClickListener { onBackPressed() }
    }

    override fun layout(): ActivityDownloadNewBinding {
        return ActivityDownloadNewBinding.inflate(layoutInflater)
    }

    override fun updateAdapter(list: MutableList<Conf>) {
        binding.apply {
            mAdapterDownload = DownLoadAdapter(mDownloadHelper, mAssetButtonDownload)
            rcvDownloadNew.adapter = mAdapterDownload
            mAdapterDownload.listenerDownload = this@DownloadActivity
            var number: Int = 0
            list.forEach {
                if (number > 4) {
                    it.apply { lock = !MainApp.getInstance().getPreference().listKeyBy.contains(number.toString()) }
                }
                number++
            }
            mAdapterDownload.submitList(list)
            btnApply.visible()
            rcvDownloadNew.visible()
            tvErrorNew.gone()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.txtGold.text = MainApp.getInstance().getPreference().valueCoin.toString()
    }


    private fun initView() {
        binding.apply {
            val layoutManager = ScaleLayoutManager(root.context, 10)
            val snapHelperLayout = SnapHelperCustom()

            layoutManager.infinite = true
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            rcvDownloadNew.layoutManager = layoutManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rcvDownloadNew.setOnScrollChangeListener { _, _, _, oldScrollX, _ ->
                    if (oldScrollX in -5..5 && btnDownloadVisibleNew)
                        btnApply.visible()
                    else btnApply.gone()
                    snapHelperLayout.findSnapView(rcvDownloadNew.layoutManager as ScaleLayoutManager)?.also {
                        val position = rcvDownloadNew.getChildAdapterPosition(it)
                        btnApply.text = if (mAdapterDownload.currentList[position].lock) "Mở khóa" else "Tải xuống"
                    }
                }
            } else {
                rcvDownloadNew.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        Timber.d("$dx $dy")
                    }
                })
            }
            snapHelperLayout.attachToRecyclerView(rcvDownloadNew)
            btnApply.gone()
            tvErrorNew.setOnClickListener { mPresentDownload.reload() }
            btnApply.setOnClickListener {
                snapHelperLayout.findSnapView(rcvDownloadNew.layoutManager as ScaleLayoutManager)?.also {
                    val position = rcvDownloadNew.getChildAdapterPosition(it)
                    if (mAdapterDownload.currentList[position].lock) {
                        MainApp.getInstance().getPreference().run {
                            if (valueCoin >= 5) {
                                var itemSet: MutableSet<String> = listKeyBy
                                itemSet.add(position.toString())
                                this.listKeyBy = itemSet
                                valueCoin -= 5
                                txtGold.text = valueCoin.toString()
                                lToast("đã mở khóa thành công")
                                updateAdapter(mAdapterDownload.currentList)
                                mAdapterDownload.notifyItemChanged(position)
                            } else {
                                startActivity(Intent(this@DownloadActivity, PurchaseInAppActivity::class.java))
                            }
                        }

                    } else mPresentDownload.download(position)
                }
            }
        }
    }

    private fun onResourceRead() {
        btnDownloadVisibleNew = true
    }

    private fun onResourceField() {
        btnDownloadVisibleNew = false
    }

    override fun goToApply(image: String, video: String) {
        launchActivity<SelectActivity> {
            putExtra(IMAGE_URL, image)
            putExtra(VIDEO_URL, video)
        }
    }
    //exception no network
    override fun noInternet() {
        binding.apply {
            btnApply.gone()
            rcvDownloadNew.gone()
            tvErrorNew.visible()
            Toast.makeText(this@DownloadActivity, "please check the network connection", Toast.LENGTH_SHORT).show()
        }
    }

    override fun showLoading(bool: Boolean) {
        if (bool) dialogDownload.show()
        else dialogDownload.hide()
    }

    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
        onResourceField()
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        onResourceRead()
        return false
    }
}