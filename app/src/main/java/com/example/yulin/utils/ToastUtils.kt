package com.example.yulin.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

object ToastUtils {
    private var appContext: Context? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun showShort(message: String?) {
        if (message.isNullOrBlank()) return
        show(message, Toast.LENGTH_SHORT)
    }

    fun showLong(message: String?) {
        if (message.isNullOrBlank()) return
        show(message, Toast.LENGTH_LONG)
    }

    private fun show(message: String, duration: Int) {
        appContext?.let { context ->
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Toast.makeText(context, message, duration).show()
            } else {
                mainHandler.post {
                    Toast.makeText(context, message, duration).show()
                }
            }
        }
    }
}
