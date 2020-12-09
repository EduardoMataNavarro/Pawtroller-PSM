package com.main.pawtroller_psm

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.main.pawtroller_psm.LocalDatabase.InitDatabase
import com.main.pawtroller_psm.Models.PostCategory
import com.main.pawtroller_psm.Models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreatePostActivity : AppCompatActivity(){
    companion object {
        lateinit var CONTEXT:Context
        var DB = InitDatabase()
        val DB_NAME = "pawtroller.post_drafts"
        val VERSION = 1
        val TB_NAME = "postDraft"
    }

    var userData:User? = null
    var createPostFragment = CreatePost()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CONTEXT = applicationContext

        var intentData: Intent = intent
        var userString = intentData.getStringExtra("userString")
        var userData = Gson().fromJson(userString, User::class.java)

        val service:Service = RestEngine.getRestEngine().create(Service::class.java)
        var result: Call<List<List<PostCategory>>> = service.getPostCategoriesList()
        result.enqueue(object : Callback<List<List<PostCategory>>> {

            override fun onResponse(call: Call<List<List<PostCategory>>>, response: Response<List<List<PostCategory>>>) {
                try {
                    val respuesta = response.body()
                    val categoriesList:List<PostCategory> = respuesta!![0]
                    val categoriesListString = Gson().toJson(categoriesList)
                    val userString:String = Gson().toJson(userData)

                    var bundle = Bundle()
                    bundle.putString("categoriesList", categoriesListString)
                    bundle.putString("userString", userString)
                    createPostFragment.arguments = bundle

                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frame, createPostFragment)
                    transaction.commit()
                } catch (err:Error) {
                    Toast.makeText(baseContext, "Err: ${err.message.toString()}", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<List<List<PostCategory>>>, t: Throwable) {

            }

        })
    }
}