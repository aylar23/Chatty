package com.aylar.chatty.domain.model

import com.google.gson.annotations.SerializedName

data class UploadImage(
    val filename: String,
    @SerializedName("base_64")
    val base64: String
)