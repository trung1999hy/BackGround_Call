package com.suntech.colorcall.ui_new.adapter_new

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suntech.colorcall.databinding.ItemCallButtonsBinding
import com.suntech.colorcall.di.GlideApp
import com.suntech.colorcall.model.Button
import kotlinx.android.synthetic.main.item_call_buttons.view.*
import kotlinx.android.synthetic.main.item_contact_home_new.view.*

class CallButtonAdapter : ListAdapter<Button, CallButtonAdapter.ViewHolder>(DiffUtilCall()) {
    var listenerCall: OnClickCallButton? = null

    class DiffUtilCall : DiffUtil.ItemCallback<Button>() {
        override fun areItemsTheSame(oldItem: Button, newItem: Button): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Button, newItem: Button): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(binding: ItemCallButtonsBinding, listenerCall: OnClickCallButton?) : RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.root.setOnClickListener {
                listenerCall?.onClickButton(adapterPosition)
            }
        }
        fun binDataCallButton(button: Button) {
            itemView.run {
                GlideApp.with(context).load(button.pickUp).into(item_call)
                GlideApp.with(context).load(button.reject).into(item_cancel)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        val binding = ItemCallButtonsBinding.inflate(layout, parent, false)
        return ViewHolder(binding, listenerCall)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binDataCallButton(getItem(position))
    }

    interface OnClickCallButton {
        fun onClickButton(position: Int)
    }
}

