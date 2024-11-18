package com.aylar.chatty.ui.screens.editprofile

data class EditProfileState(
    val phone: String = "",
    val name: String = "",
    val username: String = "",
    val city: String = "",
    val birthday: String = "",
    val avatar: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    fun updateToLoading(): EditProfileState {
        return copy(isLoading = true, errorMessage = null)
    }
    fun updateToDefault(): EditProfileState {
        return copy(isLoading = false, errorMessage = null)
    }
    fun updateName(name: String): EditProfileState {
        return copy(name = name)
    }
    fun updateCity(city: String): EditProfileState {
        return copy(city = city)
    }
    fun updateUsername(username: String): EditProfileState {
        return copy(username = username)
    }
    fun updatePhone(phone: String): EditProfileState {
        return copy(phone = phone)
    }
    fun updateAvatar(avatar: String): EditProfileState {
        return copy(avatar = avatar)
    }
    fun updateBirthday(birthday: String): EditProfileState {
        return copy(birthday = birthday)
    }
    fun updateToFailure(errorMessage: String): EditProfileState {
        return copy(errorMessage = errorMessage, isLoading = false)
    }
}