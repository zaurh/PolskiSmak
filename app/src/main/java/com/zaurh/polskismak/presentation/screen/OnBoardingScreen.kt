package com.zaurh.polskismak.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.zaurh.polskismak.navigation.Screen
import com.zaurh.polskismak.presentation.components.OnBoardingPage
import com.zaurh.polskismak.presentation.viewmodel.WelcomeViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@Composable
fun OnBoardingScreen(
    navController: NavController, welcomeViewModel: WelcomeViewModel = hiltViewModel()
) {
    var welcomeTextVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        delay(1500)
        welcomeTextVisibility = true
    }

    val pages = listOf(
        OnBoardingPage.First, OnBoardingPage.Second, OnBoardingPage.Third
    )
    val pagerState = rememberPagerState(3)

    Column(modifier = Modifier.fillMaxSize()) {

        HorizontalPager(
            modifier = Modifier, state = pagerState, verticalAlignment = Alignment.Top
        ) { position ->
            PagerScreen(onBoardingPage = pages[position], visible = welcomeTextVisibility)
        }

        HorizontalPagerIndicator(
            modifier = Modifier.align(Alignment.CenterHorizontally), pagerState = pagerState
        )

        FinishButton(
            modifier = Modifier, pagerState = pagerState
        ) {
            welcomeViewModel.saveOnBoardingState(completed = true)
            navController.popBackStack()
            navController.navigate(Screen.Main.route)
        }
    }
}


@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage, visible: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = ""
        )
        AnimatedVisibility(visible = visible) {
            Column {
                Spacer(modifier = Modifier.size(14.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = onBoardingPage.title,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(14.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                        .padding(top = 20.dp),
                    text = onBoardingPage.description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

        }

    }

}


@OptIn(ExperimentalPagerApi::class)
@ExperimentalAnimationApi
@Composable
fun FinishButton(
    modifier: Modifier, pagerState: PagerState, onClick: () -> Unit
) {
    Row(
        modifier = modifier.padding(horizontal = 40.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(), visible = pagerState.currentPage == 2
        ) {
            Button(
                onClick = onClick, colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                )
            ) {
                Text(text = "Finish")
            }
        }
    }
}