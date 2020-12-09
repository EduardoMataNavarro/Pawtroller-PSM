package com.main.pawtroller_psm.Models

import java.sql.Timestamp


data class Comment(
    val id:Int,
    val comment:String,
    val user_id:Int,
    val post_id:Int,
    val created_at: Timestamp,
    val updated_at: Timestamp,
    val user:User
)

data class CommentCreationData(
    val comment:String,
    val userid: Int,
    val postid: Int
)
