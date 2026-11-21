package com.zaurh.polskismak.feature.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaurh.polskismak.R
import com.zaurh.polskismak.core.mvi.CollectEffects
import com.zaurh.polskismak.domain.model.ThemeMode

@Composable
fun SettingsRoute(viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            SettingsEffect.OpenLanguageSettings -> context.openAppLanguageSettings()
            is SettingsEffect.SendEmail -> context.sendEmail(effect.address, effect.subject)
            SettingsEffect.OpenStoreListing -> context.openStoreListing()
        }
    }
    SettingsScreen(state = state, onIntent = viewModel::onIntent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(state: SettingsState, onIntent: (SettingsIntent) -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    if (state.showAbout) {
        AlertDialog(
            onDismissRequest = { onIntent(SettingsIntent.AboutDismissed) },
            icon = { Icon(Icons.Rounded.Info, contentDescription = null) },
            title = { Text(stringResource(R.string.app_name)) },
            text = { Text(stringResource(R.string.about_text)) },
            confirmButton = {
                TextButton(onClick = { onIntent(SettingsIntent.AboutDismissed) }) {
                    Text(stringResource(R.string.close))
                }
            },
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets(0),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = padding.calculateTopPadding(),
                bottom = 24.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                SettingsSection(title = stringResource(R.string.settings_appearance)) {
                    ThemeSelector(selected = state.themeMode, onSelect = { onIntent(SettingsIntent.ThemeSelected(it)) })
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        SectionDivider()
                        SettingsRow(
                            icon = Icons.Rounded.Language,
                            title = stringResource(R.string.settings_language),
                            summary = stringResource(R.string.settings_language_summary),
                            onClick = { onIntent(SettingsIntent.LanguageClicked) },
                        )
                    }
                }
            }
            item {
                SettingsSection(title = stringResource(R.string.settings_about_section)) {
                    SettingsRow(
                        icon = Icons.Rounded.Info,
                        title = stringResource(R.string.settings_about),
                        onClick = { onIntent(SettingsIntent.AboutClicked) },
                    )
                    SectionDivider()
                    SettingsRow(
                        icon = Icons.Rounded.MailOutline,
                        title = stringResource(R.string.settings_contact),
                        summary = stringResource(R.string.settings_contact_summary),
                        onClick = { onIntent(SettingsIntent.ContactClicked) },
                    )
                    SectionDivider()
                    SettingsRow(
                        icon = Icons.Rounded.StarOutline,
                        title = stringResource(R.string.settings_rate),
                        summary = stringResource(R.string.settings_rate_summary),
                        onClick = { onIntent(SettingsIntent.RateClicked) },
                    )
                }
            }
            item {
                Text(
                    stringResource(R.string.settings_version, state.versionName),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
        )
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        ) {
            content()
        }
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeSelector(selected: ThemeMode, onSelect: (ThemeMode) -> Unit) {
    Column(Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            Spacer(Modifier.width(16.dp))
            Text(stringResource(R.string.settings_theme), style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(Modifier.size(12.dp))
        val modes = ThemeMode.entries
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            modes.forEachIndexed { index, mode ->
                SegmentedButton(
                    selected = mode == selected,
                    onClick = { onSelect(mode) },
                    shape = SegmentedButtonDefaults.itemShape(index, modes.size),
                    label = {
                        Text(
                            stringResource(
                                when (mode) {
                                    ThemeMode.SYSTEM -> R.string.theme_system
                                    ThemeMode.LIGHT -> R.string.theme_light
                                    ThemeMode.DARK -> R.string.theme_dark
                                }
                            )
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    summary: String? = null,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            summary?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Icon(
            Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun Context.openAppLanguageSettings() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
    startSafely(
        Intent(Settings.ACTION_APP_LOCALE_SETTINGS, Uri.fromParts("package", packageName, null))
    )
}

private fun Context.sendEmail(address: String, subject: String) {
    startSafely(
        Intent(Intent.ACTION_SENDTO, "mailto:".toUri()).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
    )
}

private fun Context.openStoreListing() {
    val opened = startSafely(Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri()))
    if (!opened) {
        startSafely(Intent(Intent.ACTION_VIEW, "https://play.google.com/store/apps/details?id=$packageName".toUri()))
    }
}

private fun Context.startSafely(intent: Intent): Boolean = try {
    startActivity(intent)
    true
} catch (_: ActivityNotFoundException) {
    false
}
