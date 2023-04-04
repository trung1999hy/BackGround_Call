package com.suntech.colorcall.view.activity.select

import com.suntech.colorcall.model.Contact
import com.suntech.colorcall.ui_new.MainNewActivity
import io.objectbox.Box
import javax.inject.Inject

class SelectPresenter @Inject constructor(private val mBoxContact: Box<Contact>) :
    SelectContract.Presenter {
    private lateinit var mView: SelectContract.View

    override fun attach(view: SelectContract.View) {
        this.mView = view
        getContact()
    }

    override fun selectAll(currentList: MutableList<Contact>) {
        currentList.filter { !it.checked }.run {
            if (size == 0) currentList.forEach {
                it.checked = false
            }
            else forEach {
                it.checked = true
            }
        }
        updateContact(currentList)
    }

    override fun applyContact(currentList: MutableList<Contact>) {
        val style: String = mView.getImage()
        val checkList = currentList.filter { it.checked }
        val unCheckList = currentList.filter { (!it.checked && style == it.image) }
        checkList.forEach {
            it.image = mView.getImage()
            it.video = mView.getVideo()
            it.checked = false
        }
        unCheckList.forEach {
            it.image = null
        }
        mBoxContact.put(checkList)
        mBoxContact.put(unCheckList)
        mView.goBackMain()
    }

    private fun getContact() {
        val mListContact: MutableList<Contact> = mutableListOf()
        val style: String = mView.getImage()
        mListContact.addAll(mBoxContact.all)
        mListContact.forEach {
            it.checked = it.image == style
        }
        val sortedList = mListContact.sortedWith(
            compareBy { it.name }
        )
        updateContact(sortedList)
    }

    private fun updateContact(sortedList: List<Contact>) {
        mView.updateAdapter(sortedList)
    }
}
