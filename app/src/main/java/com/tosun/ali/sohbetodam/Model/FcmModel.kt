package com.tosun.ali.sohbetodam.Model

import com.google.gson.annotations.SerializedName

class FcmModel {

    @SerializedName("to")
    var to: String? = null
    @SerializedName("data")
    var data: Data? = null

    class Data {
        @SerializedName("baslik")
        var baslik: String? = null
        @SerializedName("icerik")
        var icerik: String? = null
        @SerializedName("bildirim_turu")
        var bildirim_turu: String? = null
    }
}
