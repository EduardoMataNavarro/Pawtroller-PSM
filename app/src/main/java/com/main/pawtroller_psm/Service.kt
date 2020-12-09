package com.main.pawtroller_psm

import com.main.pawtroller_psm.Models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Service {

    @POST("api/loginuser")
    fun login(@Body userLogin: UserLogin): Call<ResponseLogin>

    @POST("api/signupuser")
    fun registrar(@Body userResgister: UserRegister): Call <List<User>>

    @GET("api/pet_type")
    fun obtenerMascotas(): Call<List<List<TipoMascota>>>

    @POST("api/pet/create")
    fun agregarMascotas(@Body crearPet: RegistrarPet): Call<List<Pet>>

    @GET("api/pet/user/{id}")
    fun consultaMascotasPorUsuario(@Path("id")id: Int): Call<List<List<Pet>>>

    @POST("api/post/create")
    fun createPostEntry(@Body postData: PostCreationData): Call <List<Post>>

    @GET("api/post")
    fun getPostsList(): Call <List<List<Post>>>

    @GET("api/post/{id}")
    fun getPostById(@Path("id")id:Int): Call<List<Post>>

    @GET("api/post/{id}/comments")
    fun getPostComments(@Path("id")id:Int): Call<List<List<Comment>>>

    @GET("api/post_category")
    fun getPostCategoriesList(): Call<List<List<PostCategory>>>
}