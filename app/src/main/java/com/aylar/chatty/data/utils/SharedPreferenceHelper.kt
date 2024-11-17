package com.aylar.chatty.data.utils

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


const val USER_SHARED_PREFERENCE = "com.aylar.chatty.sharedpreference_user_shared_preference"

@Singleton
class SharedPreferenceHelper @Inject constructor(@ApplicationContext context : Context) {


    val sharedPreferences = context.getSharedPreferences(USER_SHARED_PREFERENCE, Context.MODE_PRIVATE)


    companion object {
        const val ACCESS_TOKEN =
            "access_token"

        const val REFRESH_TOKEN =
            "refresh_token"

        const val USER_ID =
            "user_id"
    }

    fun getBoolean(key: String): Boolean = sharedPreferences.getBoolean(key, false)

    fun getLong(key: String): Long = sharedPreferences.getLong(key, 0L)

    fun getInt(key: String): Int = sharedPreferences.getInt(key, 0)

    fun getString(key: String): String? = sharedPreferences.getString(key, null)

    fun put(key: String, value: Int) {
        sharedPreferences.edit { putInt(key, value) }
    }

    fun put(key: String, value: String?) {
        sharedPreferences.edit { putString(key, value) }
    }

    fun put(key: String, value: Long) {
        sharedPreferences.edit { putLong(key, value ?: 0L) }
    }

    fun put(key: String, value: Boolean) {
        sharedPreferences.edit { putBoolean(key, value) }
    }

    val getInstance = SharedPreferenceHelper

}
