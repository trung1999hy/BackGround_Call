package com.suntech.colorcall.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Material(
    val big_image_url: String,
    val desc: String,
    val is_outside: String,
    val item_name: String,
    val out_big_image: String,
    val small_image_url: String,
    val video_url: String
) : Parcelable