package com.zaurh.polskismak.feature.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zaurh.polskismak.R
import com.zaurh.polskismak.core.mvi.CollectEffects
import kotlinx.coroutines.launch

private data class OnboardingPage(
    @param:DrawableRes val image: Int,
    @param:StringRes val title: Int,
    @param:StringRes val description: Int,
)

private val pages = listOf(
    OnboardingPage(R.drawable.logo, R.string.onboarding_title_1, R.string.onboarding_desc_1),
    OnboardingPage(R.drawable.onboarding1, R.string.onboarding_title_2, R.string.onboarding_desc_2),
    OnboardingPage(R.drawable.onboarding2, R.string.onboarding_title_3, R.string.onboarding_desc_3),
)

@Composable
fun OnboardingRoute(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            OnboardingEffect.NavigateToHome -> onFinished()
        }
    }
    OnboardingScreen(onIntent = viewModel::onIntent)
}

@Composable
fun OnboardingScreen(onIntent: (OnboardingIntent) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == pages.lastIndex

    Column(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.surface)
                )
            )
            .safeDrawingPadding()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                onClick = { onIntent(OnboardingIntent.Finish) },
                modifier = Modifier.alpha(if (isLastPage) 0f else 1f),
                enabled = !isLastPage,
            ) { Text(stringResource(R.string.onboarding_skip)) }
        }

        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { index ->
            PageContent(pages[index])
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PageIndicator(count = pages.size, selected = pagerState.currentPage, modifier = Modifier.weight(1f))
            Button(onClick = {
                if (isLastPage) {
                    onIntent(OnboardingIntent.Finish)
                } else {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                }
            }) {
                Text(stringResource(if (isLastPage) R.string.onboarding_start else R.string.onboarding_next))
            }
        }
    }
}

@Composable
private fun PageContent(page: OnboardingPage) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(page.image),
            contentDescription = null,
            modifier = Modifier.size(240.dp),
        )
        Spacer(Modifier.height(32.dp))
        Text(
            stringResource(page.title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            stringResource(page.description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PageIndicator(count: Int, selected: Int, modifier: Modifier = Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(count) { index ->
            val isSelected = index == selected
            val width by animateDpAsState(if (isSelected) 24.dp else 8.dp, label = "dotWidth")
            val color by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                label = "dotColor",
            )
            Box(
                Modifier
                    .height(8.dp)
                    .width(width)
                    .background(color, CircleShape)
            )
        }
    }
}
