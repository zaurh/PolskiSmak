package com.zaurh.polskismak.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zaurh.polskismak.feature.detail.DetailRoute as DetailScreenRoute
import com.zaurh.polskismak.feature.favorites.FavoritesRoute as FavoritesScreenRoute
import com.zaurh.polskismak.feature.home.HomeRoute as HomeScreenRoute
import com.zaurh.polskismak.feature.onboarding.OnboardingRoute as OnboardingScreenRoute
import com.zaurh.polskismak.feature.settings.SettingsRoute as SettingsScreenRoute

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Any,
    modifier: Modifier = Modifier,
) {
    val openMeal: (Int) -> Unit = { id -> navController.navigate(DetailRoute(id)) }

    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        composable<OnboardingRoute> {
            OnboardingScreenRoute(onFinished = {
                navController.navigate(HomeRoute) {
                    popUpTo<OnboardingRoute> { inclusive = true }
                }
            })
        }
        composable<HomeRoute> {
            HomeScreenRoute(onOpenMeal = openMeal)
        }
        composable<FavoritesRoute> {
            FavoritesScreenRoute(
                onOpenMeal = openMeal,
                onBrowseRecipes = { navController.navigateToTopLevel(HomeRoute) },
            )
        }
        composable<SettingsRoute> {
            SettingsScreenRoute()
        }
        composable<DetailRoute> {
            DetailScreenRoute(onBack = { navController.popBackStack() })
        }
    }
}

fun NavHostController.navigateToTopLevel(route: Any) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
