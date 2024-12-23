package com.aylar.chatty.ui.screens.login

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
        viewModelScope.launch {
            _uiState.update { it.updateToLoading() }
            try {
                authRepository.sendAuthCode(phoneNumber)
                    .onSuccess { updateToDefault(); onSuccess() }
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

    fun updateToDefault() {
        _uiState.update { it.updateToDefault() }
    }
}