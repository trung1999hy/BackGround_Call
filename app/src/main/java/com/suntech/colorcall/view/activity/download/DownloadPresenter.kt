package com.suntech.colorcall.view.activity.download

import android.content.Context
import com.suntech.colorcall.api.ApiHelper
import com.suntech.colorcall.di.GlideApp
import com.suntech.colorcall.helper.FileHelper
import com.suntech.colorcall.model.Conf
import com.suntech.colorcall.ui_new.MainNewActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class DownloadPresenter @Inject constructor(
    private val apiHelper: ApiHelper,
    private val context: Context,
    private val mFileHelper: FileHelper
) :
    DownloadContract.Presenter, CoroutineScope {
    private lateinit var mView: DownloadContract.View
    private val mMutableListStyle = mutableListOf<Conf>()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun download(position: Int) {
        val imageUrl = mMutableListStyle[position].material.big_image_url
        val videoUrl = mMutableListStyle[position].material.video_url
        save(imageUrl, videoUrl)
    }

    override fun reload() {
        getStyle()
    }

    override fun attach(view: DownloadContract.View) {
        this.mView = view
        getStyle()
    }

    private fun getStyle() {
        launch {
            try {
                val response = apiHelper.getStyle()
                response.body()?.run {
                    data.forEach {
                        mMutableListStyle.addAll(it.conf)
                    }
                }
                mMutableListStyle.forEach {
                    GlideApp.with(context).load(it.material.big_image_url)
                }
                withContext(Dispatchers.Main) {
                    mView.updateAdapter(mMutableListStyle)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mView.noInternet()
                }
            }
        }
    }

    private fun save(imageUrl: String, videoUrl: String) {
        mView.showLoading(true)
        mFileHelper.save(imageUrl, videoUrl,
            onSuccess = {
                mView.showLoading(false)
                mView.goToApply(mFileHelper.urlImageToFile(imageUrl).toString(), mFileHelper.urlVideoToFile(videoUrl).toString())
            },
            onFailure = {
                mView.showLoading(false)
            })
    }
}