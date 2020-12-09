package com.main.pawtroller_psm

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.main.pawtroller_psm.Adapters.PostRecyclerAdapter
import com.main.pawtroller_psm.Models.Comment
import com.main.pawtroller_psm.datos.DatosMascota
import com.main.pawtroller_psm.datos.DatosUsuario
import com.main.pawtroller_psm.models.Pet
import com.main.pawtroller_psm.models.Post
import com.main.pawtroller_psm.models.ResponseEstatusPet
import com.main.pawtroller_psm.models.TipoMascota
import kotlinx.android.synthetic.main.activity_main_app.*
import kotlinx.android.synthetic.main.fragment_pet_profile.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainApp : AppCompatActivity(), Communicator,
    PostRecyclerAdapter.PostRecyclerClickEventHandler {

    var datosUsuario = DatosUsuario()
    var datosMascota = DatosMascota()

    var listaEstatusPet = listOf("bien", "perdido", "fallecido")
    var listaMascotaEstatusPerdido: List<ResponseEstatusPet> = listOf()
    var listaMascotaEstatusFallecida: List<ResponseEstatusPet> = listOf()

    var listaEstatusPetString: String = "[]"
    var listaMascotaEstatusPerdidoString: String = "[]"
    var listaMascotaEstatusfallecidaString: String = "[]"

    val fHome = NewsFeed()
    val fPets = PetProfile()
    val fForum = PostListview()
    val fProfile = UserProfile()

    var fragmentActual:Fragment = NewsFeed()
    val loadingDialog = LoadingDialog(this@MainApp)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_app)
        supportActionBar?.hide()

        loadingDialog.startLoadingdialog()
        val timer2 = Timer()
        timer2.schedule(object : TimerTask() {
            override fun run() {
                loadingDialog.dismissDialog()
                timer2.cancel() //this will cancel the timer of the system
            }
        }, 3000)

        datosUsuario.obtenerDatosActivity(intent)


        if (datosUsuario.userString != null) {
            adminTabBar()
        }
    }


    fun adminTabBar(){
       // mostrarPetProfile(fHome)

        ocultarTeclado()

        tabOptions.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.action_home -> {
                    mostrarFragment(fHome)
                }
                R.id.action_forum->{mostrarPostListview(fForum, datosUsuario.userString!!)}
                R.id.action_pets -> {
                    mostrarFragment(fPets)
                }
                R.id.action_profile -> {
                    mostrarFragment(fProfile)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    fun mostrarPetProfile(frag: Fragment){

        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<List<Pet>>> = service.consultaMascotasPorUsuario(datosUsuario.user!!.id.toInt())

        result.enqueue(object : Callback<List<List<Pet>>> {
            override fun onResponse(
                call: Call<List<List<Pet>>>,
                response: Response<List<List<Pet>>>
            ) {
                val respuesta = response.body()
                if (respuesta!!.get(0).size < 1) {
                    Toast.makeText(
                        this@MainApp,
                        "Aun no has registrado ninguna mascota",
                        Toast.LENGTH_LONG
                    ).show()
                }

                var gson = Gson()
                datosMascota.listaMascotaUsuario = respuesta!![0]
                datosMascota.listaMascotaUsuarioString = gson.toJson(datosMascota.listaMascotaUsuario)

                obtenerMascotasPerdidas(frag)
            }

            override fun onFailure(call: Call<List<List<Pet>>>, t: Throwable) {
                Toast.makeText(this@MainApp, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun obtenerMascotasPerdidas(frag: Fragment) {

        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<List<ResponseEstatusPet>>> = service.consultarPetsPorEstatus("perdido")
        var arrayMascotasPerdidas: List<List<ResponseEstatusPet>> = listOf()

        result.enqueue(object : Callback<List<List<ResponseEstatusPet>>> {
            override fun onResponse(
                call: Call<List<List<ResponseEstatusPet>>>,
                response: Response<List<List<ResponseEstatusPet>>>
            ) {
                if (response.isSuccessful) {
                    arrayMascotasPerdidas = response.body()!!
                    listaMascotaEstatusPerdido = arrayMascotasPerdidas[0]
                    var gson = Gson()
                    listaMascotaEstatusPerdidoString = gson.toJson(listaMascotaEstatusPerdido)
                    obtenerMascotasFallecidas(frag)
                } else {
                    obtenerMascotasFallecidas(frag)
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            this@MainApp,
                            jObjError.getJSONObject("errors").toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@MainApp,
                            e.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<List<ResponseEstatusPet>>>, t: Throwable) {
                Toast.makeText(this@MainApp, "Error" + t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun obtenerMascotasFallecidas(frag: Fragment) {
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<List<ResponseEstatusPet>>> = service.consultarPetsPorEstatus("fallecido")
        var arrayMascotasFallecidas: List<List<ResponseEstatusPet>> = listOf()

        result.enqueue(object : Callback<List<List<ResponseEstatusPet>>> {
            override fun onResponse(
                call: Call<List<List<ResponseEstatusPet>>>,
                response: Response<List<List<ResponseEstatusPet>>>
            ) {
                if (response.isSuccessful) {
                    arrayMascotasFallecidas = response.body()!!
                    listaMascotaEstatusFallecida = arrayMascotasFallecidas[0]
                    var gson = Gson()
                    listaMascotaEstatusfallecidaString = gson.toJson(listaMascotaEstatusFallecida)
                    obtenerTiposPets(frag)
                } else {
                    obtenerTiposPets(frag)
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            this@MainApp,
                            jObjError.getJSONObject("errors").toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@MainApp,
                            e.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<List<ResponseEstatusPet>>>, t: Throwable) {
                Toast.makeText(this@MainApp, "Error" + t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun obtenerTiposPets(frag: Fragment){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<List<TipoMascota>>> = service.obtenerMascotas()
        var arrayMascotas: List<List<TipoMascota>> = listOf()

        result.enqueue(object : Callback<List<List<TipoMascota>>> {
            override fun onResponse(
                call: Call<List<List<TipoMascota>>>,
                response: Response<List<List<TipoMascota>>>
            ) {
                arrayMascotas = response.body()!!

                datosMascota.listaTipoMascota = arrayMascotas.get(0)


                val bundle = Bundle()
                datosUsuario.enviarDatosFragment(bundle)
                datosMascota.enviarDatosFragment(bundle)
                bundle.putString(
                    "listaMascotaEstatusPerdidoString",
                    listaMascotaEstatusPerdidoString
                )
                bundle.putString(
                    "listaMascotaEstatusfallecidaString",
                    listaMascotaEstatusfallecidaString
                )

                frag.arguments = bundle

                fragmentActual = frag


                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame, frag)
                transaction.commit()
                fragmentActual.onResume()
            }

            override fun onFailure(call: Call<List<List<TipoMascota>>>, t: Throwable) {
                Toast.makeText(this@MainApp, "Error" + t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    fun obtenerPets(){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<List<Pet>>> = service.consultaMascotasPorUsuario(datosUsuario.user!!.id.toInt())

        result.enqueue(object : Callback<List<List<Pet>>> {
            override fun onResponse(
                call: Call<List<List<Pet>>>,
                response: Response<List<List<Pet>>>
            ) {
                val respuesta = response.body()
                if (respuesta!!.get(0).size < 1) {
                    Toast.makeText(
                        this@MainApp,
                        "Aun no has registrado ninguna mascota",
                        Toast.LENGTH_LONG
                    ).show()
                }
                var gson = Gson()
                datosMascota.listaMascotaUsuario = respuesta!![0]
                datosMascota.listaMascotaUsuarioString = gson.toJson(datosMascota.listaMascotaUsuario)
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

    fun mostrarFragment(frag: Fragment){
        obtenerPets()
        val bundle = Bundle()
        datosUsuario.enviarDatosFragment(bundle)
        datosMascota.enviarDatosFragment(bundle)
        bundle.putString("listaMascotaEstatusPerdidoString", listaMascotaEstatusPerdidoString)
        bundle.putString("listaMascotaEstatusfallecidaString", listaMascotaEstatusfallecidaString)
        frag.arguments = bundle

        fragmentActual= frag

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, frag)
        transaction.commit()
    }

    override fun passDataCom(userString: String) {
        val bundle = Bundle()
        bundle.putString("message", userString)

    }

    override fun ShowPostFragment(holder: View, postId:Int, userString: String) {
        val service:Service = RestEngine.getRestEngine().create(Service::class.java)
        val result:Call<List<Post>> = service.getPostById(postId)

        result.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                try {
                    val respuesta = response.body()
                    var post:Post = respuesta!![0]
                    var postString = Gson().toJson(post)

                    val bundle = Bundle()
                    bundle.putString("userString", userString)
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

    override fun onStart() {
        super.onStart()
        mostrarPetProfile(fragmentActual!!)
    }
}
