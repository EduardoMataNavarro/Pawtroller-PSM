package com.main.pawtroller_psm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main_app.*

class MainApp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_app)
        supportActionBar?.hide()
        adminTabBar()
    }

    fun adminTabBar(){
        val fHome = NewsFeed()
        val fPets = PetProfile()
        val fForum = ForumListView()
        val fProfile = UserProfile()

        mostrarFragment(fHome)

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

    fun mostrarFragment(frag:Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, frag)
        transaction.commit()
    }
}