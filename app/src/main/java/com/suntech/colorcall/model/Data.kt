package com.suntech.colorcall.model

data class Data(
    val conf: List<Conf>,
    val desc: String,
    val icon: String,
    val id: Long = 0,
    val index: String,
    val name: String
)