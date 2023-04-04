package com.suntech.colorcall.api

import com.suntech.colorcall.model.DataResponse
import retrofit2.Response
import retrofit2.http.GET

const val BASE_URL = "http://s1.picsjoin.com"
const val PATH = "Material_library/public/V1/ColorPhone/getGroupFlashVideo?statue=2&&music=1"

interface ApiServices {
    @GET(PATH)
    suspend fun getStyle(): Response<DataResponse>
}