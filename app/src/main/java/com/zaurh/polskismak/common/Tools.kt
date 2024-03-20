package com.zaurh.polskismak.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.zaurh.polskismak.R
import com.zaurh.polskismak.navigation.Screen


sealed class NavigationItem(
    var route: String,
    val icon: ImageVector?,
    @StringRes val titleResId: Int
) {
    object Home : NavigationItem(Screen.Main.route, Icons.Rounded.Home, R.string.home)
    object Favorites :
        NavigationItem(Screen.Favorite.route, Icons.Rounded.Favorite, R.string.favorites)

    object Settings :
        NavigationItem(Screen.Settings.route, Icons.Rounded.Settings, R.string.settings)
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Favorites,
        NavigationItem.Settings,
    )
    var selectedItem by remember { mutableStateOf(0) }

    // Update selected item based on current route
    LaunchedEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val newIndex = items.indexOfFirst { it.route == destination.route }
            if (newIndex != -1) {
                selectedItem = newIndex
            }
        }
        navController.addOnDestinationChangedListener(listener)
    }

    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = MaterialTheme.colorScheme.tertiary,
                    selectedIconColor = MaterialTheme.colorScheme.tertiary,
                    indicatorColor = MaterialTheme.colorScheme.background,
                    unselectedTextColor = Color.Gray,
                    unselectedIconColor = Color.Gray
                ),
                alwaysShowLabel = true,
                icon = { Icon(item.icon!!, contentDescription = "") },
                label = { Text(stringResource(id = item.titleResId)) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


fun Context.sendMail(to: String, subject: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "vnd.android.cursor.item/email" // or "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        //Handle case where no email app is available
    } catch (t: Throwable) {
        //Handle potential other type of exceptions
    }
}