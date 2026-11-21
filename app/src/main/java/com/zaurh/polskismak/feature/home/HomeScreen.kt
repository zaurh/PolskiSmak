package com.zaurh.polskismak.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Casino
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
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
import com.zaurh.polskismak.domain.model.Quote

@Composable
fun HomeRoute(
    onOpenMeal: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            is HomeEffect.NavigateToDetail -> onOpenMeal(effect.mealId)
        }
    }
    HomeScreen(state = state, onIntent = viewModel::onIntent)
}

@Composable
fun HomeScreen(state: HomeState, onIntent: (HomeIntent) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        fullWidth("header") { Header() }
        state.quote?.let { quote ->
            fullWidth("quote") { QuoteCard(quote, onClick = { onIntent(HomeIntent.ShuffleQuote) }) }
        }
        fullWidth("search") {
            SearchField(
                query = state.query,
                placeholder = stringResource(R.string.search_recipes),
                onQueryChange = { onIntent(HomeIntent.QueryChanged(it)) },
                onClear = { onIntent(HomeIntent.ClearQuery) },
            )
        }
        fullWidth("filters") {
            FilterRow(selected = state.filter, onSelect = { onIntent(HomeIntent.FilterSelected(it)) })
        }
        if (state.query.isEmpty() && state.filter == MealFilter.ALL) {
            fullWidth("surprise") { SurpriseCard(onClick = { onIntent(HomeIntent.SurpriseMe) }) }
        }
        fullWidth("section") {
            Text(
                text = stringResource(R.string.home_section_recipes, state.meals.size),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        if (state.meals.isEmpty()) {
            fullWidth("empty") {
                EmptyState(
                    icon = Icons.Rounded.SearchOff,
                    title = stringResource(R.string.home_empty_title),
                    message = stringResource(R.string.home_empty_message),
                )
            }
        }
        items(state.meals, key = { it.id }) { meal ->
            MealCard(
                meal = meal,
                isFavorite = meal.id in state.favoriteIds,
                onClick = { onIntent(HomeIntent.MealClicked(meal.id)) },
                onToggleFavorite = { onIntent(HomeIntent.ToggleFavorite(meal.id)) },
                modifier = Modifier.animateItem(),
            )
        }
    }
}

private fun LazyGridScope.fullWidth(key: String, content: @Composable () -> Unit) {
    item(key = key, span = { GridItemSpan(maxLineSpan) }) { content() }
}

@Composable
private fun Header() {
    Column(Modifier.padding(top = 16.dp, bottom = 4.dp)) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(R.string.home_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun QuoteCard(quote: Quote, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(
                Icons.Rounded.FormatQuote,
                contentDescription = stringResource(R.string.home_quote_hint),
                modifier = Modifier.size(28.dp),
            )
            Column {
                Text(quote.text.localized(), style = MaterialTheme.typography.bodyLarge, fontStyle = FontStyle.Italic)
                Spacer(Modifier.height(6.dp))
                Text(
                    "— ${quote.author}",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}

@Composable
private fun FilterRow(selected: MealFilter, onSelect: (MealFilter) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(MealFilter.entries) { filter ->
            FilterChip(
                selected = filter == selected,
                onClick = { onSelect(filter) },
                label = { Text(stringResource(filter.labelRes)) },
            )
        }
    }
}

private val MealFilter.labelRes: Int
    get() = when (this) {
        MealFilter.ALL -> R.string.filter_all
        MealFilter.QUICK -> R.string.filter_quick
        MealFilter.EASY -> R.string.filter_easy
        MealFilter.VEGETARIAN -> R.string.filter_vegetarian
    }

@Composable
private fun SurpriseCard(onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.surprise_title), style = MaterialTheme.typography.titleMedium)
                Text(stringResource(R.string.surprise_subtitle), style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.width(12.dp))
            FilledTonalButton(onClick = onClick) {
                Icon(Icons.Rounded.Casino, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.surprise_action))
            }
        }
    }
}

@Composable
private fun MealCard(
    meal: Meal,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Box {
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.1f)
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.secondaryContainer,
                                MaterialTheme.colorScheme.surfaceContainerLow,
                            )
                        )
                    ),
                contentAlignment = Alignment.Center,
            ) {
                PlateImage(meal.image, modifier = Modifier.fillMaxSize(0.8f), elevation = 8.dp)
            }
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                ),
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    contentDescription = stringResource(
                        if (isFavorite) R.string.remove_from_favorites else R.string.add_to_favorites
                    ),
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Column(Modifier.padding(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 14.dp)) {
            Text(
                meal.name.localized(),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(6.dp))
            MealMeta(meal)
        }
    }
}

