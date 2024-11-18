package com.aylar.chatty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.aylar.chatty.data.utils.SharedPreferenceHelper
import com.aylar.chatty.ui.navigation.ChattyNavGraph
import com.aylar.chatty.ui.theme.ChattyTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferenceHelper: SharedPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ChattyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    ChattyNavGraph(isLoggedIn = isLoggedIn())
                }
            }
        }
    }

    private fun getAccessToken(): String? {
        return sharedPreferenceHelper.getString(SharedPreferenceHelper.ACCESS_TOKEN)
    }

    private fun isLoggedIn(): Boolean = !getAccessToken().isNullOrBlank()
}
