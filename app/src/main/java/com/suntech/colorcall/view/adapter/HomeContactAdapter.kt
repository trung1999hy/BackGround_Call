package com.suntech.colorcall.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suntech.colorcall.AdMob
import com.suntech.colorcall.R
import com.suntech.colorcall.constant.AppConstant
import com.suntech.colorcall.databinding.ItemHomeContactBinding
import com.suntech.colorcall.databinding.ItemNativeAdmodBinding
import com.suntech.colorcall.di.GlideApp
import com.suntech.colorcall.helper.AssetsButtonHelper
import com.suntech.colorcall.helper.PreferencesHelper
import com.suntech.colorcall.model.AdsItem
import com.suntech.colorcall.model.BaseItem
import com.suntech.colorcall.model.Contact


class HomeContactAdapter(
    private val adMob: AdMob,
    private val preferencesHelper: PreferencesHelper,
    private val assetsButtonHelper: AssetsButtonHelper
) :
    ListAdapter<BaseItem, RecyclerView.ViewHolder>(UserCallDiffCallback()) {

    var listener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> ViewHolder(ItemHomeContactBinding.inflate(layoutInflater, parent, false), listener)
            else -> AdsViewHolder(ItemNativeAdmodBinding.inflate(layoutInflater, parent, false), adMob)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val isPermissions = getItem(position) is AdsItem
        return if (isPermissions) 2 else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (getItemViewType(position)) {
            1 -> (holder as ViewHolder).binding.run {
                (getItem(position) as Contact).run {
                    if (image != null) {
                        GlideApp.with(root.context).load(image).into(imgBackground)
                    } else {
                        GlideApp.with(root.context).clear(imgBackground)
                        imgBackground.setBackgroundColor(ContextCompat.getColor(root.context, R.color.black))
                    }
                    GlideApp.with(root.context).load(avatar).into(imgAvatar)
                    tvName.text = name
                    tvPhoneNumber.text = phoneNumber
                }
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
    }

    class ViewHolder(val binding: ItemHomeContactBinding, listener: OnClickListener?) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    class AdsViewHolder(binding: ItemNativeAdmodBinding, adMob: AdMob) : RecyclerView.ViewHolder(binding.root) {
        init {
            adMob.loadNativeAds(binding.root)
        }
    }

    interface OnClickListener {
        fun onItemClick(position: Int)
    }
}

class UserCallDiffCallback : DiffUtil.ItemCallback<BaseItem>() {
    override fun areItemsTheSame(oldItem: BaseItem, newItem: BaseItem): Boolean {
        return oldItem is Contact && newItem is Contact && oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BaseItem, newItem: BaseItem): Boolean {
        return oldItem is Contact && newItem is Contact && oldItem.id == newItem.id
    }
}