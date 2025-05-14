package com.art.lloyds.ui.main.baseViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.art.domain.common.DispatcherProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : UiState, Effect : UiEffect, Event : UiEvent> : ViewModel() {

    // State -----------------------------------------------------------------------------

    private val initialState: State by lazy { createInitialState() }
    protected abstract fun createInitialState(): State

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    //we must pass the "copy" function as a parameter with new values
    //for example: setState { copy(userId = newUserId) }
    protected fun setState(ignoredCopyFunction: State.() -> State) {
        _uiState.value = uiState.value.ignoredCopyFunction()
    }

    // Effect -----------------------------------------------------------------------------

    private val _uiEffect: Channel<Effect> = Channel()
    val uiEffect = _uiEffect.receiveAsFlow()

    protected fun setEffect(effect: Effect) {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiEffect.send(effect)
        }
    }

    // Event -----------------------------------------------------------------------------

    private val _uiEvent: MutableSharedFlow<Event> = MutableSharedFlow()
    private val uiEvent = _uiEvent.asSharedFlow()

    fun setEvent(event: Event) {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiEvent.emit(event)
        }
    }

    protected abstract fun handleEvent(event: Event)

    // Initialisation -----------------------------------------------------------------------------

    private lateinit var dispatcherProvider: DispatcherProvider

    protected fun initViewModel(dispatcherProvider: DispatcherProvider) {
        this.dispatcherProvider = dispatcherProvider
        initEvent()
    }

    private fun initEvent() {
        viewModelScope.launch(dispatcherProvider.main) {
            uiEvent.collect {
                handleEvent(it)
            }
        }
    }

}













