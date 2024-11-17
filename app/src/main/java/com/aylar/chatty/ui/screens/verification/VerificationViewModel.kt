package com.aylar.chatty.ui.screens.verification

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylar.chatty.R
import com.aylar.chatty.data.utils.SharedPreferenceHelper
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.ACCESS_TOKEN
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.REFRESH_TOKEN
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.USER_ID
import com.aylar.chatty.domain.model.HTTPValidationErrorMessage
import com.aylar.chatty.domain.model.LoginOut
import com.aylar.chatty.domain.repository.AuthRepository
import com.aylar.chatty.ui.utils.getFormattedCountTimeShort
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : ViewModel() {

    var timer = createTimer()
    var timeLeftToResend by mutableStateOf("00:00")
    var resendAllowed by mutableStateOf(false)

    private val _uiState: MutableStateFlow<VerificationState> =
        MutableStateFlow(VerificationState())
    val uiState: StateFlow<VerificationState> = _uiState

    init {
        startTimer()
    }

    fun resendCode(phoneNumber: String) {
        viewModelScope.launch {
            _uiState.update { it.updateToLoading() }
            try {
                authRepository.sendAuthCode(phoneNumber)
                    .onSuccess { updateToDefault();  startTimer() }
                    .onFailure {
                        _uiState.update { it.updateToFailure("Invalid phone number or country code") }
                    }
            } catch (e: Exception) {
                _uiState.update { it.updateToFailure("Network error") }
            }
        }
    }

    fun checkAuthCode(phone: String, code: String,  onSuccess: (Boolean) -> Unit, onFailure: () -> Unit) {
        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                 authRepository.checkAuthCode(phone, code)
                    .onSuccess { res ->
                        if (res.isUserExists){
                            saveToken(res)
                        }
                        onSuccess(res.isUserExists)
                    }
                    .onFailure {t ->
                        onFailure()
                    }
                updateToDefault()

            } catch (throwable: Throwable) {
                _uiState.update { it.updateToFailure("Network error") }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelTimer()
    }

    private fun startTimer() {
        resendAllowed = false
        timer.start()
    }

    private fun cancelTimer() {
        timer.cancel()
        resendAllowed = true
    }

    private fun createTimer(): CountDownTimer {
        val time = Calendar.getInstance()
        time.add(Calendar.SECOND, TIME)
        val timeInMillis = time.timeInMillis
        val timer =
            object : CountDownTimer(timeInMillis - Calendar.getInstance().timeInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeftToResend = getFormattedCountTimeShort(millisUntilFinished)
                }

                override fun onFinish() {
                    resendAllowed = true
                }

            }
        return timer
    }

    fun updateToDefault() {
        _uiState.update { it.updateToDefault() }
    }
    fun updateCode(code: String) {
        _uiState.update { it.updateCode(code) }
    }
    fun updatePhone(phone: String) {
        _uiState.update { it.updatePhone(phone) }
    }
    fun getErrorMessage(t:Throwable):String {
        val gson = Gson()
        val errorDetail = gson.fromJson(t.message, HTTPValidationErrorMessage::class.java)
        return errorDetail.detail.message
    }
    fun saveToken(res: LoginOut){
        with(sharedPreferenceHelper.sharedPreferences.edit()) {
            putString(ACCESS_TOKEN, res.accessToken)
            putString(REFRESH_TOKEN, res.refreshToken)
            putInt(USER_ID, res.userId)
            apply()
        }
    }

    companion object {
        const val TIME = 60
    }
}
