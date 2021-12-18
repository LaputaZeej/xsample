package com.laputa.zeej.std_0006_android.compose

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SearchViewModel : ViewModel() {

    private val _searchResult: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val searchResult: Flow<List<String>> = _searchResult

}