package com.suntech.colorcall.helper

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import javax.inject.Inject

@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CommonHelper @Inject constructor(val context: Context) {
    private val publishName: String = "Suntech Ltd."
    private val email: String = "suntectltd.software@gmail.com"
    private val subject: String = "FeedBack"

    fun shareApp() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody =
            "https://play.google.com/store/apps/details?id=" + context.packageName.trim { it <= ' ' }
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        context.startActivity(Intent.createChooser(sharingIntent, "Share to").apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK })
    }

/*    fun support() {
        val mailIntent = Intent(Intent.ACTION_VIEW)
        val data =
            Uri.parse("mailto:?SUBJECT=$SUBJECT&body=&to=$EMAIL")
        mailIntent.data = data
        context.startActivity(Intent.createChooser(mailIntent, "Send mail..."))
    }*/

    fun rateApp() {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + context.packageName)
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + context.packageName)
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            )
        }
    }

    fun moreApp() {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://search?q=pub:$publishName")
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/developer?id=$publishName")
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            )
        }
    }
}