package com.aylar.chatty.domain.repository

import com.aylar.chatty.domain.model.Avatars
import com.aylar.chatty.domain.model.GetCurrentUserProfile
import com.aylar.chatty.domain.model.UserUpdate

interface UserRepository {

    suspend fun getCurrentUser(): Result<GetCurrentUserProfile>

    suspend fun updateUser(userUpdate: UserUpdate): Result<Avatars>

}