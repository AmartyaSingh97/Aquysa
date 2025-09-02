package com.amartya.aquysa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewmodel: ViewModel() {
    private val splashShowFlow = MutableStateFlow(true)

    val isSplashShow = splashShowFlow.asStateFlow()

    init {
        viewModelScope.launch {
            delay(500)
            splashShowFlow.value = false
        }
    }
}