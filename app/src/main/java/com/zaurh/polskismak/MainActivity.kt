package com.zaurh.polskismak

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaurh.polskismak.app.AppState
import com.zaurh.polskismak.app.AppViewModel
import com.zaurh.polskismak.app.PolskiSmakAppContent
import com.zaurh.polskismak.designsystem.theme.PolskiSmakTheme
import com.zaurh.polskismak.domain.model.ThemeMode
import com.zaurh.polskismak.navigation.HomeRoute
import com.zaurh.polskismak.navigation.OnboardingRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { appViewModel.state.value is AppState.Loading }
        enableEdgeToEdge()

        setContent {
            val state by appViewModel.state.collectAsStateWithLifecycle()
            val ready = state as? AppState.Ready ?: return@setContent
            val darkTheme = ready.themeMode.isDark()

            SystemBarsEffect(darkTheme)
            PolskiSmakTheme(darkTheme = darkTheme) {
                // Decided once; finishing onboarding later navigates instead of swapping the graph.
                val startDestination = remember { if (ready.onboardingCompleted) HomeRoute else OnboardingRoute }
                PolskiSmakAppContent(startDestination = startDestination)
            }
        }
    }

    @Composable
    private fun ThemeMode.isDark(): Boolean = when (this) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    /** Keeps status/navigation bar icons readable when the in-app theme differs from the system one. */
    @Composable
    private fun SystemBarsEffect(darkTheme: Boolean) {
        DisposableEffect(darkTheme) {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme },
                navigationBarStyle = SystemBarStyle.auto(LightScrim, DarkScrim) { darkTheme },
            )
            onDispose {}
        }
    }

    private companion object {
        val LightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
        val DarkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
    }
}
