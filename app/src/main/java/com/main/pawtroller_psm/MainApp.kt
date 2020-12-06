package com.main.pawtroller_psm

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.main.pawtroller_psm.Models.Pet
import com.main.pawtroller_psm.Models.User
import kotlinx.android.synthetic.main.activity_main_app.*
import kotlinx.android.synthetic.main.fragment_pet_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainApp : AppCompatActivity(), Communicator{

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
        val fForum = ForumListView()
        val fProfile = UserProfile()

        mostrarFragment(fHome,userString)

        ocultarTeclado()

        tabOptions.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.action_home->{mostrarFragment(fHome,userString)}
                R.id.action_pets->{mostrarPetProfile(fPets,userString, user)}
                R.id.action_forum->{mostrarFragment(fForum,userString)}
                R.id.action_profile->{mostrarPetProfile(fProfile,userString,user)}
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
                if(respuesta!!.get(0).size<1) {
                    Toast.makeText(this@MainApp, "Aun no has registrado ninguna mascota", Toast.LENGTH_LONG).show()
                }
                val bundle = Bundle()
                var gson = Gson()
                var listaMascotaUsuario: List<Pet> = respuesta!![0]
                var listaMascotaUsuarioSting = gson.toJson(listaMascotaUsuario)
                bundle.putString("userString", userString)
                bundle.putString("listaMascotaUsuarioSting", listaMascotaUsuarioSting)
                frag.arguments = bundle

                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame, frag)
                transaction.commit()
            }

            override fun onFailure(call: Call<List<List<Pet>>>, t: Throwable) {
                Toast.makeText(this@MainApp, t.message, Toast.LENGTH_LONG).show()
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