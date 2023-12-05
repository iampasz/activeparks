package com.app.activeparks.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TestEventViewModel(


) : ViewModel() {




    init {
        loadEventState()
    }

    fun loadEventState() {
        viewModelScope.launch {

        }
    }

    fun saveEventState() {
        viewModelScope.launch {

        }
    }

}