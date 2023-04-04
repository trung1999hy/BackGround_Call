package com.suntech.colorcall.ui_new

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.suntech.colorcall.R
import com.suntech.colorcall.base.BaseActivity
import com.suntech.colorcall.constant.AppConstant.UPLOAD_REQUEST
import com.suntech.colorcall.constant.CommonConstant.FLASH_SETTING
import com.suntech.colorcall.constant.RequestCodeConstant.SET_DEFAULT_DIALER_CODE
import com.suntech.colorcall.databinding.ActivityMainNewBinding
import com.suntech.colorcall.extentions.launchActivity
import com.suntech.colorcall.helper.AssetsButtonHelper
import com.suntech.colorcall.helper.CommonHelper
import com.suntech.colorcall.helper.PreferencesHelper
import com.suntech.colorcall.model.BaseItem
import com.suntech.colorcall.model.Contact
import com.suntech.colorcall.ui_new.adapter_new.HomeContactAdapter
import com.suntech.colorcall.ui_new.call_button.CallButtonNewActivity
import com.suntech.colorcall.ui_new.download.DownloadActivity
import com.suntech.colorcall.view.MainApp
import com.suntech.colorcall.view.activity.main.MainContract
import com.suntech.colorcall.view.inapp.PurchaseInAppActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main_new.*
import kotlinx.android.synthetic.main.layout_setting.*
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainNewActivity : BaseActivity<ActivityMainNewBinding>(), MainContract.View, HomeContactAdapter.OnClickListener {
    @Inject
    lateinit var mPresenter : MainContract.Presenter
    @Inject
    lateinit var preference : PreferencesHelper
    @Inject
    lateinit var assetButton : AssetsButtonHelper
    @Inject
    lateinit var commonHelper : CommonHelper
    private lateinit var homeContactAdapter : HomeContactAdapter
    private var mListContact : Stack<Contact> = Stack()
    private var interAvailable = false
    var uri : String?= null
    override fun layout(): ActivityMainNewBinding {
        return ActivityMainNewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attach(this)
        requestPermission(Manifest.permission.READ_CONTACTS) {
            loadContact(interAvailable)
        }
        initView()

    }

    private fun initView(){
        showHideWhenScroll()
        fab_main.setOnClickListener { addContact() }
        btn_call_buttons.setOnClickListener { launchActivity<CallButtonNewActivity> {  } }
        btn_setting.setOnClickListener {
            ic_drawer.openDrawer(navigate)
        }
        binding.apply {
            rateAppSetting.setOnClickListener { commonHelper.rateApp() }
            checkFlash.setOnCheckedChangeListener { _, isChecked -> check(isChecked) }
            checkFlash.isChecked = preference.getBoolean(FLASH_SETTING)
            textView3.setOnClickListener { checkFlash.isChecked =! checkFlash.isChecked }
            imgFlash.setOnClickListener { checkFlash.isChecked =! checkFlash.isChecked }
        }
        binding.layout.imageView.setOnClickListener {
            launchActivity<PurchaseInAppActivity>{}
        }
        binding.layout.txtGold.setOnClickListener {
            launchActivity<PurchaseInAppActivity>{}
        }

    }


   private fun check(check : Boolean){
       preference.save(FLASH_SETTING, check)
   }
    override fun onStart() {
        super.onStart()
        val getImage = preference.getString("IMAGE_PHOTO")
        if(!mListContact.isEmpty()){
            val update = mListContact.pop()
            update.avatar = getImage
        }
        binding.layout.txtGold.text = MainApp.getInstance().getPreference().valueCoin.toString()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == UPLOAD_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null){

            try {
                uri = data.data.toString()
                preference.save("IMAGE_PHOTO", uri.toString())
               // homeContactAdapter.updateContact(update, mListContact)
            }catch (e : Exception){
                e.printStackTrace()
                Toast.makeText(this, "on Field", Toast.LENGTH_SHORT).show()
            }
        }
        if(requestCode == SET_DEFAULT_DIALER_CODE) {
            when(resultCode){
                RESULT_OK -> loadContact(interAvailable)
                else -> initLoadPermissionContact()
            }
        }
    }

    private fun initLoadPermissionContact(){
        requestPermission(Manifest.permission.READ_CONTACTS){
            mPresenter.loadContact(this, interAvailable)
        }
    }
    override fun onClickChange(position: Int, base: Contact?) {
        getImageLibrary()
        mListContact.add(base)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun callNumbers(numbers : String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$numbers")
            if (callIntent.resolveActivity(packageManager) != null) {
                requestPermission(Manifest.permission.CALL_PHONE) {
                    startActivity(callIntent)
                }
            }
        }
    }

    override fun onClick(position: Int) {
        callNumbers((homeContactAdapter.currentList[position] as Contact).phoneNumber)
    }


    override fun updateAdapter(adapter: List<BaseItem>) {
        homeContactAdapter = HomeContactAdapter(preference, assetButton)
        rcv_main.setHasFixedSize(true)
        rcv_main.layoutManager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
        rcv_main.adapter = homeContactAdapter
        homeContactAdapter.listener = this
        homeContactAdapter.submitList(adapter)
    }



    private fun getImageLibrary(){
        Dexter.withContext(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.areAllPermissionsGranted()){
                        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(intent, UPLOAD_REQUEST)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {
                    p1!!.continuePermissionRequest()
                }

            }).check()
    }
    private fun loadContact(internetAvailable: Boolean) {
        mPresenter.loadContact(this, internetAvailable)
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
    //runtime permission
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
    //check an hien button
    private fun showHideWhenScroll(){
        rcv_main.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) fab_main.hide()
                else fab_main.show()
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }
    fun shouldBeShown(token: PermissionToken) {
        AlertDialog.Builder(this).setTitle(R.string.alert_title_permission)
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
    fun showPermissionDenied() {
        AlertDialog.Builder(this).setTitle(R.string.alert_title_permission)
            .setMessage(R.string.go_to_setting_message)
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + this.applicationContext.packageName)
                startActivity(intent)
            }
            .show()
    }


}