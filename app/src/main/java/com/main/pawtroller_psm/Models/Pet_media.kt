package com.main.pawtroller_psm.Models

import java.sql.Timestamp

data class Pet_media(
    val id: Int,
    val type: String,
    val path: String,
    val pet_id: Int,
    val timestamps: Timestamp
)
