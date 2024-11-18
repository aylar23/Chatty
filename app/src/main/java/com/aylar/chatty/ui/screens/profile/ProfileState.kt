package com.aylar.chatty.ui.screens.profile

import com.aylar.chatty.domain.model.UserProfileSend


data class ProfileState(
    val isLoading: Boolean = false,
    val isFailure: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: Int? = null,
    val profileData: UserProfileSend? = null
){
    fun updateToLoading(): ProfileState {
        return copy(isLoading = true, errorMessage = null, isSuccess = false)
    }
    fun updateToDefault(): ProfileState {
        return copy(isLoading = false, errorMessage = null, isFailure = false)
    }
    fun updateToSuccess(profileData: UserProfileSend): ProfileState {
        return copy(isLoading = false, errorMessage = null, isFailure = false, isSuccess = true, profileData = profileData)
    }
    fun updateToFailure(errorMessage:Int): ProfileState {
        return copy(errorMessage = errorMessage, isLoading = false, isFailure = true, isSuccess = false)
    }
}