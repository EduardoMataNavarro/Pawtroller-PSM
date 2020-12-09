package com.main.pawtroller_psm.Models

import android.app.Application
import android.widget.Toast
import androidx.annotation.MainThread
import com.main.pawtroller_psm.CreatePostActivity
import com.main.pawtroller_psm.LocalDatabase.Contract
import com.main.pawtroller_psm.MainApp
import okhttp3.internal.format
import java.lang.Error
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import kotlin.contracts.contract

data class PostCategory (
    val id:Int,
    val name:String,
    val description:String,
    val timestamps: Timestamp
)

data class PostCreationData(
    val id:Int,
    val title:String,
    val content:String,
    val userid:Int,
    val categoryid:Int,
    val created_at: String
)

data class Post(
    val id: String,
    val title: String,
    val content: String,
    val user_id: Int,
    val category_id: Int,
    val created_at: Timestamp,
    val user: User
)
