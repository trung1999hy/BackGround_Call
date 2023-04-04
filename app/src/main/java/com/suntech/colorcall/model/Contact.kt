package com.suntech.colorcall.model

import android.os.Parcelable
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import kotlinx.android.parcel.Parcelize

open class BaseItem

@Entity
@Parcelize
data class Contact(
    @Id(assignable = true) var id: Long = 0,
    val name: String,
    val phoneNumber: String,
    var avatar: String?,
    var video: String? = null,
    var image: String? = null,
    var checked: Boolean = false
) : BaseItem(),
    Parcelable

data class AdsItem(val id: Long = 1) : BaseItem()
