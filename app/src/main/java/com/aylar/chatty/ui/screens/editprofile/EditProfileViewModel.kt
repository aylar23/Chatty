package com.aylar.chatty.ui.screens.editprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylar.chatty.data.utils.SharedPreferenceHelper
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.AVATAR
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.BIRTHDAY
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.CITY
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.NAME
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.PHONE
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.USERNAME
import com.aylar.chatty.domain.model.HTTPValidationErrorMessage
import com.aylar.chatty.domain.model.UploadImage
import com.aylar.chatty.domain.model.UserUpdate
import com.aylar.chatty.domain.repository.UserRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : ViewModel() {

    private val _uiState: MutableStateFlow<EditProfileState> = MutableStateFlow(EditProfileState())
    val uiState: StateFlow<EditProfileState> = _uiState

    init {
        getProfileData()
    }
    fun updateUser(
        username: String,
        name: String,
        birthday: String,
        city: String,
        avatar: UploadImage?,
        onSuccess: () -> Unit,
    ) {
        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                userRepository.updateUser(
                    UserUpdate(
                        username = username,
                        name = name,
                        birthday = birthday,
                        avatar = avatar,
                        city = city
                    )
                ).onSuccess { res ->
                    saveProfileData(
                        name = name,
                        birthday = birthday,
                        city = city,
                        avatar = res.avatar,
                    )
                    onSuccess()
                }.onFailure { t ->
                    Log.e("TAG", "updateUser: "+t.message )
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

    fun updateErrorMessage(msg: String) {
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

    fun updateBirthday(birthday: String) {
        _uiState.update { it.updateBirthday(birthday) }
    }

    fun updateCity(city: String) {
        _uiState.update { it.updateCity(city) }
    }

    fun updateAvatar(avatar: String) {
        _uiState.update { it.updateAvatar(avatar) }
    }

    fun getErrorMessage(t: Throwable): String {
        val gson = Gson()
        val errorDetail = gson.fromJson(t.message, HTTPValidationErrorMessage::class.java)
        return errorDetail.detail.message
    }

    private fun saveProfileData(
        name: String,
        birthday: String,
        city: String,
        avatar: String
    ) {
        sharedPreferenceHelper.put(NAME, name)
        sharedPreferenceHelper.put(AVATAR, avatar)
        sharedPreferenceHelper.put(BIRTHDAY, birthday)
        sharedPreferenceHelper.put(CITY, city)
    }

    private fun getProfileData() {
        updatePhone(sharedPreferenceHelper.getString(PHONE)?: "")
        updateUsername(sharedPreferenceHelper.getString(USERNAME)?:"")
        updateName(sharedPreferenceHelper.getString(NAME)?: "")
        updateAvatar(sharedPreferenceHelper.getString(AVATAR) ?: "")
        updateBirthday(sharedPreferenceHelper.getString(BIRTHDAY) ?: "")
        updateCity(sharedPreferenceHelper.getString(CITY)?: "")
    }
}
