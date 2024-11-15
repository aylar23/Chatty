package com.aylar.chatty.domain.model

import com.google.gson.annotations.SerializedName

data class Success(
    @SerializedName("is_success")
    val isSuccess: Boolean
)