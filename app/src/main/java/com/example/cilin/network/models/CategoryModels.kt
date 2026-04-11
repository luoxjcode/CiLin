package com.example.cilin.network.models

import com.google.gson.annotations.SerializedName

// 分类节点模型
data class CategoryNode(
    val id: String,
    val name: String,
    val level: Int,
    val children: List<CategoryNode>? = null
)

// 词语模型
data class WordItem(
    val id: String,
    val word: String,
    val pinyin: String?,
    val definition: String?,
    val example: String?,
    val categoryId: String
)
