package com.aylar.chatty.domain.model

import com.google.gson.annotations.SerializedName

data class RefreshToken(
    @SerializedName("refresh_token")
    val refreshToken: String
)