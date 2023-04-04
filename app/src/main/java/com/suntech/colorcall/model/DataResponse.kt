package com.suntech.colorcall.model

data class DataResponse(
    var id: Long,
    val `data`: List<Data>,
    val msg: String,
    val server_time: Int,
    val status: Int
)




