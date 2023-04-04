package com.suntech.colorcall

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@Suppress("DEPRECATION")
class AdMob @Inject constructor(@ApplicationContext val context: Context) {

    private val ID_BANNER_AD = "ca-app-pub-3940256099942544/6300978111"
    private val ID_INTERSTITIAL_AD = "ca-app-pub-3940256099942544/1033173712"
    private val ID_NATIVE_AD = "ca-app-pub-3940256099942544/2247696110"

    private var mInterstitialAd: InterstitialAd? = null
    private val NUM_SHOW_ADS = 5
    private var numClicked = 5

    init {
        MobileAds.initialize(context)
        initInterstitial()
    }

    fun loadBanner(banner: FrameLayout) {
        val adView = AdView(context)
        adView.adSize = AdSize.BANNER
        adView.adUnitId = ID_BANNER_AD
        val adRequest: AdRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                banner.addView(adView)
            }

            override fun onAdClicked() {}
        }
    }

    private fun initInterstitial() {
        mInterstitialAd = InterstitialAd(context)
        mInterstitialAd!!.adUnitId = ID_INTERSTITIAL_AD
        mInterstitialAd!!.loadAd(AdRequest.Builder().build())
        mInterstitialAd!!.adListener = object : AdListener() {
            override fun onAdLoaded() {}
            override fun onAdFailedToLoad(errorCode: Int) {}
            override fun onAdOpened() {}
            override fun onAdClicked() {}
            override fun onAdLeftApplication() {}
            override fun onAdClosed() {
                initInterstitial()
            }
        }
    }

    fun loadInterialAds() {
        numClicked++
        if (isLoaded()
            && numClicked > NUM_SHOW_ADS
        ) {
            numClicked = 1
            mInterstitialAd!!.show()
        }
    }

    private fun isLoaded(): Boolean {
        return mInterstitialAd!!.isLoaded
    }

    @SuppressLint("InflateParams")
    fun loadNativeAds(
        adView: UnifiedNativeAdView
    ) {
        val builder = AdLoader.Builder(context, ID_NATIVE_AD)
            .forUnifiedNativeAd { unifiedNativeAd ->
                populateUnifiedNativeAdView(unifiedNativeAd, adView)
            }
        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .build()
        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .build()
        builder.withNativeAdOptions(adOptions)
        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {}
        }).build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateUnifiedNativeAdView(
        unifiedNativeAd: UnifiedNativeAd,
        adView: UnifiedNativeAdView
    ) {
        val mediaView: MediaView = adView.findViewById(R.id.ad_media)
        adView.mediaView = mediaView
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        (adView.headlineView as TextView).text = unifiedNativeAd.headline
        when (unifiedNativeAd.callToAction) {
            null -> {
                adView.callToActionView.visibility = View.GONE
            }
            else -> {
                adView.callToActionView.visibility = View.VISIBLE
                (adView.callToActionView as Button).text = unifiedNativeAd.callToAction
            }
        }
        when (unifiedNativeAd.icon) {
            null -> {
                adView.iconView.visibility = View.GONE
            }
            else -> {
                (adView.iconView as ImageView).setImageDrawable(
                    unifiedNativeAd.icon.drawable
                )
                adView.iconView.visibility = View.VISIBLE
            }
        }
        adView.setNativeAd(unifiedNativeAd)
        val vc = unifiedNativeAd.videoController
        when {
            vc.hasVideoContent() -> {
                vc.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                }
            }
        }
    }
}