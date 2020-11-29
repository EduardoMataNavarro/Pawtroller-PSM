package com.main.pawtroller_psm

import com.main.pawtroller_psm.Models.ResponseLogin
import com.main.pawtroller_psm.Models.User
import com.main.pawtroller_psm.Models.UserLogin
import com.main.pawtroller_psm.Models.UserRegister
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Service {

    @POST("api/loginuser")
    fun login(@Body userLogin: UserLogin): Call<ResponseLogin>

    @POST("api/signupuser")
    fun registrar(@Body userResgister: UserRegister): Call <List<User>>
}