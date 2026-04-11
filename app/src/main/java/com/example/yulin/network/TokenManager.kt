package com.example.yulin.network

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_TOKEN = "auth_token"
    
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
        prefs?.edit()?.putString(KEY_TOKEN, token)?.apply()
    }

    fun getToken(): String? {
        return prefs?.getString(KEY_TOKEN, null)
    }

    fun clearToken() {
        prefs?.edit()?.remove(KEY_TOKEN)?.apply()
    }
}
