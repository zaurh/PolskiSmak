@file:OptIn(ExperimentalMaterial3Api::class)

package com.zaurh.polskismak.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.zaurh.polskismak.R
import com.zaurh.polskismak.data.local.entities.MealsEntity
import com.zaurh.polskismak.data.remote.toMealsEntity
import com.zaurh.polskismak.navigation.Screen
import com.zaurh.polskismak.presentation.components.MySearchBar
import com.zaurh.polskismak.presentation.viewmodel.FavoriteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavoriteScreen(
    navController: NavController,
    favoriteViewModel: FavoriteViewModel
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty))
    val favoritesState = favoriteViewModel.favoritesState.value

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MySearchBar(
                    text = stringResource(id = R.string.search_favorite),
                    onBack = {
                        favoriteViewModel.clearSearch()
                    }, onSearch = { query ->
                        favoriteViewModel.searchFavorites(query)
                    })

                if (favoritesState.resultList.isEmpty()) {
                    LottieAnimation(
                        modifier = Modifier.size(200.dp),
                        speed = 0.5f,
                        composition = composition
                    )
                }
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(favoritesState.resultList) { favoriteEntity ->
                        FavoriteItem(mealsEntity = favoriteEntity.toMealsEntity()) {
                            navController.navigate(Screen.Detail.createRoute(favoriteEntity.mealId))
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun FavoriteItem(
    mealsEntity: MealsEntity,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable {
                onClick()
            }
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier.size(80.dp),
            model = mealsEntity.imageUrl,
            contentDescription = ""
        )
        Spacer(modifier = Modifier.size(16.dp))
        Column {
            Text(
                text = mealsEntity.name,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = mealsEntity.description,
                color = MaterialTheme.colorScheme.secondary,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}