package com.aylar.chatty.data.remote

import com.aylar.chatty.domain.model.Avatars
import com.aylar.chatty.domain.model.GetCurrentUserProfile
import com.aylar.chatty.domain.model.UserUpdate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserService {

    @GET("/api/v1/users/me/")
    suspend fun getCurrentUser(): Response<GetCurrentUserProfile>

    @PUT("/api/v1/users/me/")
    suspend fun updateUser(@Body user: UserUpdate): Response<Avatars>

}