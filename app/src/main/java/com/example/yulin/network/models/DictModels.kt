package com.example.yulin.network.models

import com.google.gson.annotations.SerializedName

/**
 * 词语列表项
 */
data class DictWordItem(
    val id: Int,
    val word: String,
    val pinyin: String?,
    val abbreviation: String?,
    val explanation: String?
)

/**
 * 词语详情
 */
data class DictWordDetail(
    val id: Int,
    val hierarchyId: String?,
    val word: String,
    val pinyin: String?,
    val abbreviation: String?,
    val explanation: String?,
    val derivation: String?,
    val exampleList: List<DictExample>?
)

/**
 * 词语例句
 */
data class DictExample(
    val id: Int,
    val entryId: Int,
    val content: String,
    val sortOrder: Int,
    val createTime: String
)
