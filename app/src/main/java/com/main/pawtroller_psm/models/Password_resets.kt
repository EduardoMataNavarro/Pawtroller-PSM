package com.main.pawtroller_psm.models

import java.sql.Timestamp

data class Password_resets(
    val email: String,
    val token: String,
    val created_at: Timestamp
)
