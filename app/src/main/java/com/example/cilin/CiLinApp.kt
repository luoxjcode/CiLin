package com.example.cilin

import android.app.Application
import com.example.cilin.network.TokenManager
import com.example.cilin.utils.ToastUtils

class CiLinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
        ToastUtils.init(this)
    }
}
