package com.aylar.chatty.data.utils

import android.util.Log
import com.aylar.chatty.data.remote.AuthService
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.ACCESS_TOKEN
import com.aylar.chatty.data.utils.SharedPreferenceHelper.Companion.REFRESH_TOKEN
import com.aylar.chatty.domain.model.RefreshToken
import com.aylar.chatty.domain.model.Token
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject


class SynchronizedAuthenticator @Inject constructor(
    private val authService: AuthService,
    private val sharedPreferenceHelper: SharedPreferenceHelper,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authenticatedReq = originalRequest.signedRequest()
        val initResponse = chain.proceed(authenticatedReq)
        if (initResponse.code != 401) return initResponse

        synchronized(this) {
            val refreshToken = refreshTokenWithCode() ?: return initResponse

            val newAuthReq = originalRequest
                .newBuilder()
                .addHeader("Authorization", "Bearer $refreshToken")
                .build()
            initResponse.close()
            return chain.proceed(newAuthReq)
        }

    }

    private fun Request.signedRequest(): Request {
        val accessToken = getAccessToken()
        return if (accessToken != null)
            newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        else
            newBuilder().build()
    }

    private fun getAccessToken(): String? {
        return sharedPreferenceHelper.getString(ACCESS_TOKEN)
    }

    private fun refreshTokenWithCode(): String? {
        val refreshToken = sharedPreferenceHelper.getString(REFRESH_TOKEN) ?: return null

        return try {
            val requestBody = RefreshToken(refreshToken)
            val response = authService.refreshToken(requestBody).execute()

            when {
                response.isSuccessful -> {
                    response.body()?.let { tokens ->
                        updateToken(tokens)
                        tokens.accessToken
                    }
                }
                response.code() in 401..403 -> {
                    sharedPreferenceHelper.sharedPreferences.edit().clear().apply()
                    null
                }
                else -> null
            }
        } catch (e: Exception) {
            Log.e("SynchronizedAuthenticator", "error: ${e.message}")
            null
        }
    }

    private fun updateToken(token: Token){
        with(sharedPreferenceHelper.sharedPreferences.edit()) {
            putString(ACCESS_TOKEN, token.accessToken)
            putString(REFRESH_TOKEN, token.refreshToken)
            commit()
        }
    }


}