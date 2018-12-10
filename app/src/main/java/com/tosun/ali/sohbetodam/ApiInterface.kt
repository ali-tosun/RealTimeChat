package com.tosun.ali.sohbetodam

import com.google.android.gms.common.api.Response
import com.tosun.ali.sohbetodam.Model.FcmModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface ApiInterface {


    @POST("send")
    fun bildirimleriGonder(
            @HeaderMap Headers: Map<String, String>,
            @Body bilgiler: FcmModel


    ): Call<retrofit2.Response<FcmModel>>

}