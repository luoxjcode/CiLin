package com.example.cilin.network.models

import com.google.gson.annotations.SerializedName

/**
 * 每日题目选项模型
 * type: 0-恰当用法 1-褒贬误用 2-望文生义 3-对象误用
 */
data class QuizOption(
    val content: String,
    val type: Int // 0, 1, 2, 3
) {
    fun isCorrect() = type == 0
    
    fun getErrorReason(): String? {
        return when (type) {
            1 -> "褒贬误用"
            2 -> "望文生义"
            3 -> "对象误用"
            else -> null
        }
    }
}

/**
 * 每日题目模型
 */
data class QuizQuestion(
    val word: String,
    val pinyin: String,
    val options: List<QuizOption>
)
