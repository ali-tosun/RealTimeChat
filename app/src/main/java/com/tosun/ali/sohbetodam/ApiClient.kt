package com.tosun.ali.sohbetodam

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    var base_Url = "https://fcm.googleapis.com/fcm/"

    var retrofit:Retrofit? = null

    val client:Retrofit?
    get() {

        if(retrofit == null){

            retrofit = Retrofit.Builder()
                    .baseUrl(base_Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return  retrofit


        }

        return retrofit
    }


}