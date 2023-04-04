package com.suntech.colorcall.ui_new.download

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.suntech.colorcall.R
import com.suntech.colorcall.constant.AppConstant
import com.suntech.colorcall.databinding.ItemContactHomeNewBinding
import com.suntech.colorcall.di.GlideApp
import com.suntech.colorcall.extentions.per
import com.suntech.colorcall.helper.AssetsButtonHelper
import com.suntech.colorcall.helper.PreferencesHelper
import com.suntech.colorcall.model.Conf
import kotlinx.android.synthetic.main.item_call_button.view.*
import kotlinx.android.synthetic.main.item_contact_home_new.view.*
import kotlinx.android.synthetic.main.item_home_contact.view.*
import java.lang.Appendable

class DownLoadAdapter(
    private val downloadHelper : PreferencesHelper,
    private val assetButton : AssetsButtonHelper
) : ListAdapter<Conf, DownLoadAdapter.ViewHolderDownload>(DiffUtilCallBackDownload()){
    var listenerDownload : RequestListener<Drawable>?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDownload {
        val layout = LayoutInflater.from(parent.context)
        val binding = ItemContactHomeNewBinding.inflate(layout, parent, false)
        return ViewHolderDownload(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderDownload, position: Int) {
        listenerDownload?.let { holder.binDataDownload(getItem(position), assetButton, downloadHelper, it) }
    }
    inner class ViewHolderDownload(binding : ItemContactHomeNewBinding) : RecyclerView.ViewHolder(binding.root){
        fun binDataDownload(conf : Conf, assetButton : AssetsButtonHelper,  downloadHelper : PreferencesHelper, listener : RequestListener<Drawable>){
            itemView.run {
                val callButton = downloadHelper.getInt(AppConstant.BUTTON_CALL)
                assetButton.setButtonCall(
                    callButton,
                    updatePickUp = {bitmap1, bitmap2 ->
                        Glide.with(context).load(bitmap1).into(button.btn_pick_up)
                        Glide.with(context).load(bitmap2).into(button.btn_reject)
                    }
                )
                itemView.lock.visibility = if (conf.lock)View.VISIBLE else View.GONE

                val metric = context.resources.displayMetrics
                val layoutParam = FrameLayout.LayoutParams(metric.widthPixels.per(60), metric.heightPixels.per(60))
                layoutParams = layoutParam
                GlideApp.with(context).load(conf.material.big_image_url).listener(listener)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(img_full)
                GlideApp.with(context).load(R.drawable.img_item_contact).into(img_avt_home)
                tv_name_Home.text = context.getString(R.string.app_name_download)
                tv_phone_number_home.text = context.getString(R.string.number_download)

            }
        }
    }
    class  DiffUtilCallBackDownload : DiffUtil.ItemCallback<Conf>(){
        override fun areItemsTheSame(oldItem: Conf, newItem: Conf): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Conf, newItem: Conf): Boolean {
            return  oldItem == newItem
        }

    }

}
