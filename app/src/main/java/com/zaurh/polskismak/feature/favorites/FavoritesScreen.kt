package com.zaurh.polskismak.feature.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaurh.polskismak.R
import com.zaurh.polskismak.core.mvi.CollectEffects
import com.zaurh.polskismak.core.ui.EmptyState
import com.zaurh.polskismak.core.ui.MealMeta
import com.zaurh.polskismak.core.ui.PlateImage
import com.zaurh.polskismak.core.ui.SearchField
import com.zaurh.polskismak.core.ui.localized
import com.zaurh.polskismak.domain.model.Meal
import kotlinx.coroutines.launch

@Composable
fun FavoritesRoute(
    onOpenMeal: (Int) -> Unit,
    onBrowseRecipes: () -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val resources = context.resources

    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            is FavoritesEffect.NavigateToDetail -> onOpenMeal(effect.mealId)
            FavoritesEffect.NavigateToHome -> onBrowseRecipes()
            is FavoritesEffect.ShowRemoved -> scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                val result = snackbarHostState.showSnackbar(
                    message = resources.getString(R.string.favorites_removed, effect.mealName.localized(context)),
                    actionLabel = resources.getString(R.string.undo),
                    duration = SnackbarDuration.Short,
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.onIntent(FavoritesIntent.UndoRemove(effect.mealId))
                }
            }
        }
    }
    FavoritesScreen(state = state, snackbarHostState = snackbarHostState, onIntent = viewModel::onIntent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    state: FavoritesState,
    snackbarHostState: SnackbarHostState,
    onIntent: (FavoritesIntent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets(0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.favorites_title)) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        if (!state.hasAnyFavorites) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                EmptyState(
                    icon = Icons.Rounded.FavoriteBorder,
                    title = stringResource(R.string.favorites_empty_title),
                    message = stringResource(R.string.favorites_empty_message),
                    action = {
                        Button(onClick = { onIntent(FavoritesIntent.BrowseRecipes) }) {
                            Text(stringResource(R.string.favorites_browse))
                        }
                    },
                )
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = padding.calculateTopPadding(),
                bottom = 24.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(key = "search") {
                SearchField(
                    query = state.query,
                    placeholder = stringResource(R.string.search_favorites),
                    onQueryChange = { onIntent(FavoritesIntent.QueryChanged(it)) },
                    onClear = { onIntent(FavoritesIntent.ClearQuery) },
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }
            if (state.favorites.isEmpty()) {
                item(key = "no_results") {
                    EmptyState(
                        icon = Icons.Rounded.SearchOff,
                        title = stringResource(R.string.home_empty_title),
                        message = stringResource(R.string.favorites_no_results),
                    )
                }
            }
            items(state.favorites, key = { it.id }) { meal ->
                FavoriteCard(
                    meal = meal,
                    onClick = { onIntent(FavoritesIntent.MealClicked(meal.id)) },
                    onRemove = { onIntent(FavoritesIntent.RemoveFavorite(meal.id)) },
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }
}

@Composable
private fun FavoriteCard(
    meal: Meal,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(96.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center,
            ) {
                PlateImage(meal.image, Modifier.size(80.dp), elevation = 6.dp)
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(meal.name.localized(), style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Text(
                    meal.description.localized(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(8.dp))
                MealMeta(meal)
            }
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Rounded.Favorite,
                    contentDescription = stringResource(R.string.remove_from_favorites),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
