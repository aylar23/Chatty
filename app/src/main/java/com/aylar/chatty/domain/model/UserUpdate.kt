package com.aylar.chatty.domain.model

data class UserUpdate(
    val name: String,
    val username: String,
    val birthday: String?,
    val city: String?,
    val vk: String?,
    val instagram: String?,
    val status: String?,
    val avatar: UploadImage
)