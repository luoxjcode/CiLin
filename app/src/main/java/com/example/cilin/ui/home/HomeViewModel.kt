package com.example.cilin.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cilin.network.RetrofitClient
import com.example.cilin.network.models.QuizQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var lastFetchDate: String = ""

    init {
        fetchDailyQuiz()
    }

    fun fetchDailyQuiz() {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        
        // Only fetch if date has changed or list is empty
        if (currentDate == lastFetchDate && _uiState.value.questions.isNotEmpty()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val response = RetrofitClient.apiService.getDailyQuiz()
                if (response.code == 200 && response.data != null) {
                    lastFetchDate = currentDate
                    _uiState.value = _uiState.value.copy(
                        questions = response.data,
                        isLoading = false,
                        currentIndex = 0,
                        answers = IntArray(response.data.size) { -1 } // -1 means no answer
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "获取题目失败: ${response.msg}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "网络错误: ${e.message}"
                )
            }
        }
    }

    fun selectOption(optionIndex: Int) {
        val state = _uiState.value
        if (state.currentIndex < 0 || state.currentIndex >= state.questions.size) return
        
        val newAnswers = state.answers.copyOf()
        newAnswers[state.currentIndex] = optionIndex
        _uiState.value = state.copy(answers = newAnswers)
    }

    fun nextQuestion() {
        val state = _uiState.value
        if (state.currentIndex < state.questions.size - 1) {
            _uiState.value = state.copy(currentIndex = state.currentIndex + 1)
        }
    }

    fun previousQuestion() {
        val state = _uiState.value
        if (state.currentIndex > 0) {
            _uiState.value = state.copy(currentIndex = state.currentIndex - 1)
        }
    }
}

data class HomeUiState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = -1,
    val answers: IntArray = intArrayOf(), // Stores selected option index for each question
    val isLoading: Boolean = false,
    val error: String? = null
) {
    // Custom equals/hashCode because of IntArray
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as HomeUiState
        if (questions != other.questions) return false
        if (currentIndex != other.currentIndex) return false
        if (!answers.contentEquals(other.answers)) return false
        if (isLoading != other.isLoading) return false
        if (error != other.error) return false
        return true
    }

    override fun hashCode(): Int {
        var result = questions.hashCode()
        result = 31 * result + currentIndex
        result = 31 * result + answers.contentHashCode()
        result = 31 * result + isLoading.hashCode()
        result = 31 * result + (error?.hashCode() ?: 0)
        return result
    }
}
