package com.aylar.chatty.ui.screens.verification

import androidx.annotation.StringRes


data class VerificationState(
    val code: String = "",
    val phone: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    fun updateToLoading(): VerificationState {
        return copy(isLoading = true, errorMessage = null)
    }

    fun updateToDefault(): VerificationState {
        return copy(isLoading = false, errorMessage = null)
    }

    fun updateCode(code: String): VerificationState {
        return copy(code = code)
    }

    fun updatePhone(phone: String): VerificationState {
        return copy(phone = phone)
    }

    fun updateToFailure(errorMessage: String): VerificationState {
        return copy(errorMessage = errorMessage, isLoading = false)
    }
}