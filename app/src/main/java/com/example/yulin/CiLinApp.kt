package com.example.yulin

import android.app.Application
import com.example.yulin.network.TokenManager
import com.example.yulin.utils.ToastUtils

class CiLinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
        ToastUtils.init(this)
    }
}
