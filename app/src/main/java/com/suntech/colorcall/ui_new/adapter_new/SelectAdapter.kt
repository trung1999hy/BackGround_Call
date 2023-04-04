package com.suntech.colorcall.ui_new.adapter_new

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suntech.colorcall.R
import com.suntech.colorcall.databinding.ItemSelectBinding
import com.suntech.colorcall.di.GlideApp
import com.suntech.colorcall.model.Contact

class SelectAdapter(val mListenerCheckBox: ClickListener) : ListAdapter<Contact, SelectAdapter.ViewHolder>(DiffUtiSelect()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        val binding = ItemSelectBinding.inflate(layout, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binDataSelect(getItem(position), mListenerCheckBox)
    }

    inner class ViewHolder(val binding: ItemSelectBinding) : RecyclerView.ViewHolder(binding.root) {
        fun binDataSelect(contact: Contact, listener: ClickListener) {
            itemView.run {
                binding.apply {
                    tvNameSelect.text = contact.name
                    tvPhoneNumberSelect.text = contact.phoneNumber
                    if (contact.avatar != null) {
                        GlideApp.with(context).load(contact.avatar).placeholder(R.drawable.img_item_contact).into(imgSelect)
                    }
                    checkSelect.isChecked = contact.checked
                    checkSelect.setOnClickListener {
                        listener.onItemClickCheckBox(adapterPosition)
                    }
                    root.setOnClickListener {
                        listener.onItemClickCheckBox(adapterPosition)
                    }
                }
            }
        }
    }

    interface ClickListener {
        fun onItemClickCheckBox(position: Int)
    }

    class DiffUtiSelect : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }

    }

}
