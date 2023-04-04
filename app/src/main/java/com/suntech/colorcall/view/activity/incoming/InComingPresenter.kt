package com.suntech.colorcall.view.activity.incoming

import android.content.Context
import android.os.Build
import android.telecom.Call
import android.util.Log
import androidx.annotation.RequiresApi
import com.suntech.colorcall.R
import com.suntech.colorcall.constant.CommonConstant.FLASH_SETTING
import com.suntech.colorcall.helper.CallHelper
import com.suntech.colorcall.helper.ContactHelper
import com.suntech.colorcall.helper.PreferencesHelper
import com.suntech.colorcall.model.Contact
import com.suntech.colorcall.model.Contact_
import com.suntech.colorcall.ui_new.MainNewActivity
import io.objectbox.Box
import timber.log.Timber
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.M)
class InComingPresenter @Inject constructor(
    private val context: Context,
    private val mCallHelper: CallHelper,
    private val mContactBox: Box<Contact>,
    private val mContactHelper: ContactHelper,
    private val mSharePreference: PreferencesHelper
) : InComingContract.Presenter, Call.Callback() {
    private lateinit var mView: InComingContract.View
    private lateinit var mContact: Contact
    private var active = false
    private var reject = false

    override fun attach(view: InComingContract.View) {
        this.mView = view
        getData()
        mCallHelper.registerCallback(this)
        updateCallState(mCallHelper.getState())
    }

    override fun unSubscribe() {
        mCallHelper.unregisterCallback(this)
        mCallHelper.reject()
    }

    override fun pickUp() {
        mCallHelper.pickUp()
    }

    override fun reject() {
        mCallHelper.reject()
        reject = true
    }

    private fun getData() {
        mCallHelper.getPhoneNumber().let { phone ->
            mContactHelper.getNameByNumber(phone)?.let { name ->
                val a = mContactBox.query().equal(Contact_.name, name).build().find()
                a[0]?.let { contact ->
                    mContact = contact
                    Timber.d("$mContact")
                }
            } ?: let {
                //Số này không tồn tại trong danh bạ
                mContact = Contact(-2, context.getString(R.string.unknow), phone, null, null, null, false)
                Timber.d("$mContact")
            }
        }
        with(mContact) {
            mView.setData(name, phoneNumber, avatar, image, video)
        }
    }

    override fun onStateChanged(call: Call?, state: Int) {
        super.onStateChanged(call, state)
        call?.let {
            updateCallState(state)
        }
    }

     private fun updateCallState(state: Int) {
        Timber.d("$state")
        with(mContact) {
            when (state) {
                Call.STATE_RINGING -> ringing()
                Call.STATE_ACTIVE -> active()
                Call.STATE_DISCONNECTED -> name?.let {
                    if (phoneNumber != null) {
                        mView.disconnect(
                            !reject && !active && mCallHelper.missingCall,
                            it,
                            phoneNumber,
                            id
                        )
                    }
                } //missing = ringing && not pickup && not reject
                Call.STATE_CONNECTING, Call.STATE_DIALING -> dialing()
                else -> Call.STATE_RINGING
            }
        }
    }

    private fun ringing() {
        reject = false
        active = false
        with(mContact) {
            avatar?.let {
                if (phoneNumber != null) {
                    if (name != null) {
                        mView.ringing(name, phoneNumber, it)
                    }
                }
            }
                ?: let {
                    if (phoneNumber != null) {
                        if (name != null) {
                            mView.ringing(name, phoneNumber, "")

                        }
                    }
                }
        }
    }

    private fun active() {
        active = true
        with(mContact) {
            avatar?.let {
                if (phoneNumber != null) {
                    if (name != null) {
                        mView.pickUpped(name, phoneNumber, it)
                    }
                }
            }
                ?: let {
                    if (phoneNumber != null) {
                        if (name != null) {
                            mView.pickUpped(name, phoneNumber, "")
                        }
                    }
                }
        }
    }

    private fun dialing() {
        with(mContact) {
            avatar?.let {
                if (name != null) {
                    if (phoneNumber != null) {
                        mView.dialing(name, phoneNumber, it)
                    }
                }
            }
                ?: let {
                    if (name != null) {
                        if (phoneNumber != null) {
                            mView.dialing(name, phoneNumber, "")
                        }
                    }
                }
        }
    }


    override fun isFlashOn(): Boolean = mSharePreference.getBoolean(FLASH_SETTING)
}