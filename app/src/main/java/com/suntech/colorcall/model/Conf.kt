package com.suntech.colorcall.model

data class Conf(
    val g_id: String,
    val index: String,
    val is_hot: String,
    val is_lock: String,
    val is_new: String,
    val is_rec: String,
    val is_voice: String,
    val like: String,
    val material: Material,
    val uniqid: String,
    var lock : Boolean = false
)