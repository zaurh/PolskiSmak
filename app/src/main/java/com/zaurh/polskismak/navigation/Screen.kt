package com.zaurh.polskismak.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen(route = "welcome")

    object Main : Screen(route = "main")
    object Favorite : Screen(route = "favorite_screen")
    object Settings : Screen(route = "settings_screen")
    object Detail : Screen("detail_screen/{id}")

    fun createRoute(id: Int? = null): String {
        return when (this) {
            is Detail -> route.replace("{id}", id.toString())
            else -> route
        }
    }
}