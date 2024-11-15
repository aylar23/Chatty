package com.aylar.chatty.domain.model

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("user_id")
    val userId: Int
)