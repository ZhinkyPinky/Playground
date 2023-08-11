package com.je.playground.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val placeHolder : List<Any> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application : Application
) : ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())

    val homeUiState : StateFlow<HomeUiState> get() = _homeUiState

    init {
        viewModelScope.launch {}
    }
}