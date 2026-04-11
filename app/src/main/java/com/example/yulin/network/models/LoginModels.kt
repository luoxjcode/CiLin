package com.example.yulin.network.models

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    val code: Int,
    val msg: String,
    val data: T?,
    val traceId: String?
)

data class LoginRequest(
    val email: String,
    val validCode: String,
    val validCodeReqNo: String,
    val device: String = "APP"
)

data class UserInfo(
    val id: String,
    val account: String,
    val name: String,
    val email: String,
    val avatar: String?,
    val phone: String?
)
