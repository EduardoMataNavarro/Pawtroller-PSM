package com.main.pawtroller_psm

import com.main.pawtroller_psm.Models.ResponseLogin
import com.main.pawtroller_psm.Models.UserLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Service {

    @POST("api/loginuser")
    fun login(@Body userLogin: UserLogin): Call<ResponseLogin>

}