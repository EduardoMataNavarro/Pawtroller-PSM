package com.main.pawtroller_psm

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.main.pawtroller_psm.Models.User
import kotlinx.android.synthetic.main.activity_main_app.*

class MainApp : AppCompatActivity(), Communicator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_app)
        supportActionBar?.hide()
        val datos: Intent = intent
        var userString = datos.getStringExtra("userString")
        var gson = Gson()
        var user = gson.fromJson(userString, User::class.java)

        if (userString != null) {
            adminTabBar(userString)
        }
    }

    fun adminTabBar(userString:String){
        val fHome = NewsFeed()
        val fPets = PetProfile()
        val fForum = ForumListView()
        val fProfile = UserProfile()

        mostrarFragment(fHome,userString)

        ocultarTeclado()

        tabOptions.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.action_home->{mostrarFragment(fHome,userString)}
                R.id.action_pets->{mostrarFragment(fPets,userString)}
                R.id.action_forum->{mostrarFragment(fForum,userString)}
                R.id.action_profile->{mostrarFragment(fProfile,userString)}
            }
            return@setOnNavigationItemSelectedListener true
        }
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