package com.main.pawtroller_psm.Models

import java.sql.Timestamp

data class Posts(
    val id: String,
    val title: String,
    val content: String,
    val owner_id: Int,
    val category_id: Int,
    val timestamps: Timestamp
)
