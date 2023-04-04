package com.suntech.colorcall.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suntech.colorcall.R
import com.suntech.colorcall.databinding.ItemSelectContactBinding
import com.suntech.colorcall.di.GlideApp
import com.suntech.colorcall.model.Contact

class SelectContactAdapter(private var listener: CheckBoxListener) :
    ListAdapter<Contact, SelectContactAdapter.ViewHolder>(DiffUtilSelectContact()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSelectContactBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            with(getItem(position)) {
                tvName.text = name
                tvPhoneNumber.text = phoneNumber
                GlideApp.with(root).load(R.drawable.img_item_contact).into(imgAvatar)
                cbSelected.isChecked = checked
                GlideApp.with(root.context).load(avatar).into(imgAvatar)
                cbSelected.setOnClickListener {
                    listener.onItemSelect(position)
                }
                root.setOnClickListener {
                    listener.onItemSelect(position)
                }
            }
        }
    }

    class ViewHolder(val binding: ItemSelectContactBinding) : RecyclerView.ViewHolder(binding.root)

    interface CheckBoxListener {
        fun onItemSelect(position: Int)
    }
}

class  DiffUtilSelectContact : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldContact: Contact, newContact: Contact) = oldContact == newContact
    override fun areContentsTheSame(oldContact: Contact, newContact: Contact) = oldContact == newContact
}