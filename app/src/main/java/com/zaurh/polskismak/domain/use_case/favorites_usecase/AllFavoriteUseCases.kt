package com.zaurh.polskismak.domain.use_case.favorites_usecase

data class AllFavoriteUseCases(
    val addFavoriteUseCase: AddFavoriteUseCase,
    val getFavoritesUseCase: GetFavoritesUseCase,
    val removeFavoriteUseCase: RemoveFavoriteUseCase
)
