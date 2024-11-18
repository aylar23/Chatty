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
import com.aylar.chatty.ui.screens.chat.ChatScreen
import com.aylar.chatty.ui.screens.dialogs.DialogsScreen
import com.aylar.chatty.ui.screens.login.LoginScreen
import com.aylar.chatty.ui.screens.profile.ProfileScreen
import com.aylar.chatty.ui.screens.register.RegistrationScreen
import com.aylar.chatty.ui.screens.verification.CodeVerificationScreen


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
                    navController.navigate("${Screen.Verification.route}/$it")
                }
            )
        }

        composable(
            route = "${Screen.Verification.route}/{phone}", arguments = listOf(
                navArgument("phone") { type = NavType.StringType },
            )
        ) { entry ->
            entry.arguments?.getString("phone")?.let { phone ->
                CodeVerificationScreen(
                    verifyViewModel = hiltViewModel(),
                    phoneNumber = phone,
                    logIn = {
                        navController.navigate(Screen.Dialogs.route)
                    },
                    register = { phone ->
                        navController.navigate("${Screen.Registration.route}/$phone")
                    }
                )
            }
        }

        composable(
            route = "${Screen.Registration.route}/{phone}", arguments = listOf(
                navArgument("phone") { type = NavType.StringType },
            )
        ) { entry ->
            entry.arguments?.getString("phone")?.let { phone ->
                RegistrationScreen(
                    phone = phone,
                    registrationViewModel = hiltViewModel(),
                    onSuccess = {
                        navController.navigate(Screen.Dialogs.route)
                    }
                )
            }
        }

        composable(Screen.Dialogs.route) {
            DialogsScreen(
                navigateToChat = {navController.navigate(Screen.Chat.route)},
                navigateToProfile = {navController.navigate(Screen.Profile.route)}
            )
        }

        composable(Screen.Chat.route) {
            ChatScreen(onNavigateUp = { navController.navigateUp() })
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                profileViewModel = hiltViewModel(),
                onNavigateUp = { navController.navigateUp()}
            )
        }

        composable(Screen.EditProfile.route) {

        }

    }
}


