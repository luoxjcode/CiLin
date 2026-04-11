package com.example.cilin.network

import android.util.Log
import com.example.cilin.utils.ToastUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Android emulator local host IP. If running on physical device, change to actual PC IP.
    private const val BASE_URL = "http://127.0.0.1:82/"

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()
        
        // Add token header if available
        TokenManager.getToken()?.let { token ->
            builder.header("token", token)
            builder.header("Cookie", "token=$token")
        }
        
        chain.proceed(builder.build())
    }

    private val responseInterceptor = Interceptor { chain ->
        val request = chain.request()
        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            ToastUtils.showShort("网络异常: ${e.message}")
            throw e
        }

        if (!response.isSuccessful) {
            ToastUtils.showShort("服务器异常: ${response.code}")
        } else {
            // Peek at the body to show msg if it exists
            val responseBody = response.peekBody(Long.MAX_VALUE)
            val content = responseBody.string()
            try {
                val jsonObject = JSONObject(content)
                if (jsonObject.has("msg")) {
                    val msg = jsonObject.getString("msg")
                    val code = jsonObject.optInt("code", -1)
                    
                    // Show message for non-silent codes or if msg is not empty
                    // Usually code 200 is success, but we might want to show success msg too
                    if (code != 200 && msg.isNotBlank()) {
                        ToastUtils.showShort(msg)
                    } else if (code == 200 && msg.isNotBlank() && (request.method == "POST" || request.url.encodedPath.contains("login", true))) {
                        // For POST requests or login, show success message
                        ToastUtils.showShort(msg)
                    }
                }
            } catch (e: Exception) {
                // Not a JSON or other error, ignore
            }
        }
        response
    }

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d("OkHttp", message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(responseInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
