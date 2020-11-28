package com.main.pawtroller_psm.Models

data class User(
    val id:String,
    val name:String,
    var email:String,
    val nickname:String,
    val email_verified_at:String,
    val password:String,
    val rememberToken:String,
    val birthdate:String,
    val avatarImg:String,
    val bannerImg:String,
    val timestamps:String
)

data class UserLogin(
    val email: String,
    val password: String
)

data class ResponseLogin(
    val message: String,
    val user: List<User>
)