package com.aylar.chatty.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aylar.chatty.ui.screens.login.LoginScreen


@Composable
fun ChattyNavGraph(
    isLoggedIn:Boolean,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    NavHost(
        navController,
        startDestination = if (isLoggedIn) Screen.Dialogs.route else Screen.Login.route
    ) {

        composable(Screen.Login.route) {
            LoginScreen(
                loginViewModel = hiltViewModel(),
                onContinue = {
                    navController.navigate("${Screen.Verification.route}")
                }
            )
        }

        composable(Screen.Verification.route) {}

        composable(Screen.Registration.route) {}

        composable(Screen.Dialogs.route) {}

        composable(Screen.Chat.route) {}

        composable(Screen.Profile.route) {}

        composable(Screen.EditProfile.route) {}

    }
}


