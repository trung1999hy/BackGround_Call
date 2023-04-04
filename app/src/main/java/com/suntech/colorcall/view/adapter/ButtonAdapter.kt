package com.suntech.colorcall.view.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suntech.colorcall.databinding.ItemCallButtonBinding
import com.suntech.colorcall.di.GlideApp
import com.suntech.colorcall.model.Button

class ButtonAdapter : ListAdapter<Button, ButtonAdapter.ViewHolder>(ButtonDiffCallback()) {
    var mListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemCallButtonBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.mBinding) {
            GlideApp.with(root.context).load(getItem(position).reject).into(btnReject)
            GlideApp.with(root.context).load(getItem(position).pickUp).into(btnPickUp)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                root.setBackgroundColor(root.context.getColor((android.R.color.black)))
            } else {
                root.setBackgroundColor(ContextCompat.getColor(root.context, android.R.color.black))
            }
        }
    }

    class ViewHolder(
        val mBinding: ItemCallButtonBinding,
        onClick: OnItemClickListener?
    ) : RecyclerView.ViewHolder(mBinding.root) {
        init {
            mBinding.root.setOnClickListener {
                onClick?.onClick(adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
    }
}

class ButtonDiffCallback : DiffUtil.ItemCallback<Button>() {
    override fun areItemsTheSame(oldItem: Button, newItem: Button) = newItem == oldItem
    override fun areContentsTheSame(oldItem: Button, newItem: Button) = newItem == oldItem
}