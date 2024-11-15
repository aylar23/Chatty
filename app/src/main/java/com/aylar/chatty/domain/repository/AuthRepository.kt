package com.aylar.chatty.domain.repository

import com.aylar.chatty.domain.model.LoginOut
import com.aylar.chatty.domain.model.Success
import com.aylar.chatty.domain.model.Token


interface AuthRepository {

    suspend fun sendAuthCode(phone: String): Result<Success>

    suspend fun checkAuthCode(phone: String, code: String): Result<LoginOut>

    suspend fun register(phone: String, name: String, username: String): Result<Token>

}
