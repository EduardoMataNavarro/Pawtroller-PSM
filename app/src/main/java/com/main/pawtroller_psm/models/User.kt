package com.main.pawtroller_psm.models

data class User(
    val id:String,
    val name:String,
    var email:String,
    val nickname:String,
    val email_verified_at:String,
    val created_at:String,
    val updated_at:String,
    val password:String,
    val password_confirmation:String,
    val rememberToken:String,
    val birthdate:String,
    val avatar_pic_path:String,
    val banner_pic_path:String,
    val profile_photo_url:String,
    val api_token:String,
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

data class UserRegister(
    val name:String,
    var email:String,
    val password:String,
    val password_confirmation:String,
    val birthdate:String
)

data class ResetPasswordUser(
    val password:String,
    val password_confirmation:String,
    val user_id: String
)

data class ResponseRegister(
    val id:String,
    val name:String,
    var email:String,
    val email_verified_at:String,
    val created_at:String,
    val updated_at:String,
    val birthdate:String,
    val avatar_pic_path:String,
    val banner_pic_path:String,
    val profile_photo_url:String,
    val api_token:String
)