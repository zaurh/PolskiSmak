@file:OptIn(ExperimentalMaterial3Api::class)

package com.zaurh.polskismak.presentation.screen

import android.annotation.SuppressLint
import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.zaurh.polskismak.R
import com.zaurh.polskismak.common.sendMail
import com.zaurh.polskismak.data_store.StoreSettings
import kotlinx.coroutines.launch
import java.util.Locale


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    language: Boolean,
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit
) {
    val context = LocalContext.current
    val dataStore = StoreSettings(context)
    var languageState by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val deviceLocale = context.resources.configuration.locales.get(0)
    val currentLocale = remember { mutableStateOf(deviceLocale.toLanguageTag()) }
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("en")
    AppCompatDelegate.setApplicationLocales(appLocale)

    var aboutAlert by remember { mutableStateOf(false) }

    if (aboutAlert) {
        AlertDialog(
            onDismissRequest = { aboutAlert = false },
            title = { Text(text = "Polski Smak") },
            text = {
                Text(
                    text = stringResource(id = R.string.about_text)
                )
            },
            confirmButton = {
                Button(colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                ), onClick = { aboutAlert = false }) {
                    Text(
                        text = stringResource(id = R.string.close),
                        color = MaterialTheme.colorScheme.surface
                    )

                }
            })
    }
    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SettingsItem(
                    text = stringResource(id = R.string.language),
                    leadingIcon = painterResource(id = R.drawable.language),
                    onClick = {
                        languageState = !language
                        if (languageState) {
                            currentLocale.value = Locale("en").toLanguageTag()
                            localeSelection(context = context, localeTag = currentLocale.value)
                        } else {
                            currentLocale.value = Locale("pl").toLanguageTag()
                            localeSelection(context = context, localeTag = currentLocale.value)
                        }
                        scope.launch {
                            dataStore.saveLanguage(languageState)
                        }
                    }) {
                    CustomSwitcher(
                        switch = language,
                        size = 40.dp,
                        padding = 5.dp,
                        firstIcon = "\uD83C\uDDF5\uD83C\uDDF1",
                        secondIcon = "\uD83C\uDDEC\uD83C\uDDE7"
                    )
                }
                Divider(modifier = Modifier.alpha(0.3f))
                SettingsItem(
                    text = stringResource(id = R.string.dark_mode),
                    leadingIcon = painterResource(id = R.drawable.dark_mode),
                    onClick = {
                        onThemeUpdated()
                    }) {
                    CustomSwitcher(
                        switch = darkTheme,
                        size = 40.dp,
                        padding = 5.dp,
                        firstIcon = "☀️",
                        secondIcon = "\uD83C\uDF19"
                    )
                }
                Divider(modifier = Modifier.alpha(0.3f))
                SettingsItem(
                    text = stringResource(id = R.string.about),
                    leadingIcon = painterResource(id = R.drawable.about),
                    onClick = {
                        aboutAlert = true
                    }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "",
                        tint = Color.Gray
                    )
                }
                Divider(modifier = Modifier.alpha(0.3f))

                SettingsItem(
                    text = stringResource(id = R.string.contact),
                    leadingIcon = painterResource(id = R.drawable.mail),
                    onClick = {
                        context.sendMail(to = "zaurway@gmail.com", subject = "Polski Smak")
                    }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "",
                        tint = Color.Gray
                    )
                }
            }
        }
    )

}


@Composable
private fun CustomSwitcher(
    switch: Boolean,
    size: Dp = 150.dp,
    firstIcon: String,
    secondIcon: String,
    padding: Dp = 10.dp,
    borderWidth: Dp = 1.dp,
    parentShape: Shape = CircleShape,
    toggleShape: Shape = CircleShape,
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
) {
    val offset by animateDpAsState(
        targetValue = if (!switch) 0.dp else size,
        animationSpec = animationSpec
    )

    Box(
        modifier = Modifier
            .width(size * 2)
            .height(size)
            .clip(shape = parentShape)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .offset(x = offset)
                .padding(all = padding)
                .clip(shape = toggleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {}
        Row(
            modifier = Modifier
                .border(
                    border = BorderStroke(
                        width = borderWidth,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    shape = parentShape
                )
        ) {
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Text(text = firstIcon)
            }
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Text(text = secondIcon)
            }
        }
    }
}


fun localeSelection(context: Context, localeTag: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java).applicationLocales =
            LocaleList.forLanguageTags(localeTag)
    } else {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(localeTag)
        )
    }
}

@Composable
private fun SettingsItem(
    text: String,
    leadingIcon: Painter,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = leadingIcon, contentDescription = "")
            Spacer(modifier = Modifier.size(12.dp))
            Text(text = text, color = MaterialTheme.colorScheme.primary)
        }
        content()
    }
}