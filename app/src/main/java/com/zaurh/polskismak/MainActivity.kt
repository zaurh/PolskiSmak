package com.zaurh.polskismak

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zaurh.polskismak.common.BottomNavigationBar
import com.zaurh.polskismak.data_store.StoreSettings
import com.zaurh.polskismak.presentation.detail_screen.DetailViewModel
import com.zaurh.polskismak.presentation.favorite_screen.FavoriteScreen
import com.zaurh.polskismak.presentation.main_screen.MainScreen
import com.zaurh.polskismak.presentation.main_screen.MainViewModel
import com.zaurh.polskismak.presentation.detail_screen.DetailScreen
import com.zaurh.polskismak.presentation.favorite_screen.FavoriteViewModel
import com.zaurh.polskismak.presentation.settings_screen.SettingsScreen
import com.zaurh.polskismak.ui.theme.ZaurH
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val dataStore = StoreSettings(context)
            var darkTheme by remember { mutableStateOf(false) }
            val savedDarkMode = dataStore.getDarkMode.collectAsState(initial = false)
            val savedLanguageState = dataStore.getLanguage.collectAsState(initial = false)
            val scope = rememberCoroutineScope()

            ZaurH(darkTheme = savedDarkMode.value ?: false) {
                val navController = rememberNavController()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        content = {
                            Navig(
                                navController = navController,
                                darkTheme = savedDarkMode.value ?: false,
                                language = savedLanguageState.value ?: false
                                ) {
                                scope.launch {
                                    darkTheme = !(savedDarkMode.value ?: false)
                                    dataStore.saveDarkMode(darkTheme)
                                }
                            }
                        },
                        bottomBar = {
                            BottomNavigationBar(navController = navController)
                        }
                    )
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Main : Screen(route = "main")
    object Favorite : Screen(route = "favorite_screen")
    object Settings : Screen(route = "settings_screen")
}

@Composable
fun Navig(
    navController: NavHostController,
    darkTheme: Boolean,
    language: Boolean,
    onThemeUpdated: () -> Unit
) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val detailViewModel = hiltViewModel<DetailViewModel>()
    val favoriteViewModel = hiltViewModel<FavoriteViewModel>()

    NavHost(navController = navController, startDestination = "main") {
        composable(Screen.Main.route) {
            MainScreen(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }
        composable("detail_screen/{id}", arguments = listOf(
            navArgument("id") { type = NavType.IntType }
        )) {
            val id = it.arguments?.getInt("id")
            DetailScreen(
                detailViewModel = detailViewModel,
                favoriteViewModel = favoriteViewModel,
                id = id ?: 1,
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
    }
}




