package com.example.cilin.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cilin.network.RetrofitClient
import com.example.cilin.network.TokenManager
import com.example.cilin.network.models.LoginRequest
import com.example.cilin.network.models.UserInfo
import com.example.cilin.utils.ToastUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var countdownJob: Job? = null

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            try {
                if (TokenManager.getToken() != null) {
                    val response = RetrofitClient.apiService.isLogin()
                    if (response.code == 200 && response.data == true) {
                        fetchUserInfo()
                    } else {
                        TokenManager.clearToken()
                        _uiState.value = _uiState.value.copy(isLoggedIn = false)
                    }
                } else {
                    _uiState.value = _uiState.value.copy(isLoggedIn = false)
                }
            } catch (e: Exception) {
                // Interceptor already shows network error
                _uiState.value = _uiState.value.copy(isLoggedIn = false)
            }
        }
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getLoginUser()
                if (response.code == 200 && response.data != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoggedIn = true,
                        userInfo = response.data
                    )
                    // Login successful, stop countdown
                    stopCountdown()
                }
            } catch (e: Exception) {
                // Interceptor handles it
            }
        }
    }

    fun getVerificationCode(email: String) {
        if (email.isBlank()) {
            ToastUtils.showShort("请输入邮箱")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = RetrofitClient.apiService.getEmailValidCode(email = email)
                if (response.code == 200 && response.data != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        validCodeReqNo = response.data,
                        showCodeInput = true
                    )
                    startCountdown()
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun startCountdown() {
        stopCountdown()
        countdownJob = viewModelScope.launch {
            for (i in 60 downTo 0) {
                _uiState.value = _uiState.value.copy(countdown = i)
                delay(1000)
            }
        }
    }

    private fun stopCountdown() {
        countdownJob?.cancel()
        countdownJob = null
        _uiState.value = _uiState.value.copy(countdown = 0)
    }

    fun login(email: String, validCode: String) {
        val reqNo = _uiState.value.validCodeReqNo
        if (reqNo == null) {
            ToastUtils.showShort("请先获取验证码")
            return
        }
        if (validCode.isBlank()) {
            ToastUtils.showShort("请输入验证码")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val request = LoginRequest(
                    email = email,
                    validCode = validCode,
                    validCodeReqNo = reqNo
                )
                val response = RetrofitClient.apiService.doLoginByEmail(request)
                if (response.code == 200 && response.data != null) {
                    // Save token
                    TokenManager.saveToken(response.data)
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    fetchUserInfo()
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                // Call server logout API
                RetrofitClient.apiService.doLogout()
            } catch (e: Exception) {
                // Ignore logout error as we want to clear local state anyway
            } finally {
                // Always clear local state
                TokenManager.clearToken()
                stopCountdown()
                _uiState.value = ProfileUiState()
                ToastUtils.showShort("已退出登录")
            }
        }
    }
}

data class ProfileUiState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val showCodeInput: Boolean = false,
    val validCodeReqNo: String? = null,
    val countdown: Int = 0,
    val userInfo: UserInfo? = null
)
