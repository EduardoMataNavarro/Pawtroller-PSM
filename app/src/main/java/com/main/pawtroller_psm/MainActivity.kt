package com.main.pawtroller_psm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var feedFragment = NewsFeed()
        var UserProfile = UserProfile()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.MainFragment, feedFragment)
            addToBackStack(null)
            commit()
        }

        btn_feed.setOnClickListener(){
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.MainFragment, feedFragment)
                addToBackStack(null)
                commit()
            }
        }
        btn_pets.setOnClickListener(){

        }
        btn_forum.setOnClickListener(){

        }
        btn_profile.setOnClickListener(){
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.MainFragment, UserProfile)
                addToBackStack(null)
                commit()
            }
        }
    }
}