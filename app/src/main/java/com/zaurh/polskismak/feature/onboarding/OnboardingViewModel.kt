package com.zaurh.polskismak.feature.onboarding

import androidx.lifecycle.viewModelScope
import com.zaurh.polskismak.core.mvi.MviViewModel
import com.zaurh.polskismak.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Onboarding has no state of its own; the pager position lives in the UI. */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : MviViewModel<Unit, OnboardingIntent, OnboardingEffect>(Unit) {

    override fun handleIntent(intent: OnboardingIntent) {
        when (intent) {
            OnboardingIntent.Finish -> viewModelScope.launch {
                settingsRepository.setOnboardingCompleted()
                sendEffect(OnboardingEffect.NavigateToHome)
            }
        }
    }
}
