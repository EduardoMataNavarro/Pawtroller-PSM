package com.main.pawtroller_psm

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.main.pawtroller_psm.Models.Pet
import com.main.pawtroller_psm.Models.ResponseEstatusPet
import com.main.pawtroller_psm.Models.TipoMascota
import com.main.pawtroller_psm.Models.User
import kotlinx.android.synthetic.main.activity_main_app.*
import kotlinx.android.synthetic.main.fragment_pet_profile.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainApp : AppCompatActivity(), Communicator{

    var user: User ?=null
    var listaMascotaUsuario : List<Pet> = listOf()
    var listaTipoMascota: List<TipoMascota> = listOf()
    var listaEstatusPet = listOf("bien","perdido","fallecido")
    var listaMascotaEstatusPerdido: List<ResponseEstatusPet> = listOf()
    var listaMascotaEstatusFallecida: List<ResponseEstatusPet> = listOf()

    var userString: String ?= null
    var listaMascotaUsuarioString: String = "[]"
    var listaTipoMascotaString:  String = "[]"
    var listaEstatusPetString: String = "[]"
    var listaMascotaEstatusPerdidoString: String = "[]"
    var listaMascotaEstatusfallecidaString: String = "[]"

    val fHome = NewsFeed()
    val fPets = PetProfile()
    val fForum = ForumListView()
    val fProfile = UserProfile()

    var fragmentActual:Fragment = NewsFeed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_app)
        supportActionBar?.hide()

        val datos: Intent = intent
        userString = datos.getStringExtra("userString")
        var gson = Gson()
        user = gson.fromJson(userString, User::class.java)

        if (userString != null) {
            adminTabBar()
        }
    }

    fun adminTabBar(){
       // mostrarPetProfile(fHome)

        ocultarTeclado()

        tabOptions.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.action_home->{mostrarFragment(fHome)}
                R.id.action_pets->{mostrarFragment(fPets)}
                R.id.action_forum->{mostrarFragment(fForum)}
                R.id.action_profile->{mostrarFragment(fProfile)}
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    fun mostrarPetProfile(frag:Fragment){

        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<List<Pet>>> = service.consultaMascotasPorUsuario(user!!.id.toInt())

        result.enqueue(object : Callback<List<List<Pet>>> {
            override fun onResponse(call: Call<List<List<Pet>>>,response: Response<List<List<Pet>>>) {
                val respuesta = response.body()
                if(respuesta!!.get(0).size<1) {
                    Toast.makeText(this@MainApp, "Aun no has registrado ninguna mascota", Toast.LENGTH_LONG).show()
                }

                var gson = Gson()
                listaMascotaUsuario = respuesta!![0]
                listaMascotaUsuarioString = gson.toJson(listaMascotaUsuario)

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
                call: Call<List<List<ResponseEstatusPet>>>,response: Response<List<List<ResponseEstatusPet>>>) {
                if (response.isSuccessful) {
                    arrayMascotasPerdidas = response.body()!!
                    listaMascotaEstatusPerdido = arrayMascotasPerdidas[0]
                    var gson = Gson()
                    listaMascotaEstatusPerdidoString = gson.toJson(listaMascotaEstatusPerdido)
                    obtenerMascotasFallecidas(frag)
                }else {
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
                call: Call<List<List<ResponseEstatusPet>>>,response: Response<List<List<ResponseEstatusPet>>>) {
                if (response.isSuccessful) {
                    arrayMascotasFallecidas = response.body()!!
                    listaMascotaEstatusFallecida = arrayMascotasFallecidas[0]
                    var gson = Gson()
                    listaMascotaEstatusfallecidaString = gson.toJson(listaMascotaEstatusFallecida)
                    obtenerTiposPets(frag)
                }else {
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
            override fun onResponse(call: Call<List<List<TipoMascota>>>, response: Response<List<List<TipoMascota>>>) {
                arrayMascotas = response.body()!!

                listaTipoMascota= arrayMascotas.get(0)

                var gson = Gson()
                listaTipoMascotaString = gson.toJson(listaTipoMascota)

                val bundle = Bundle()
                bundle.putString("userString", userString)
                bundle.putString("listaMascotaUsuarioString", listaMascotaUsuarioString)
                bundle.putString("listaTipoMascotaString",listaTipoMascotaString)
                bundle.putString("listaMascotaEstatusPerdidoString",listaMascotaEstatusPerdidoString)
                bundle.putString("listaMascotaEstatusfallecidaString",listaMascotaEstatusfallecidaString)

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
        val result: Call<List<List<Pet>>> = service.consultaMascotasPorUsuario(user!!.id.toInt())

        result.enqueue(object : Callback<List<List<Pet>>> {
            override fun onResponse(call: Call<List<List<Pet>>>,response: Response<List<List<Pet>>>) {
                val respuesta = response.body()
                if(respuesta!!.get(0).size<1) {
                    Toast.makeText(this@MainApp, "Aun no has registrado ninguna mascota", Toast.LENGTH_LONG).show()
                }
                var gson = Gson()
                listaMascotaUsuario = respuesta!![0]
                listaMascotaUsuarioString = gson.toJson(listaMascotaUsuario)
            }

            override fun onFailure(call: Call<List<List<Pet>>>, t: Throwable) {
                Toast.makeText(this@MainApp, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun mostrarFragment(frag:Fragment){
        obtenerPets()
        val bundle = Bundle()
        bundle.putString("userString",userString)
        bundle.putString("listaMascotaUsuarioString", listaMascotaUsuarioString)
        bundle.putString("listaTipoMascotaString",listaTipoMascotaString)
        bundle.putString("listaMascotaEstatusPerdidoString",listaMascotaEstatusPerdidoString)
        bundle.putString("listaMascotaEstatusfallecidaString",listaMascotaEstatusfallecidaString)
        frag.arguments = bundle

        fragmentActual= frag

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, frag)
        transaction.commit()
    }

    override fun passDataCom(userString:String) {
        val bundle = Bundle()
        bundle.putString("message",userString)

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