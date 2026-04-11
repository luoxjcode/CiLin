package com.example.yulin.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yulin.network.RetrofitClient
import com.example.yulin.network.models.DictWordDetail
import com.example.yulin.network.models.DictWordItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        // Initial search to show some random data
        searchWords("")
    }

    fun searchWords(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, searchQuery = query)
            try {
                val response = RetrofitClient.apiService.searchDictWords(query)
                if (response.code == 200 && response.data != null) {
                    _uiState.value = _uiState.value.copy(
                        searchResults = response.data,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun fetchWordDetail(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDetailLoading = true)
            try {
                val response = RetrofitClient.apiService.getDictWordDetail(id)
                if (response.code == 200 && response.data != null) {
                    _uiState.value = _uiState.value.copy(
                        selectedWordDetail = response.data,
                        isDetailLoading = false,
                        showDetail = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isDetailLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isDetailLoading = false)
            }
        }
    }

    fun dismissDetail() {
        _uiState.value = _uiState.value.copy(showDetail = false, selectedWordDetail = null)
    }
}

data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<DictWordItem> = emptyList(),
    val selectedWordDetail: DictWordDetail? = null,
    val isLoading: Boolean = false,
    val isDetailLoading: Boolean = false,
    val showDetail: Boolean = false
)
