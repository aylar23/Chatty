package com.aylar.chatty.data.remote

import com.aylar.chatty.domain.model.CheckAuthCode
import com.aylar.chatty.domain.model.LoginOut
import com.aylar.chatty.domain.model.PhoneBase
import com.aylar.chatty.domain.model.RefreshToken
import com.aylar.chatty.domain.model.RegisterIn
import com.aylar.chatty.domain.model.Success
import com.aylar.chatty.domain.model.Token
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthService {

    @POST("/api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(@Body phone: PhoneBase): Response<Success>

    @POST("/api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body checkAuthCode: CheckAuthCode): Response<LoginOut>

    @POST("/api/v1/users/register/")
    suspend fun register(@Body registerIn: RegisterIn): Response<Token>

    @POST("/api/v1/users/refresh-token/")
    fun refreshToken(@Body refreshToken: RefreshToken): Call<Token>
}