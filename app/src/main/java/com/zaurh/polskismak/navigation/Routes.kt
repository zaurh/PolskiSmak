package com.zaurh.polskismak.navigation

import kotlinx.serialization.Serializable

@Serializable
data object OnboardingRoute

@Serializable
data object HomeRoute

@Serializable
data object FavoritesRoute

@Serializable
data object SettingsRoute

@Serializable
data class DetailRoute(val id: Int) {
    companion object {
        /** Must match the property name above; read by DetailViewModel via SavedStateHandle. */
        const val ARG_ID = "id"
    }
}
