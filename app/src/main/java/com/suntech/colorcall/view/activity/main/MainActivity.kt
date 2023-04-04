package com.suntech.colorcall.view.activity.main

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.suntech.colorcall.AdMob
import com.suntech.colorcall.R
import com.suntech.colorcall.view.adapter.HomeContactAdapter
import com.suntech.colorcall.base.BaseActivity
import com.suntech.colorcall.constant.AppConstant.ADSMOB_EVERY_ITEMS
import com.suntech.colorcall.constant.RequestCodeConstant.SET_DEFAULT_DIALER_CODE
import com.suntech.colorcall.databinding.ActivityMainBinding
import com.suntech.colorcall.extentions.isDefaultDialer
import com.suntech.colorcall.extentions.launchActivity
import com.suntech.colorcall.extentions.setDefaultDialerIntent
import com.suntech.colorcall.helper.AssetsButtonHelper
import com.suntech.colorcall.helper.PreferencesHelper
import com.suntech.colorcall.model.BaseItem
import com.suntech.colorcall.model.Contact
import com.suntech.colorcall.view.activity.download.DownloadActivity
import com.suntech.colorcall.view.activity.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), MainContract.View, HomeContactAdapter.OnClickListener {
    @Inject
    lateinit var mPresenter: MainContract.Presenter

    @Inject
    lateinit var mAdMob: AdMob

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    @Inject
    lateinit var assetsButtonHelper: AssetsButtonHelper
    private lateinit var mAdapter: HomeContactAdapter

    private var internetAvailable = false

    override fun layout() = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attach(this)
    }

    override fun onResume() {
        super.onResume()
        initView()
        if (!isDefaultDialer()) setDefaultDialerIntent()
        else initLoadContactPermission()
    }

    @SuppressLint("MissingPermission", "QueryPermissionsNeeded")
    private fun callNumber(numberPhone: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$numberPhone")
            if (callIntent.resolveActivity(packageManager) != null) {
                requestPermission(Manifest.permission.CALL_PHONE) {
                    startActivity(callIntent)
                }
            }
        }
    }

    private fun initView() {
        binding.apply {
            ViewCompat.setTranslationZ(actionBar.root, 10f)
            actionBar.btnPlus.setOnClickListener { addContact() }
            actionBar.btnMenu.setOnClickListener { openMenu() }
        }
    }

    override fun updateAdapter(adapter: List<BaseItem>) {
        val layoutManager = GridLayoutManager(this@MainActivity, 2)
        if (internetAvailable) {
            layoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position % ADSMOB_EVERY_ITEMS == 0) 2 else 1
                }
            }
        }
        binding.rcvUserCall.layoutManager = layoutManager
        mAdapter = HomeContactAdapter(mAdMob, preferencesHelper, assetsButtonHelper)
        mAdapter.listener = this
        mAdapter.submitList(adapter)
        binding.rcvUserCall.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SET_DEFAULT_DIALER_CODE -> checkDefaultApp(resultCode)
        }
    }

    private fun checkDefaultApp(resultCode: Int) {
        when (resultCode) {
            RESULT_OK -> loadContact(internetAvailable)
            else -> initLoadContactPermission()
        }
    }

    private fun initLoadContactPermission() {
        requestPermission(Manifest.permission.READ_CONTACTS) {
            loadContact(internetAvailable)
        }
    }

    private fun loadContact(internetAvailable: Boolean) {
        mPresenter.loadContact(this, internetAvailable)
    }

    fun showPermissionDenied() {
        AlertDialog.Builder(this@MainActivity).setTitle(R.string.alert_title_permission)
            .setMessage(R.string.go_to_setting_message)
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + this@MainActivity.applicationContext.packageName)
                startActivity(intent)
            }
            .show()
    }

    fun shouldBeShown(token: PermissionToken) {
        AlertDialog.Builder(this@MainActivity).setTitle(R.string.alert_title_permission)
            .setMessage(R.string.alert_permission_message)
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
                token.cancelPermissionRequest()
            }
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                token.continuePermissionRequest()
            }
            .setOnDismissListener { token.cancelPermissionRequest() }
            .show()
    }

    private fun openMenu() {
        launchActivity<SettingActivity> { }
    }

    private fun addContact() {
        Dexter.withContext(this).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    p0?.grantedPermissionResponses?.let {
                        if (it.size > 0) {
                            launchActivity<DownloadActivity> { }
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {
                    p1?.let { shouldBeShown(it) }
                }
            }).check()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onItemClick(position: Int) {
        Timber.d("Clicked, $position, ${mAdapter.currentList[position]}")
        callNumber((mAdapter.currentList[position] as Contact).phoneNumber)
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(internetState, intentFilter)
    }

    private val internetState: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == ConnectivityManager.CONNECTIVITY_ACTION) {
                    context?.let { context ->
                        val internetAvailable = checkInternet(context)
                        netWorkChange(internetAvailable)
                    }
                }
            }
        }
    }

    private fun checkInternet(context: Context): Boolean {
        var connected = false
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED
        return connected
    }

    fun netWorkChange(result: Boolean) {
        internetAvailable = result
        initLoadContactPermission()
    }

    private fun requestPermission(permission: String, permissionGranted: () -> Unit) {
        Dexter.withContext(this)
            .withPermission(permission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    permissionGranted()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    showPermissionDenied()
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                    shouldBeShown(token)
                }
            }).check()
    }


}