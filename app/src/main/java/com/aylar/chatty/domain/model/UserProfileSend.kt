package com.aylar.chatty.domain.model

import com.aylar.chatty.di.AppModule.Companion.BASE_URL
import com.google.gson.annotations.SerializedName

data class UserProfileSend(
    val name: String,
    val username: String,
    val birthday: String?,
    val city: String?,
    val vk: String?,
    val instagram: String?,
    val status: String?,
    val avatar: String?,
    val id: Int,
    val last: String?,
    val online: Boolean,
    val created: String?,
    val phone: String,
    @SerializedName("completed_task")
    val completedTask: Int = 0,
    val avatars: Avatars?
){
    fun getUserAvatar(): String {
        return avatar?.let { "$BASE_URL$avatar" } ?: ""
    }
}