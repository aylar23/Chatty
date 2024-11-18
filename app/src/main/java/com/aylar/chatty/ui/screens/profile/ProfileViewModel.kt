package com.aylar.chatty.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylar.chatty.R
import com.aylar.chatty.data.utils.SharedPreferenceHelper
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.AVATAR
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.BIRTHDAY
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.CITY
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.NAME
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.PHONE
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.USERNAME
import com.aylar.chatty.domain.model.UserProfileSend
import com.aylar.chatty.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : ViewModel() {

    private val _uiState: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = this._uiState

    init {
        getProfile()
    }

    fun getProfile() {
        viewModelScope.launch {
            _uiState.update { it.updateToLoading() }
            try {
                userRepository.getCurrentUser()
                    .onSuccess { res ->
                        Log.e("TAG", "getProfile: "+res )
                        saveProfileData(res.profileData)
                        _uiState.update { it.updateToSuccess(res.profileData) }
                    }
                    .onFailure {
                        _uiState.update { it.updateToFailure(R.string.network_error) }
                    }
            } catch (e: Exception) {
                _uiState.update { it.updateToFailure(R.string.network_error) }
            }
        }
    }

    fun updateToDefault() {
        _uiState.update { it.updateToDefault() }
    }

    private fun saveProfileData(profileData:UserProfileSend) {
        sharedPreferenceHelper.put(PHONE, profileData.phone)
        sharedPreferenceHelper.put(USERNAME, profileData.username)
        sharedPreferenceHelper.put(NAME, profileData.name)
        sharedPreferenceHelper.put(AVATAR, profileData.getUserAvatar())
        sharedPreferenceHelper.put(BIRTHDAY,profileData.birthday )
        sharedPreferenceHelper.put(CITY, profileData.city)
    }

}