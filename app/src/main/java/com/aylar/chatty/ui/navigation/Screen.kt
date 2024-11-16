package com.aylar.chatty.ui.navigation



sealed class Screen(
    val route: String
) {

    object Login : Screen(
        "login"
    )

    object Verification : Screen(
        "code_verification"
    )

    object Registration : Screen(
        "code_verification"
    )

    object Dialogs : Screen(
        "dialogs"
    )

    object Chat : Screen(
        "chat"
    )

    object Profile : Screen(
        "profile"
    )

    object EditProfile : Screen(
        "edit_profile"
    )

}