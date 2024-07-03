package com.zaurh.polskismak.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.zaurh.polskismak.R
import com.zaurh.polskismak.data.local.entities.MealsEntity
import com.zaurh.polskismak.navigation.Screen
import com.zaurh.polskismak.presentation.components.MySearchBar
import com.zaurh.polskismak.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val mealList = mainViewModel.mealListState.value
    val randomQuote = mainViewModel.quoteState.value
    var randomMeal by remember { mutableStateOf(0) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty))

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = mainViewModel.isRefreshing.value
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(bottom = 100.dp),
                onClick = {
                    val numbers = 1..mealList.resultList.size
                    randomMeal = numbers.random()
                    navController.navigate(Screen.Detail.createRoute(randomMeal))
                }) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    painter = painterResource(id = R.drawable.random),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        },
        content = {
            SwipeRefresh(state = swipeRefreshState, onRefresh = {
                mainViewModel.refreshRecipes()
            }) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (randomQuote.isLoading) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            randomQuote.result?.let {
                                Text(
                                    text = "“${it.quote}”",
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = it.author,
                                    fontStyle = FontStyle.Italic,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }
                        }
                    }
                    MySearchBar(
                        text = stringResource(id = R.string.search_recipe),
                        onBack = {
                            mainViewModel.clearSearch()
                        },
                        onSearch = {
                            mainViewModel.searchMeals(it)
                        }
                    )
                    if (mealList.resultList.isEmpty()) {
                        LottieAnimation(
                            modifier = Modifier.size(200.dp),
                            speed = 0.5f,
                            composition = composition
                        )
                    }
                    LazyVerticalGrid(
                        modifier = Modifier.padding(bottom = 80.dp),
                        columns = GridCells.Fixed(2),
                        content = {
                            items(mealList.resultList.shuffled()) {
                                MealItem(
                                    mealsEntity = it,
                                    onClick = {
                                        navController.navigate(Screen.Detail.createRoute(it.mealId))
                                    })
                            }
                        })
                }
            }

        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
fun MealItem(
    mealsEntity: MealsEntity,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .height(200.dp)
            .width(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(150.dp)
                .padding(top = 60.dp)
                .clip(RoundedCornerShape(20))
                .clickable {
                    onClick()
                }
                .background(MaterialTheme.colorScheme.background)
        )
        Box {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(
                            RoundedCornerShape(50)
                        )
                        .clickable {
                            onClick()
                        },
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(mealsEntity.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "",
                )
                Text(text = mealsEntity.name, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.size(8.dp))
                Row(horizontalArrangement = Arrangement.Center) {
                    val totalTime = mealsEntity.cookTimeMinutes + mealsEntity.prepTimeMinutes
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$totalTime", color = MaterialTheme.colorScheme.secondary)
                        Text(text = "Min", color = MaterialTheme.colorScheme.secondary)
                    }
                    Text(text = " | ", color = MaterialTheme.colorScheme.secondary)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = when (mealsEntity.difficulty) {
                                "EASY" -> "Easy"
                                "MODERATE" -> "Mid."
                                "HARD" -> "Hard"
                                else -> {
                                    ""
                                }
                            }, color = MaterialTheme.colorScheme.secondary
                        )
                        Text(text = "Lvl", color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }

    }
}







