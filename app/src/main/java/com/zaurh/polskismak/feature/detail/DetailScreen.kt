package com.zaurh.polskismak.feature.detail

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Kitchen
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.RestaurantMenu
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaurh.polskismak.R
import com.zaurh.polskismak.core.mvi.CollectEffects
import com.zaurh.polskismak.core.ui.EmptyState
import com.zaurh.polskismak.core.ui.IngredientImage
import com.zaurh.polskismak.core.ui.PlateImage
import com.zaurh.polskismak.core.ui.labelRes
import com.zaurh.polskismak.core.ui.localized
import com.zaurh.polskismak.core.ui.quantityText
import com.zaurh.polskismak.domain.model.Ingredient
import com.zaurh.polskismak.domain.model.Instruction
import com.zaurh.polskismak.domain.model.Meal

@Composable
fun DetailRoute(
    onBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            DetailEffect.NavigateBack -> onBack()
            is DetailEffect.ShareRecipe -> context.shareRecipe(effect.meal)
        }
    }
    DetailScreen(state = state, onIntent = viewModel::onIntent)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailScreen(state: DetailState, onIntent: (DetailIntent) -> Unit) {
    val listState = rememberLazyListState()
    val scrolled by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 240 }
    }
    KeepScreenOn(enabled = state.selectedTab == DetailTab.STEPS)

    // When switching tabs while scrolled down, start the new tab from its top (just under the tabs).
    LaunchedEffect(state.selectedTab) {
        if (listState.firstVisibleItemIndex >= TABS_ITEM_INDEX) listState.scrollToItem(TABS_ITEM_INDEX)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        DetailTopBar(
            title = state.meal?.name?.localized().orEmpty(),
            showTitle = scrolled,
            isFavorite = state.isFavorite,
            showActions = state.meal != null,
            onIntent = onIntent,
        )

        val meal = state.meal
        if (meal == null) {
            if (state.notFound) {
                EmptyState(
                    icon = Icons.Rounded.RestaurantMenu,
                    title = stringResource(R.string.detail_not_found),
                    message = "",
                )
            }
            return@Column
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 24.dp
            ),
        ) {
            item(key = "hero") { Hero(meal.image) }
            item(key = "title") { TitleSection(meal) }
            item(key = "stats") { StatsRow(meal) }
            stickyHeader(key = "tabs") {
                DetailTabs(
                    selected = state.selectedTab,
                    ingredientCount = meal.ingredientCount,
                    stepCount = meal.instructions.size,
                    onSelect = { onIntent(DetailIntent.TabSelected(it)) },
                )
            }
            when (state.selectedTab) {
                DetailTab.INGREDIENTS -> ingredients(meal.ingredients)
                DetailTab.STEPS -> steps(meal.instructions, state.completedSteps) {
                    onIntent(DetailIntent.StepToggled(it))
                }
            }
        }
    }
}

@Composable
private fun DetailTopBar(
    title: String,
    showTitle: Boolean,
    isFavorite: Boolean,
    showActions: Boolean,
    onIntent: (DetailIntent) -> Unit,
) {
    val color by animateColorAsState(
        if (showTitle) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.secondaryContainer,
        label = "topBarColor",
    )
    Surface(color = color) {
        Row(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(64.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { onIntent(DetailIntent.BackClicked) }) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back))
            }
            Box(Modifier.weight(1f)) {
                androidx.compose.animation.AnimatedVisibility(visible = showTitle, enter = fadeIn(), exit = fadeOut()) {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            if (showActions) {
                IconButton(onClick = { onIntent(DetailIntent.ShareClicked) }) {
                    Icon(Icons.Rounded.Share, contentDescription = stringResource(R.string.share))
                }
                IconButton(onClick = { onIntent(DetailIntent.ToggleFavorite) }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = stringResource(
                            if (isFavorite) R.string.remove_from_favorites else R.string.add_to_favorites
                        ),
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@Composable
private fun Hero(@DrawableRes image: Int) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(
                Brush.verticalGradient(
                    listOf(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.surface)
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        PlateImage(image, modifier = Modifier.size(240.dp), elevation = 16.dp)
    }
}

@Composable
private fun TitleSection(meal: Meal) {
    Column(Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Tag(stringResource(meal.dietary.labelRes))
            Tag(meal.origin.localized())
        }
        Spacer(Modifier.height(12.dp))
        Text(meal.name.localized(), style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            meal.description.localized(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun Tag(text: String) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
    ) {
        Text(
            text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        )
    }
}

@Composable
private fun StatsRow(meal: Meal) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        StatTile(
            icon = Icons.Rounded.Schedule,
            value = stringResource(R.string.minutes_short, meal.totalTimeMinutes),
            label = stringResource(R.string.detail_time_split, meal.prepTimeMinutes, meal.cookTimeMinutes),
            modifier = Modifier.weight(1f),
        )
        StatTile(
            icon = Icons.Rounded.LocalFireDepartment,
            value = stringResource(meal.difficulty.labelRes),
            label = stringResource(R.string.detail_difficulty),
            modifier = Modifier.weight(1f),
        )
        StatTile(
            icon = Icons.Rounded.Kitchen,
            value = meal.ingredientCount.toString(),
            label = stringResource(R.string.detail_ingredients),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatTile(icon: ImageVector, value: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            Spacer(Modifier.height(6.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, maxLines = 1)
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailTabs(
    selected: DetailTab,
    ingredientCount: Int,
    stepCount: Int,
    onSelect: (DetailTab) -> Unit,
) {
    PrimaryTabRow(
        selectedTabIndex = selected.ordinal,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Tab(
            selected = selected == DetailTab.INGREDIENTS,
            onClick = { onSelect(DetailTab.INGREDIENTS) },
            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            text = { Text(stringResource(R.string.tab_ingredients, ingredientCount)) },
        )
        Tab(
            selected = selected == DetailTab.STEPS,
            onClick = { onSelect(DetailTab.STEPS) },
            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            text = { Text(stringResource(R.string.tab_steps, stepCount)) },
        )
    }
}

private fun LazyListScope.ingredients(ingredients: List<Ingredient>) {
    item(key = "ingredients_top") { Spacer(Modifier.height(12.dp)) }
    itemsIndexed(ingredients, key = { index, _ -> "ingredient_$index" }) { _, ingredient ->
        if (ingredient.items.isEmpty()) {
            IngredientRow(
                image = ingredient.image,
                name = ingredient.name.localized(),
                note = ingredient.note?.localized(),
                amount = quantityText(ingredient.quantity, ingredient.unit),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            )
        } else {
            IngredientGroup(ingredient)
        }
    }
}

@Composable
private fun IngredientGroup(group: Ingredient) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Row(
            Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IngredientImage(group.image, Modifier.size(44.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(group.name.localized(), style = MaterialTheme.typography.titleMedium)
                group.note?.let {
                    Text(it.localized(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        Column(Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
            group.items.forEach { item ->
                IngredientRow(
                    image = item.image,
                    name = item.name.localized(),
                    note = item.note?.localized(),
                    amount = quantityText(item.quantity, item.unit),
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                )
            }
        }
    }
}

@Composable
private fun IngredientRow(
    @DrawableRes image: Int,
    name: String,
    note: String?,
    amount: String?,
    modifier: Modifier = Modifier,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surface,
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(56.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center,
        ) {
            IngredientImage(image, Modifier.size(44.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(name, style = MaterialTheme.typography.bodyLarge)
            note?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        amount?.let {
            Spacer(Modifier.width(12.dp))
            Text(
                it,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.width(IntrinsicAmountWidth),
                maxLines = 3,
                textAlign = androidx.compose.ui.text.style.TextAlign.End,
            )
        }
    }
}

private val IntrinsicAmountWidth = 104.dp

/** hero, title, stats, then the sticky tabs. */
private const val TABS_ITEM_INDEX = 3

private fun LazyListScope.steps(
    steps: List<Instruction>,
    completed: Set<Int>,
    onToggle: (Int) -> Unit,
) {
    item(key = "steps_progress") {
        val done = steps.count { it.number in completed }
        val progress by animateFloatAsState(
            if (steps.isEmpty()) 0f else done.toFloat() / steps.size,
            label = "stepsProgress",
        )
        Column(Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(
                stringResource(R.string.steps_progress, done, steps.size),
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                drawStopIndicator = {},
            )
            Spacer(Modifier.height(6.dp))
            Text(
                stringResource(R.string.steps_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
    itemsIndexed(steps, key = { index, _ -> "step_$index" }) { _, step ->
        StepCard(step = step, done = step.number in completed, onClick = { onToggle(step.number) })
    }
}

@Composable
private fun StepCard(step: Instruction, done: Boolean, onClick: () -> Unit) {
    val contentAlpha by animateFloatAsState(if (done) 0.55f else 1f, label = "stepAlpha")
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (done) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Surface(
                shape = CircleShape,
                color = if (done) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                contentColor = if (done) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(32.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (done) {
                        Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    } else {
                        Text(step.number.toString(), style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
            Spacer(Modifier.width(14.dp))
            Text(
                step.step.localized(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .alpha(contentAlpha),
            )
        }
    }
}

/** Stops the screen from dimming while the user is following the steps. */
@Composable
private fun KeepScreenOn(enabled: Boolean) {
    val view = LocalView.current
    DisposableEffect(view, enabled) {
        view.keepScreenOn = enabled
        onDispose { view.keepScreenOn = false }
    }
}
