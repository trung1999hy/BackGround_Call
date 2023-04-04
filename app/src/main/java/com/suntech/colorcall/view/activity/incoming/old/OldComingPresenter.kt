package com.suntech.colorcall.view.activity.incoming.old

import android.content.Context
import android.telephony.TelephonyManager
import com.suntech.colorcall.R
import com.suntech.colorcall.constant.CommonConstant.FLASH_SETTING
import com.suntech.colorcall.helper.ContactHelper
import com.suntech.colorcall.helper.PreferencesHelper
import com.suntech.colorcall.model.Contact
import com.suntech.colorcall.model.Contact_
import com.suntech.colorcall.ui_new.MainNewActivity
import io.objectbox.Box
import javax.inject.Inject


class OldComingPresenter @Inject constructor(
    context: Context,
    private val mContactBox: Box<Contact>,
    private val mContactHelper: ContactHelper,
    private val mSharePreference: PreferencesHelper
) : OldComingContract.Presenter {
    private lateinit var mView: OldComingContract.View
    private var mContact = Contact(-2, context.getString(R.string.unknow), "", null, null, null, false)

    override fun attach(view: OldComingContract.View) {
        this.mView = view
    }

    override fun pickUp() {

    }

    override fun reject() {

    }

    override fun updateState(state: String, phone: String) {
        getData(phone)
        when (state) {
            TelephonyManager.EXTRA_STATE_IDLE -> {
                mView.disconnect()
            }
            TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                active()
            }
            TelephonyManager.EXTRA_STATE_RINGING -> {
                ringing()
            }
        }
    }

    private fun getData(phoneNumber: String) {
        phoneNumber.let { phone ->
            mContactHelper.getNameByNumber(phone)?.let { name ->
                val a = mContactBox.query().equal(Contact_.name, name).build().find()
                a[0]?.let { contact ->
                    mContact = contact
                }
            }
        }
        with(mContact) {
            mView.setData(name, phoneNumber, avatar, image, video)
        }
    }

    private fun ringing() {
        with(mContact) {
            avatar?.let { mView.ringing(name, phoneNumber, it) }
                ?: let { mView.ringing(name, phoneNumber, "") }
        }
    }

    private fun active() {
        with(mContact) {
            avatar?.let { mView.pickUpped(name, phoneNumber, it) }
                ?: let { mView.pickUpped(name, phoneNumber, "") }
        }
    }

    private fun dialing() {
        with(mContact) {
            avatar?.let { mView.dialing(name, phoneNumber, it) }
                ?: let { mView.dialing(name, phoneNumber, "") }
        }
    }

    override fun isFlashOn(): Boolean = mSharePreference.getBoolean(FLASH_SETTING)
}