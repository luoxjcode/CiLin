package com.example.cilin.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cilin.network.RetrofitClient
import com.example.cilin.network.models.CategoryNode
import com.example.cilin.network.models.WordItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        fetchCategoryTree()
    }

    private fun fetchCategoryTree() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = RetrofitClient.apiService.getCategoryTree()
                if (response.code == 200 && response.data != null) {
                    _uiState.value = _uiState.value.copy(
                        categoryTree = response.data,
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

    fun fetchWordList(categoryId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isWordsLoading = true)
            try {
                val response = RetrofitClient.apiService.getWordList(categoryId)
                if (response.code == 200 && response.data != null) {
                    _uiState.value = _uiState.value.copy(
                        words = response.data,
                        isWordsLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isWordsLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isWordsLoading = false)
            }
        }
    }

    fun navigateTo(level: Int, path: List<CategoryNode>) {
        _uiState.value = _uiState.value.copy(
            catLevel = level,
            catPath = path
        )
        // If it's a leaf node (level 3), fetch words
        if (level == 3 && path.isNotEmpty()) {
            fetchWordList(path.last().id)
        }
    }

    fun goBack() {
        if (_uiState.value.catLevel > 0) {
            val newLevel = _uiState.value.catLevel - 1
            val newPath = _uiState.value.catPath.take(newLevel)
            navigateTo(newLevel, newPath)
        }
    }

    fun reset(refresh: Boolean = false) {
        _uiState.value = _uiState.value.copy(
            catLevel = 0,
            catPath = emptyList(),
            words = emptyList()
        )
        if (refresh) {
            fetchCategoryTree()
        }
    }
}

data class CategoriesUiState(
    val categoryTree: List<CategoryNode> = emptyList(),
    val words: List<WordItem> = emptyList(),
    val isLoading: Boolean = false,
    val isWordsLoading: Boolean = false,
    val catLevel: Int = 0,
    val catPath: List<CategoryNode> = emptyList()
)
