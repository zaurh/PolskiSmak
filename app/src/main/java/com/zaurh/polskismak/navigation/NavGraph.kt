package com.zaurh.polskismak.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.zaurh.polskismak.presentation.screen.DetailScreen
import com.zaurh.polskismak.presentation.viewmodel.DetailViewModel
import com.zaurh.polskismak.presentation.screen.FavoriteScreen
import com.zaurh.polskismak.presentation.viewmodel.FavoriteViewModel
import com.zaurh.polskismak.presentation.screen.MainScreen
import com.zaurh.polskismak.presentation.viewmodel.MainViewModel
import com.zaurh.polskismak.presentation.screen.OnBoardingScreen
import com.zaurh.polskismak.presentation.screen.SettingsScreen

@Composable
fun Navig(
    navController: NavHostController,
    startDestination: String,
    darkTheme: Boolean,
    language: Boolean,
    onThemeUpdated: () -> Unit
) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val detailViewModel = hiltViewModel<DetailViewModel>()
    val favoriteViewModel = hiltViewModel<FavoriteViewModel>()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Main.route) {
            MainScreen(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }
        composable(
            Screen.Detail.route, arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val id = arguments.getInt("id")
            DetailScreen(
                detailViewModel = detailViewModel,
                favoriteViewModel = favoriteViewModel,
                id = id,
                navController = navController
            )
        }
        composable(Screen.Favorite.route) {
            FavoriteScreen(
                navController = navController,
                favoriteViewModel = favoriteViewModel
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                language = language,
                darkTheme = darkTheme,
                onThemeUpdated = onThemeUpdated
            )
        }
        composable(Screen.Welcome.route) {
            OnBoardingScreen(
                navController = navController
            )
        }
    }
}