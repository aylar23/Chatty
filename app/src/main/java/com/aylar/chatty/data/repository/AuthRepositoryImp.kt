package com.aylar.chatty.data.repository

import com.aylar.chatty.data.remote.AuthService
import com.aylar.chatty.domain.model.CheckAuthCode
import com.aylar.chatty.domain.model.LoginOut
import com.aylar.chatty.domain.model.PhoneBase
import com.aylar.chatty.domain.model.RegisterIn
import com.aylar.chatty.domain.model.Success
import com.aylar.chatty.domain.model.Token
import com.aylar.chatty.domain.repository.AuthRepository
import javax.inject.Inject


class AuthRepositoryImp @Inject constructor(
    private val authService: AuthService
) : AuthRepository {

    override suspend fun sendAuthCode(phone: String): Result<Success> {
        return try {
            val response = authService.sendAuthCode(PhoneBase(phone))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkAuthCode(phone: String, code: String): Result<LoginOut> {
        return try {
            val response = authService.checkAuthCode(CheckAuthCode(phone, code))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(phone: String, name: String, username: String): Result<Token> {
        return try {
            val response = authService.register(RegisterIn(phone, name, username))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}