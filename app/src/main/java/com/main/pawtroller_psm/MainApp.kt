package com.main.pawtroller_psm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.main.pawtroller_psm.Adapters.PostRecyclerAdapter
import com.main.pawtroller_psm.Models.Comment
import com.main.pawtroller_psm.Models.Pet
import com.main.pawtroller_psm.Models.Post
import com.main.pawtroller_psm.Models.User
import kotlinx.android.synthetic.main.activity_main_app.*
import kotlinx.android.synthetic.main.fragment_pet_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.lang.Error
import kotlin.math.log

class MainApp : AppCompatActivity(), Communicator,
    PostRecyclerAdapter.PostRecyclerClickEventHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_app)
        supportActionBar?.hide()


        val datos: Intent = intent
        var userString = datos.getStringExtra("userString")
        var gson = Gson()
        var user = gson.fromJson(userString, User::class.java)



        if (userString != null) {
            adminTabBar(userString,user)
        }
    }

    fun adminTabBar(userString:String,user:User){
        val fHome = NewsFeed()
        val fPets = PetProfile()
        val fForum = PostListview()
        val fProfile = UserProfile()

        mostrarFragment(fHome,userString)

        ocultarTeclado()

        tabOptions.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.action_home->{mostrarFragment(fHome,userString)}
                R.id.action_pets->{mostrarPetProfile(fPets,userString, user)}
                R.id.action_forum->{mostrarPostListview(fForum,userString)}
                R.id.action_profile->{mostrarFragment(fProfile,userString)}
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    fun mostrarPetProfile(frag:Fragment,userString: String, user: User){

        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<List<Pet>>> = service.consultaMascotasPorUsuario(user.id.toInt())

        result.enqueue(object : Callback<List<List<Pet>>> {
            override fun onResponse(call: Call<List<List<Pet>>>,response: Response<List<List<Pet>>>) {
                val respuesta = response.body()
                val id: String = respuesta!!.get(0).get(0).id.toString()
                if (id != null) {
                    val bundle = Bundle()
                    var gson = Gson()
                    var listaMascotaUsuario: List<Pet> = respuesta!![0]
                    var listaMascotaUsuarioSting = gson.toJson(listaMascotaUsuario)
                    bundle.putString("userString",userString)
                    bundle.putString("listaMascotaUsuarioSting",listaMascotaUsuarioSting)
                    frag.arguments = bundle

                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frame, frag)
                    transaction.commit()
                } else {
                    Toast.makeText(this@MainApp,"Ocurrio un error, ",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<List<Pet>>>, t: Throwable) {
                Toast.makeText(this@MainApp, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun mostrarPostListview(frag:Fragment, userString:String){
        val service:Service = RestEngine.getRestEngine().create(Service::class.java)
        val result:Call<List<List<Post>>> = service.getPostsList()

        result.enqueue(object:Callback<List<List<Post>>> {
            override fun onResponse(call:Call<List<List<Post>>>, response:Response<List<List<Post>>>) {
                try {
                    val respuesta = response.body()
                    val bundle = Bundle()
                    var postsList:List<Post> = respuesta!![0]
                    var postsListString = Gson().toJson(postsList)
                    bundle.putString("userString",userString)
                    bundle.putString("PostsList", postsListString)
                    frag.arguments = bundle

                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frame, frag)
                    transaction.commit()
                } catch (err:Error){
                    Toast.makeText(this@MainApp, err.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<List<Post>>>, t: Throwable) {
                Toast.makeText(this@MainApp, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun mostrarFragment(frag:Fragment,userString: String){
        val bundle = Bundle()
        bundle.putString("userString",userString)
        frag.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, frag)
        transaction.commit()
    }

    override fun passDataCom(userString:String) {
        val bundle = Bundle()
        bundle.putString("message",userString)

    }

    override fun ShowPostFragment(holder: View, postId:Int) {
        val service:Service = RestEngine.getRestEngine().create(Service::class.java)
        val result:Call<List<Post>> = service.getPostById(postId)

        result.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                try {
                    val respuesta = response.body()
                    var post:Post = respuesta!![0]
                    var postString = Gson().toJson(post)

                    val bundle = Bundle()
                    bundle.putString("PostData", postString)

                    val commentService:Service = RestEngine.getRestEngine().create(Service::class.java)
                    val commentsResult:Call<List<List<Comment>>> = commentService.getPostComments(postId)

                    commentsResult.enqueue(object:Callback<List<List<Comment>>> {
                        override fun onResponse(call: Call<List<List<Comment>>>,response: Response<List<List<Comment>>>) {
                            var commentsResponse = response.body()
                            var comments: List<Comment> = commentsResponse!![0]
                            var commentsList = Gson().toJson(comments)
                            bundle.putString("CommentsList", commentsList)

                            val postDetailView = PostDetailview()
                            postDetailView.arguments = bundle

                            val transaction = supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.frame, postDetailView)
                            transaction.commit()
                            transaction.addToBackStack(null)
                        }

                        override fun onFailure(call: Call<List<List<Comment>>>, t: Throwable) {
                            Toast.makeText(this@MainApp, t.message, Toast.LENGTH_SHORT).show()
                        }
                    })
                } catch (err:Error){
                    Log.println(Log.ERROR, "comentarios y posts ", err.message.toString())
                }
            }
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Toast.makeText(this@MainApp, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    fun ocultarTeclado(){
        tabOptions?.setOnFocusChangeListener{ view, hasFocus->
            if(hasFocus) hideSoftKeyboard()
        }
    }

    fun hideSoftKeyboard() {
        val inputMethodManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }

}