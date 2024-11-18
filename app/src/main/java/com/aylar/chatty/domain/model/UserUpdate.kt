package com.aylar.chatty.domain.model

data class UserUpdate(
    val name: String,
    val username: String? = null,
    val birthday: String? = null,
    val city: String? = null,
    val vk: String? = null,
    val instagram: String? = null,
    val status: String? = null,
    val avatar: UploadImage? = null
)