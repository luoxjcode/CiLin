package com.example.yulin.network

import com.example.yulin.network.models.ApiResponse
import com.example.yulin.network.models.CategoryNode
import com.example.yulin.network.models.DictWordDetail
import com.example.yulin.network.models.DictWordItem
import com.example.yulin.network.models.LoginRequest
import com.example.yulin.network.models.UserInfo
import com.example.yulin.network.models.WordItem
import com.example.yulin.network.models.QuizQuestion
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    // 接口1：获取邮箱验证码
    @GET("auth/c/getEmailValidCode")
    suspend fun getEmailValidCode(
        @Query("email") email: String,
        @Query("validCode") validCode: String = "email",
        @Query("validCodeReqNo") validCodeReqNo: String = "email"
    ): ApiResponse<String>

    // 接口2：邮箱验证码登录
    @POST("auth/c/doLoginByEmail")
    suspend fun doLoginByEmail(@Body request: LoginRequest): ApiResponse<String>

    // 接口3：获取用户信息
    @GET("auth/c/getLoginUser")
    suspend fun getLoginUser(): ApiResponse<UserInfo>

    // 接口4：判断是否登录
    @GET("auth/c/isLogin")
    suspend fun isLogin(): ApiResponse<Boolean>

    // 接口5：退出登录
    @GET("auth/c/doLogout")
    suspend fun doLogout(): ApiResponse<Unit>

    // 接口6：获取分类树
    @GET("category/tree")
    suspend fun getCategoryTree(): ApiResponse<List<CategoryNode>>

    // 接口7：获取词语列表
    @GET("word/list")
    suspend fun getWordList(@Query("categoryId") categoryId: String): ApiResponse<List<WordItem>>

    // 接口8：词典查询词语列表
    @GET("dict/list")
    suspend fun searchDictWords(@Query("searchKey") searchKey: String): ApiResponse<List<DictWordItem>>

    // 接口9：获取词语详情
    @GET("dict/detail")
    suspend fun getDictWordDetail(@Query("id") id: Int): ApiResponse<DictWordDetail>

    // 接口10：获取每日题目（10个）
    @GET("quiz/daily")
    suspend fun getDailyQuiz(): ApiResponse<List<QuizQuestion>>
}