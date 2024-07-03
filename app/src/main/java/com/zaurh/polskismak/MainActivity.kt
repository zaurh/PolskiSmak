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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.zaurh.polskismak.common.BottomNavigationBar
import com.zaurh.polskismak.data_store.StoreSettings
import com.zaurh.polskismak.navigation.Navig
import com.zaurh.polskismak.navigation.Screen
import com.zaurh.polskismak.presentation.viewmodel.SplashViewModel
import com.zaurh.polskismak.ui.theme.ZaurH
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var splashViewModel: SplashViewModel

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            !splashViewModel.isLoading.value
        }
        setContent {
            val context = LocalContext.current
            val dataStore = StoreSettings(context)
            var darkTheme by remember { mutableStateOf(false) }
            val savedDarkMode = dataStore.getDarkMode.collectAsState(initial = false)
            val savedLanguageState = dataStore.getLanguage.collectAsState(initial = false)
            val scope = rememberCoroutineScope()

            ZaurH(darkTheme = savedDarkMode.value ?: false) {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val screen by splashViewModel.startDestination
                    Scaffold(
                        content = {
                            Navig(
                                navController = navController,
                                startDestination = screen,
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
                            if (screen != Screen.Welcome.route) {
                                BottomNavigationBar(navController = navController)
                            }
                        }
                    )
                }
            }
        }
    }
}








