package com.suntech.colorcall.view.activity.incoming

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import com.suntech.colorcall.base.BaseActivity
import com.suntech.colorcall.constant.AppConstant
import com.suntech.colorcall.databinding.ActivityIncomingCallBinding
import com.suntech.colorcall.di.GlideApp
import com.suntech.colorcall.extentions.gone
import com.suntech.colorcall.extentions.visible
import com.suntech.colorcall.helper.AssetsButtonHelper
import com.suntech.colorcall.helper.FlashHelper
import com.suntech.colorcall.helper.NotificationHelper
import com.suntech.colorcall.helper.PreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InComingActivity : BaseActivity<ActivityIncomingCallBinding>(), InComingContract.View {
    @Inject
    lateinit var mPresenter: InComingContract.Presenter

    @Inject
    lateinit var mFlashHelper: FlashHelper

    @Inject
    lateinit var mNotificationHelper: NotificationHelper

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    @Inject
    lateinit var assetsButtonHelper: AssetsButtonHelper
    private var proximityWakeLock: PowerManager.WakeLock? = null

    override fun layout() = ActivityIncomingCallBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.root.gone()
        initFlags()
        initView()
        initProximitySensor()
        binding.root.visible()
        mPresenter.attach(this)
    }

    private fun initFlags() {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
    }

    private fun initView() {
        binding.callButtons.apply {
            btnPickUp.setOnClickListener {
                pickUp()
            }
            btnReject.setOnClickListener {
                reject()
            }
            btnReject2.setOnClickListener {
                reject()
            }
            val callButtonType = preferencesHelper.getInt(AppConstant.BUTTON_CALL)
            assetsButtonHelper.setButtonCall(
                callButtonType,
                updatePickUp = { bitmap1, bitmap2 ->
                    GlideApp.with(root.context).load(bitmap1).into(btnPickUp)
                    GlideApp.with(root.context).load(bitmap2).into(btnReject)
                    GlideApp.with(root.context).load(bitmap2).into(btnReject2)
                }
            )
        }
    }

    private fun reject() {
        mPresenter.reject()
    }

    private fun pickUp() {
        hideView()
        mPresenter.pickUp()
    }

    @SuppressLint("LogNotTimber")
    override fun ringing(name: String, phone: String, avatar: String) {
        mNotificationHelper.showInComingNotification(name, phone, avatar)
        if (mPresenter.isFlashOn())
            mFlashHelper.blinkOn()
    }

    override fun pickUpped(name: String, phone: String, avatar: String) {
        hideView()
        mNotificationHelper.showReceivedCall(name, phone, avatar)
        if (mPresenter.isFlashOn())
            mFlashHelper.blinkOff()
    }

    override fun disconnect(missingCall: Boolean, name: String, phoneNumber: String, id: Long) {
        mNotificationHelper.clearComingCallNotification()
//        if (missingCall) { //Sử dụng notification mặc định cho cuộc gọi nhỡ
//            Timber.d("MISSING")
//            mNotificationHelper.showMissingNotification(name, phoneNumber, phoneNumber.hashCode())
//        } else Timber.d("PICKUPPED OR REJECTED")
        if (mPresenter.isFlashOn()) {
            mFlashHelper.blinkOff()
        }
        if (proximityWakeLock?.isHeld == true) {
            proximityWakeLock!!.release()
        }
        finish()
    }

    private fun initProximitySensor() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        proximityWakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "com.suntech.colorcall:wake_lock")
        proximityWakeLock!!.acquire(10 * 60 * 1000L)
    }

    override fun setData(name: String, phone: String, avatar: String?, image: String?, video: String?) {
        binding.tvName.text = name
        binding.tvPhoneNumber.text = phone
        GlideApp.with(applicationContext).load(image).into(binding.imgBackground)
        GlideApp.with(applicationContext).load(avatar).into(binding.imgAvatar)
        video?.let { setVideo(it) }
    }

    override fun dialing(name: String, phoneNumber: String, s: String) {
        hideView()
    }

    private fun setVideo(video: String) {
        binding.vdBackground.apply {
            setOnPreparedListener { mp ->
                mp.isLooping = true
                mp.setVolume(0f, 0f)
            }
            setVideoPath(video)
            start()
        }
    }

    private fun hideView() {
        binding.apply {
            callButtons.btnReject2.visible()
            callButtons.btnPickUp.gone()
            callButtons.btnReject.gone()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.vdBackground.start()
    }

    override fun onDestroy() {
        mPresenter.unSubscribe()
        if (proximityWakeLock?.isHeld == true) {
            proximityWakeLock!!.release()
        }
        super.onDestroy()
    }
}