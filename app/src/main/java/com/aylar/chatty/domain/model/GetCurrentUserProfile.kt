package com.aylar.chatty.domain.model

import com.google.gson.annotations.SerializedName

data class GetCurrentUserProfile(
    @SerializedName("profile_data")
    val profileData: UserProfileSend
)