package com.zaurh.polskismak.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base class for the MVI pattern used by every screen:
 *
 * - **State**  – a single immutable snapshot the UI renders ([state]).
 * - **Intent** – everything the user does is sent through [onIntent].
 * - **Effect** – one-off events such as navigation or sharing ([effects]).
 *
 * The UI never mutates state directly; it only sends intents. The ViewModel reduces
 * intents into a new state with [setState] or emits effects with [sendEffect].
 */
abstract class MviViewModel<State, Intent, Effect>(initialState: State) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effects = Channel<Effect>(Channel.BUFFERED)
    val effects: Flow<Effect> = _effects.receiveAsFlow()

    protected val currentState: State get() = _state.value

    fun onIntent(intent: Intent) = handleIntent(intent)

    protected abstract fun handleIntent(intent: Intent)

    protected fun setState(reduce: State.() -> State) = _state.update(reduce)

    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch { _effects.send(effect) }
    }
}
