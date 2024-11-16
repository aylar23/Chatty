package com.aylar.chatty.ui.screens.login

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylar.chatty.R
import com.aylar.chatty.domain.repository.AuthRepository
import com.aylar.chatty.ui.components.CountryPhoneCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = this._uiState

    fun login(phoneNumber: String, onSuccess: () -> Unit) {
        if (phoneNumber.isBlank()) {
            return
        }
        viewModelScope.launch {
            _uiState.update { it.updateToLoading() }
            try {
               authRepository.sendAuthCode(phoneNumber)
                   .onSuccess { onSuccess() }
                   .onFailure {
                       _uiState.update { it.updateToFailure(R.string.invalid_phone_number_or_country_code) }
                   }
            } catch (e: Exception) {
                _uiState.update { it.updateToFailure(R.string.invalid_phone_number_or_country_code) }
            }
        }
    }

    fun updateCode(code: String) {
        _uiState.update { it.updateCode(code) }
    }
    fun updatePhone(phone: String) {
        _uiState.update { it.updatePhone(phone) }
    }
    fun updateCountry(country: CountryPhoneCode?) {
        _uiState.update { it.updateCountry(country) }
    }

    fun applyPhoneMask(phoneNumber: String, format: String): String {
        val digits = phoneNumber.filter { it.isDigit() }
        val formatted = StringBuilder()
        var digitIndex = 0

        for (char in format) {
            if (digitIndex >= digits.length) break
            if (char == 'X') {
                formatted.append(digits[digitIndex])
                digitIndex++
            } else {
                formatted.append(char)
            }
        }
        return formatted.toString()
    }
}