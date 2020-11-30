package com.main.pawtroller_psm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.main.pawtroller_psm.Models.User
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter


class UserProfile : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_user_profile, container, false)

        var userString = arguments?.getString("userString")
        var gson = Gson()
        var user = gson.fromJson(userString, User::class.java)

        var createdAtParse = LocalDate.parse(user.created_at, DateTimeFormatter.ISO_DATE_TIME)
        var createdAt = createdAtParse.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
        var birthDate = LocalDate.parse(user.birthdate, DateTimeFormatter.ISO_DATE_TIME)
        var period =  Period.between(birthDate, LocalDate.now())
        val edad = period.years


        view.userName.text = user.name
        view.correoUser.text = "Correo: " + user.email
        view.edadUser.text = "Edad: "+ edad.toString()
        view.registroUser.text = "Miembro desde: " + createdAt

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserProfile().apply {
                arguments = Bundle().apply {
                }
            }
    }


}