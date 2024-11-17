package com.aylar.chatty.ui.screens.register


data class RegistrationState(
    val phone: String = "",
    val name: String = "",
    val username: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    fun updateToLoading(): RegistrationState {
        return copy(isLoading = true, errorMessage = null)
    }
    fun updateToDefault(): RegistrationState {
        return copy(isLoading = false, errorMessage = null)
    }
    fun updateName(name: String): RegistrationState {
        return copy(name = name)
    }
    fun updateUsername(username: String): RegistrationState {
        return copy(username = username)
    }
    fun updatePhone(phone: String): RegistrationState {
        return copy(phone = phone)
    }
    fun updateToFailure(errorMessage: String): RegistrationState {
        return copy(errorMessage = errorMessage, isLoading = false)
    }
}