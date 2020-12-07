package com.main.pawtroller_psm

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.main.pawtroller_psm.Models.Pet
import com.main.pawtroller_psm.Models.TipoMascota
import com.main.pawtroller_psm.Models.User
import kotlinx.android.synthetic.main.activity_main_app.*
import kotlinx.android.synthetic.main.fragment_pet_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainApp : AppCompatActivity(), Communicator{

    var user: User ?=null
    var listaMascotaUsuario : List<Pet> = listOf()
    var listaTipoMascota: List<TipoMascota> = listOf()
    var listaEstatusPet = listOf("bien","perdido","fallecido")

    var userString: String ?= null
    var listaMascotaUsuarioString: String ?= null
    var listaTipoMascotaString:  String ?= null
    var listaEstatusPetString: String ?= null

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
        val fHome = NewsFeed()
        val fPets = PetProfile()
        val fForum = ForumListView()
        val fProfile = UserProfile()

        mostrarPetProfile(fHome)

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

                obtenerTiposPets(frag)
                //listaEstatusPet = obtenerEstatusPets()


            }

            override fun onFailure(call: Call<List<List<Pet>>>, t: Throwable) {
                Toast.makeText(this@MainApp, t.message, Toast.LENGTH_LONG).show()
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

                frag.arguments = bundle

                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame, frag)
                transaction.commit()
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
        frag.arguments = bundle

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
}