package com.aylar.chatty.ui.screens.login

import com.aylar.chatty.ui.components.CountryPhoneCode


data class LoginState(
    val code: String = "",
    val phone: String = "",
    val country: CountryPhoneCode? = null,
    val isLoading: Boolean = false,
    val errorMessage: Int? = null
){
    fun updateToLoading(): LoginState {
        return copy(isLoading = true, errorMessage = null)
    }
    fun updateToDefault(): LoginState {
        return copy(isLoading = false, errorMessage = null)
    }
    fun updateCode(code: String): LoginState {
        return copy(code = code)
    }
    fun updatePhone(phone: String): LoginState {
        return copy(phone = phone)
    }
    fun updateCountry(country: CountryPhoneCode?): LoginState {
        return copy(country = country)
    }
    fun updateToFailure(errorMessage:Int): LoginState {
        return copy(errorMessage = errorMessage, isLoading = false)
    }
}