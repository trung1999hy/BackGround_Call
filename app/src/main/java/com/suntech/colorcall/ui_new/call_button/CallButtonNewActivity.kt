package com.suntech.colorcall.ui_new.call_button

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.suntech.colorcall.R
import com.suntech.colorcall.base.BaseActivity
import com.suntech.colorcall.databinding.ActivityCallButtonNewBinding
import com.suntech.colorcall.helper.AssetsButtonHelper
import com.suntech.colorcall.model.Button
import com.suntech.colorcall.ui_new.adapter_new.CallButtonAdapter
import com.suntech.colorcall.view.activity.button.ButtonContract
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_call_button_new.*
import javax.inject.Inject
@AndroidEntryPoint
class CallButtonNewActivity : BaseActivity<ActivityCallButtonNewBinding>(), ButtonContract.View, CallButtonAdapter.OnClickCallButton {
    @Inject
    lateinit var mPresentCallButtons : ButtonContract.Presenter
    @Inject
    lateinit var mAssetButtonCall : AssetsButtonHelper
    private val mAdapterCallButton = CallButtonAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_button_new)
        mPresentCallButtons.attach(this)
        initViewAndData()
        btn_back_main.setOnClickListener { onBackPressed() }
    }

    override fun layout(): ActivityCallButtonNewBinding {
        return ActivityCallButtonNewBinding.inflate(layoutInflater)

    }

    private fun initViewAndData(){
        val mListButtonCall : MutableList<Button> = mutableListOf()
        for (i in 0..5){
            mAssetButtonCall.setButtonCall(i) { id1, id2 ->
                mListButtonCall.add(Button(i, id1, id2))
            }
        }
        mAdapterCallButton.listenerCall = this
        mAdapterCallButton.submitList(mListButtonCall)
        rcv_call_button.layoutManager = GridLayoutManager(this, 2)
        rcv_call_button.adapter = mAdapterCallButton
    }



    override fun onClickButton(position: Int) {
        mPresentCallButtons.saveButtonCall(position)
        finish()
    }

}