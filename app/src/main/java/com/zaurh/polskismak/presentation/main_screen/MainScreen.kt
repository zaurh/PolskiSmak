@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.zaurh.polskismak.presentation.main_screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zaurh.polskismak.R
import com.zaurh.polskismak.data.local.entities.MealsEntity
import com.zaurh.polskismak.presentation.components.AnimatedShimmerMain
import com.zaurh.polskismak.presentation.components.MySearchBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    var mealList = mainViewModel.mealListState.value
    val randomQuote = mainViewModel.quoteState.value
    var randomMeal by remember { mutableStateOf(0) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(bottom = 100.dp),
                onClick = {
                    val numbers = 1..mealList.resultList.size
                    randomMeal = numbers.random()
                    navController.navigate("detail_screen/${randomMeal}")
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
//                    Row() {
//                        Text(text = "RANDOM: ", color = MaterialTheme.colorScheme.primary)
//                        Text(text = mealState.result?.mealId.toString() ?: "", color = MaterialTheme.colorScheme.primary)
//                    }
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
                                text = it.author ?: "",
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
                if (mealList.isLoading) {
                    AnimatedShimmerMain()
                }
                LazyVerticalGrid(modifier = Modifier.padding(bottom = 80.dp),columns = GridCells.Fixed(2), content = {
                    items(mealList.resultList) {
                        MealItem(
                            mealsEntity = it,
                            onClick = {
                                navController.navigate("detail_screen/${it.mealId}")
                                println("id: ${it.mealId}")
                            })
                    }
                })
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}

enum class NavigationBarItems(val icon: ImageVector) {
    Home(icon = Icons.Default.Home),
    Recipe(icon = Icons.Default.Favorite),
    Settings(icon = Icons.Default.Settings)
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







