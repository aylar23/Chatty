package com.aylar.chatty.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylar.chatty.data.utils.SharedPreferenceHelper
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.ACCESS_TOKEN
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.REFRESH_TOKEN
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.USER_ID
import com.aylar.chatty.domain.model.HTTPValidationErrorMessage
import com.aylar.chatty.domain.model.Token
import com.aylar.chatty.domain.repository.AuthRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : ViewModel() {

    private val _uiState: MutableStateFlow<RegistrationState> =
        MutableStateFlow(RegistrationState())
    val uiState: StateFlow<RegistrationState> = _uiState

    fun register(
        phone: String,
        name: String,
        username: String,
        onSuccess: () -> Unit,
    ) {
        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                authRepository.register(phone, name, username)
                    .onSuccess { res ->
                        saveToken(res)
                        onSuccess()
                    }
                    .onFailure { t ->
                        _uiState.update { it.updateToFailure(getErrorMessage(t)) }
                    }
                updateToDefault()

            } catch (throwable: Throwable) {
                _uiState.update { it.updateToFailure("Network error") }
            }
        }
    }

    fun updateToDefault() {
        _uiState.update { it.updateToDefault() }
    }

    fun updateErrorMessage(msg:String) {
        _uiState.update { it.updateToFailure(msg) }
    }

    fun updateName(name: String) {
        _uiState.update { it.updateName(name) }
    }

    fun updateUsername(username: String) {
        _uiState.update { it.updateUsername(username) }
    }

    fun updatePhone(phone: String) {
        _uiState.update { it.updatePhone(phone) }
    }

    fun getErrorMessage(t: Throwable): String {
        val gson = Gson()
        val errorDetail = gson.fromJson(t.message, HTTPValidationErrorMessage::class.java)
        return errorDetail.detail.message
    }

    fun saveToken(res: Token) {
        with(sharedPreferenceHelper.sharedPreferences.edit()) {
            putString(ACCESS_TOKEN, res.accessToken)
            putString(REFRESH_TOKEN, res.refreshToken)
            putInt(USER_ID, res.userId)
            apply()
        }
    }
}
