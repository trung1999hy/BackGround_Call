package com.suntech.colorcall.view.activity.main

import androidx.appcompat.app.AppCompatActivity
import com.suntech.colorcall.constant.AppConstant.ADSMOB_EVERY_ITEMS
import com.suntech.colorcall.helper.ContactHelper
import com.suntech.colorcall.model.AdsItem
import com.suntech.colorcall.model.BaseItem
import com.suntech.colorcall.model.Contact
import com.suntech.colorcall.model.Contact_
import io.objectbox.Box
import timber.log.Timber
import javax.inject.Inject


class MainPresenter @Inject constructor(
    private val mBoxContact: Box<Contact>,
    private val mContactHelper: ContactHelper
) : MainContract.Presenter {

    private lateinit var mView: MainContract.View

    override fun attach(view: MainContract.View) {
        this.mView = view
    }

    override fun loadContact(activity: AppCompatActivity, internetAvailable: Boolean) {
        Thread {
            val listContact: MutableList<BaseItem> = mutableListOf()
            mContactHelper.getAllContact().forEach {
                mBoxContact.get(it.id) ?: mBoxContact.put(it)
            }
            listContact.addAll(mBoxContact.query().order(Contact_.name).build().find())
            if (internetAvailable) {
                var adsCount = 0
                for (index in 0 until (listContact.size + listContact.size / (ADSMOB_EVERY_ITEMS - 1))) {
                    Timber.d("$index")
                    if (index % ADSMOB_EVERY_ITEMS == 0) {
                        listContact.add(index, AdsItem(adsCount.toLong()))
                        adsCount++
                    }
                }
            }
            activity.runOnUiThread {
                mView.updateAdapter(listContact)

            }

        }.start()


    }

}

