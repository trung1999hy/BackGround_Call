package com.suntech.colorcall.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Style(
    @Id
    var id: Long = 0,
    val background: String,
    val downloaded: Boolean
)