package com.aylar.chatty.data.repository

import com.aylar.chatty.data.remote.UserService
import com.aylar.chatty.domain.model.Avatars
import com.aylar.chatty.domain.model.GetCurrentUserProfile
import com.aylar.chatty.domain.model.UserUpdate
import com.aylar.chatty.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserRepository {

    override suspend fun getCurrentUser(): Result<GetCurrentUserProfile> {
        return try {
            val response = userService.getCurrentUser()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("User not found"))
            } else {
                Result.failure(Exception("Failed to fetch user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(userUpdate: UserUpdate): Result<Avatars> {
        return try {
            val response = userService.updateUser(userUpdate)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Failed to update user"))
            } else {
                Result.failure(Exception("Failed to update user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}