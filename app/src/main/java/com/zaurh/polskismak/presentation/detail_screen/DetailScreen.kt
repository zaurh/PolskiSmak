package com.zaurh.polskismak.presentation.detail_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.zaurh.polskismak.R
import com.zaurh.polskismak.data.remote.dto.Ingredient
import com.zaurh.polskismak.data.remote.dto.Recipe
import com.zaurh.polskismak.data.remote.toFavoritesEntity
import com.zaurh.polskismak.presentation.components.AnimatedShimmer
import com.zaurh.polskismak.presentation.favorite_screen.FavoriteViewModel
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel,
    favoriteViewModel: FavoriteViewModel,
    navController: NavController,
    id: Int
) {
    val specificMeal = detailViewModel.detailState.value
    val state = rememberCollapsingToolbarScaffoldState()
    var instructions by remember { mutableStateOf(true) }
    var ingredients by remember { mutableStateOf(false) }
    var selectedIngredient: Ingredient? by remember { mutableStateOf(null) }
    var ingredientAlert by remember { mutableStateOf(false) }
    val favoriteList = favoriteViewModel.favoritesState.value.resultList

    val isFavorite by remember { mutableStateOf(favoriteList.any { it.mealId == id }) }
    var addFavoriteState by remember { mutableStateOf(isFavorite) }


    LaunchedEffect(true) {
        detailViewModel.getMeal(id)
    }

    if (ingredientAlert) {
        selectedIngredient?.let {
            IngredientAlert(
                name = it.name,
                imageUrl = it.imageUrl ?: "",
                quantity = it.quantity ?: "",
                unit = it.unit ?: "",
                recipe = it.recipe ?: listOf(),
                onDismiss = {
                    ingredientAlert = false
                    selectedIngredient = null
                }, onClose = {
                    ingredientAlert = false
                    selectedIngredient = null
                }) {

            }
        }
    }



    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        toolbar = {

            val textSize = (18 + (30 - 18) * state.toolbarState.progress).sp
            val imageSize = (150 + 150 * state.toolbarState.progress).dp
            val imageBlur = (14 - 14 * state.toolbarState.progress).dp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .pin()
            )
            Image(
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
//                    .blur(imageBlur)
                    .fillMaxWidth()
                    .height(imageSize)
                    .pin(),
                painter = rememberImagePainter(data = specificMeal.result?.imageUrl),
                contentDescription = null
            )
            if (specificMeal.isLoading) {
                AnimatedShimmer()
            }
            Text(
                text = "",
                modifier = Modifier
                    .road(Alignment.CenterStart, Alignment.BottomEnd)
                    .padding(60.dp, 16.dp, 16.dp, 16.dp),
                color = MaterialTheme.colorScheme.primary,
                fontSize = textSize
            )

        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .background(color = MaterialTheme.colorScheme.surface),
                horizontalAlignment = CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10))
                        .background(color = MaterialTheme.colorScheme.background),
                    horizontalAlignment = CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(top = 8.dp, bottom = 8.dp),
                        painter = painterResource(id = R.drawable.pull_up),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        if (specificMeal.isLoading) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                        }
                        specificMeal.result?.let {
                            Text(
                                text = it.name,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 24.sp
                            )
                        }
                        IconButton(enabled = specificMeal.result != null,onClick = {
                            addFavoriteState = !addFavoriteState
                            if (addFavoriteState) {
                                favoriteViewModel.addToFavorite(specificMeal.result!!.toFavoritesEntity())
                            } else {
                                favoriteViewModel.removeFavorite(specificMeal.result!!.toFavoritesEntity())
                            }
                        }) {
                            Icon(
                                imageVector = if (addFavoriteState) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "",
                                tint = if (addFavoriteState) Color.Red else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(32.dp))
                    specificMeal.result?.let {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = CenterHorizontally) {
                                val totalTime = it.cookTimeMinutes + it.prepTimeMinutes
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(id = R.drawable.clock),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                                Text(
                                    text = "$totalTime Minute",
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(text = "Cooking", color = MaterialTheme.colorScheme.secondary)
                            }
                            Text(
                                text = "|",
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Column(horizontalAlignment = CenterHorizontally) {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(id = R.drawable.fire),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                                Text(
                                    text = when (it.difficulty) {
                                        "EASY" -> "Easy level"
                                        "MODERATE" -> "Mid. level"
                                        "HARD" -> "Hard level"
                                        else -> {
                                            ""
                                        }
                                    }, color = MaterialTheme.colorScheme.primary
                                )
                                Text(text = "Recipes", color = MaterialTheme.colorScheme.secondary)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(32.dp))


                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 28.dp, end = 28.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                MaterialTheme.colorScheme.surface
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        ChooseItem(state = instructions, text = "Instructions") {
                            instructions = true
                            ingredients = false
                        }
                        ChooseItem(state = ingredients, text = "Ingredients") {
                            instructions = false
                            ingredients = true
                        }
                    }
                    Spacer(modifier = Modifier.size(32.dp))
                }
            }
            LazyColumn(modifier = Modifier.padding(bottom = 100.dp)) {
                item {
                    specificMeal.result?.let {
                        if (instructions) {
                            Column(Modifier.padding(bottom = 20.dp)) {
                                it.instructions.forEach { mealInstr ->
                                    Text(
                                        text = "${mealInstr.number}. ${mealInstr.step}",
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                }
                            }


                        } else {
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surface)
                                    .fillMaxWidth() // Ensure the Box fills the width of its parent
                                    .height(500.dp) // Set a fixed height
                            ) {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(4),
                                    content = {
                                        items(it.ingredients) { ingredient ->
                                            IngredientItem(
                                                onClick = {
                                                    ingredientAlert = true
                                                    selectedIngredient = ingredient
                                                },
                                                imageUrl = ingredient.imageUrl ?: "",
                                                name = ingredient.name,
                                                textSize = 16
                                            )
                                        }
                                    }
                                )
                            }


//                            }
                        }
                    }
                }

            }

        }
    }
}

@Composable
private fun ChooseItem(
    state: Boolean,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
            .clip(RoundedCornerShape(50))
            .clickable {
                onClick()
            }
            .background(if (state) MaterialTheme.colorScheme.background else Color.Transparent)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(14.dp),
            color = if (state) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun IngredientItem(
    onClick: () -> Unit,
    imageUrl: String,
    name: String,
    textSize: Int
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp), horizontalAlignment = CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20))
                .size(80.dp)
                .background(color = MaterialTheme.colorScheme.background)
                .clickable {
                    onClick()
                },
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(70.dp),
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = ""
            )
        }
        val ingredientName = name.split(" ")
        Text(
            text = ingredientName.joinToString(separator = "\n"),
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            fontSize = textSize.sp
        )
    }
}

@Composable
fun RecipeItem(
    recipe: Recipe
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(20))
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(100.dp),
            painter = rememberImagePainter(data = recipe.imageUrl),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.size(16.dp))
        Column() {
            Text(text = recipe.name, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
            if (recipe.quantity != "") {
                Text(
                    text = "${recipe.quantity} ${recipe.unit}",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            if (recipe.optional != null) {
                Text(text = recipe.optional, color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}


@Composable
fun IngredientAlert(
    name: String,
    imageUrl: String,
    quantity: String,
    unit: String,
    recipe: List<Recipe> = listOf(),
    onDismiss: () -> Unit,
    onClose: () -> Unit,
    onRecipeClick: (Recipe) -> Unit
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        text = {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(200.dp),
                    painter = rememberImagePainter(data = imageUrl),
                    contentDescription = ""
                )
                Text(text = name, fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.size(16.dp))
                if (quantity != "") {
                    Text(text = "$quantity $unit", color = MaterialTheme.colorScheme.primary)
                }
                if (recipe.isNotEmpty()) {
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(text = "Ingredients:", color = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.size(16.dp))
                    LazyColumn(modifier = Modifier.height(200.dp)) {
                        items(recipe) {
                            RecipeItem(recipe = it)
                        }
                    }
                }
            }
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            Button(colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            ), onClick = {
                onClose()
            }) {
                Text(text = "Close", color = MaterialTheme.colorScheme.surface)
            }
        }
    )
}


