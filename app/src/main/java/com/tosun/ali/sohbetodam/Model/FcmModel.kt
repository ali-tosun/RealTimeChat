package com.tosun.ali.sohbetodam.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

abstract class FcmModel {

    @Expose
    @SerializedName("data")
    var data: Data? = null
    @Expose
    @SerializedName("to")
    var to: String? = null

    class Data {
        @Expose
        @SerializedName("bildirim_turu")
        var bildirim_turu: String? = null
        @Expose
        @SerializedName("baslik")
        var baslik: String? = null
        @Expose
        @SerializedName("icerik")
        var İcerik: String? = null
    }
}
