package com.suntech.colorcall.api

import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiServices: ApiServices) {
    suspend fun getStyle() = apiServices.getStyle()
}