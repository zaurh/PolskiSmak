package com.zaurh.polskismak.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zaurh.polskismak.R
import com.zaurh.polskismak.navigation.AppNavHost
import com.zaurh.polskismak.navigation.FavoritesRoute
import com.zaurh.polskismak.navigation.HomeRoute
import com.zaurh.polskismak.navigation.SettingsRoute
import com.zaurh.polskismak.navigation.navigateToTopLevel
import kotlin.reflect.KClass

private enum class TopLevelDestination(
    val route: Any,
    val routeClass: KClass<*>,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val labelRes: Int,
) {
    HOME(HomeRoute, HomeRoute::class, Icons.Rounded.Home, Icons.Outlined.Home, R.string.nav_home),
    FAVORITES(FavoritesRoute, FavoritesRoute::class, Icons.Rounded.Favorite, Icons.Outlined.FavoriteBorder, R.string.nav_favorites),
    SETTINGS(SettingsRoute, SettingsRoute::class, Icons.Rounded.Settings, Icons.Outlined.Settings, R.string.nav_settings),
}

@Composable
fun PolskiSmakAppContent(startDestination: Any) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val showBottomBar = TopLevelDestination.entries.any { currentDestination.isOn(it) }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            AnimatedVisibility(visible = showBottomBar, enter = expandVertically(), exit = shrinkVertically()) {
                NavigationBar {
                    TopLevelDestination.entries.forEach { destination ->
                        val selected = currentDestination.isOn(destination)
                        NavigationBarItem(
                            selected = selected,
                            onClick = { navController.navigateToTopLevel(destination.route) },
                            icon = {
                                Icon(
                                    if (selected) destination.selectedIcon else destination.unselectedIcon,
                                    contentDescription = null,
                                )
                            },
                            label = { Text(stringResource(destination.labelRes)) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        AppNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding()),
        )
    }
}

private fun NavDestination?.isOn(destination: TopLevelDestination): Boolean =
    this?.hierarchy?.any { it.hasRoute(destination.routeClass) } == true
