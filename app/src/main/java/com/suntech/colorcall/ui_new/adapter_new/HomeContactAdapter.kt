package com.suntech.colorcall.ui_new.adapter_new

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.suntech.colorcall.R
import com.suntech.colorcall.constant.AppConstant
import com.suntech.colorcall.databinding.ItemContactHomeNewBinding
import com.suntech.colorcall.di.GlideApp
import com.suntech.colorcall.helper.AssetsButtonHelper
import com.suntech.colorcall.helper.PreferencesHelper
import com.suntech.colorcall.model.BaseItem
import com.suntech.colorcall.model.Contact
import kotlinx.android.synthetic.main.item_contact_home_new.view.*


class HomeContactAdapter(
    private val preferenceHelper: PreferencesHelper,
    private val assetButton: AssetsButtonHelper
) : ListAdapter<BaseItem, HomeContactAdapter.ViewHolder>(DiffCallBack()) {
    var listener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        val binding = ItemContactHomeNewBinding.inflate(layout, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binData(getItem(position) as Contact, preferenceHelper, assetButton, listener)
        holder.itemView.setOnClickListener {
            listener?.onClick(position)
        }
    }

    inner class ViewHolder(val binding: ItemContactHomeNewBinding) : RecyclerView.ViewHolder(binding.root) {
        val requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
        fun binData(base: Contact, pre: PreferencesHelper, asset: AssetsButtonHelper, listener: OnClickListener?) {
            if (base.image != null) {
                Glide.with(binding.root).load(base.image).apply(requestOptions).into(binding.imgFull)
            }
            binding.imgAvtHome.setOnClickListener {
                listener?.onClickChange(adapterPosition, base)
            }

            GlideApp.with(binding.root).load(base.avatar).apply(requestOptions).placeholder(R.drawable.img_item_contact).into(binding.imgAvtHome)
            binding.tvNameHome.text = base.name
            binding.tvPhoneNumberHome.text = base.phoneNumber
            itemView.lock.visibility = View.GONE
            val callButtonType = pre.getInt(AppConstant.BUTTON_CALL)
            asset.setButtonCall(
                callButtonType,
                updatePickUp = { bitmap1, bitmap2 ->
                    GlideApp.with(binding.root).load(bitmap1).apply(requestOptions).into(binding.button.btnPickUp)
                    GlideApp.with(binding.root).load(bitmap2).apply(requestOptions).into(binding.button.btnReject)
                }
            )
        }
    }

class DiffCallBack : DiffUtil.ItemCallback<BaseItem>() {
    override fun areItemsTheSame(oldItem: BaseItem, newItem: BaseItem): Boolean {
        return oldItem is Contact && newItem is Contact && oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BaseItem, newItem: BaseItem): Boolean {
        return oldItem is Contact && newItem is Contact && oldItem.id == newItem.id
    }

}

interface OnClickListener {
    fun onClick(position: Int)
    fun onClickChange(position: Int, base: Contact?)
}
}



