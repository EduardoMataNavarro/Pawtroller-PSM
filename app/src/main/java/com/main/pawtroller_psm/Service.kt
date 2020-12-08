package com.main.pawtroller_psm

import com.main.pawtroller_psm.Models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Service {

    @Headers("Accept:application/json")
    @POST("api/loginuser")
    fun login(@Body userLogin: UserLogin): Call<ResponseLogin>

    @Headers("Accept:application/json")
    @POST("api/signupuser")
    fun registrar(@Body userResgister: UserRegister): Call <List<User>>

    @Headers("Accept:application/json")
    @POST("api/resetpassword")
    fun resetPassword(@Body resetPasswordUser: ResetPasswordUser ): Call <ResponseLogin>

    @Headers("Accept:application/json")
    @Multipart
    @POST("api/usermedia")
    fun cambiarMediaUsuario(@Part media: MultipartBody.Part, @Part ("type") type : RequestBody, @Part ("user_id") user_id: RequestBody): Call<ResponseLogin>

    @GET("api/pet_type")
    fun obtenerMascotas(): Call<List<List<TipoMascota>>>

    @Multipart
    @POST("api/pet/create")
    fun agregarMascotas(@Part petImg: MultipartBody.Part, @Part ("name") name: RequestBody, @Part ("nickname") nickname: RequestBody, @Part ("description") description: RequestBody,
                        @Part ("userid") userId: RequestBody,@Part ("birthdate") birthdate: RequestBody,@Part ("age") age: RequestBody,
                        @Part ("pettype") pettype: RequestBody): Call<List<Pet>>

    @Headers("Accept:application/json")
    @Multipart
    @POST("api/pet/media")
    fun agregarMediaPet(@Part media: MultipartBody.Part, @Part ("type") type : RequestBody, @Part ("pet_id") user_id: RequestBody): Call<ResponsePetMedia>

    @GET("api/pet/user/{id}")
    fun consultaMascotasPorUsuario(@Path("id")id: Int): Call<List<List<Pet>>>

    @GET("api/pet/{id}/media")
    fun consultaImagenPorMascota(@Path("id")id: Int): Call<List<List<Pet_media>>>

    @Headers("Accept:application/json")
    @GET("api/pet/status/{estatus}")
    fun consultarPetsPorEstatus(@Path("estatus") estatus: String): Call<List<List<ResponseEstatusPet>>>

    @Headers("Accept:application/json")
    @PUT("api/pet/status")
    fun cambiarStatus(@Body estatusPet: EstatusPet):  Call<ResponseEstatusPet>

}