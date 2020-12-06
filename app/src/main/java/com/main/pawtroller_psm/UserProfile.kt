package com.main.pawtroller_psm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.main.pawtroller_psm.Models.Pet
import com.main.pawtroller_psm.Models.ResetPasswordUser
import com.main.pawtroller_psm.Models.ResponseLogin
import com.main.pawtroller_psm.Models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter


class UserProfile : Fragment() {

    var listaMascotaUsuario :List<Pet> = listOf()

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
        var listaMascotaUsuarioString= arguments?.getString("listaMascotaUsuarioSting").toString()
        var gson = Gson()
        var user = gson.fromJson(userString, User::class.java)

        var createdAtParse = LocalDate.parse(user.created_at, DateTimeFormatter.ISO_DATE_TIME)
        var createdAt = createdAtParse.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
        var birthDate = LocalDate.parse(user.birthdate, DateTimeFormatter.ISO_DATE_TIME)
        var period =  Period.between(birthDate, LocalDate.now())
        val edad = period.years

        if(!"[]".equals(listaMascotaUsuarioString)) {
             listaMascotaUsuario= ArrayList(
                 gson.fromJson(
                     listaMascotaUsuarioString,
                     Array<Pet>::class.java
                 ).toList()
             )
        }
        view.userName.text = user.name
        view.correoUser.text = "Correo: " + user.email
        view.edadUser.text = "Edad: "+ edad.toString()
        view.registroUser.text = "Miembro desde: " + createdAt
        Picasso.get().load(user.avatar_pic_path).into(view.userImage)
        Picasso.get().load(user.banner_pic_path).into(view.fondoUser)

        view.passEditBtn.setOnClickListener(){
            val pass:String = view.editTextTextPassword2.text.toString()
            val resetPass: String = view.editTextTextPassword.text.toString()
            if("".equals(pass) || "".equals(resetPass)) {
                Toast.makeText(
                    context,
                    "Introduce tu password actual y tu nuevo password",
                    Toast.LENGTH_LONG
                ).show()
            }else {
                cambiarPass(user, pass, resetPass)
            }
        }

        return view
    }

    private fun cambiarPass(user: User, pass: String, resetPass: String) {
        val resetPasswordUser = ResetPasswordUser(pass, resetPass, user.id)

        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<ResponseLogin> = service.resetPassword(resetPasswordUser)

        result.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                val respuesta = response.body()
                if (response.isSuccessful) {
                    Toast.makeText(context, "Password actualizado con éxito", Toast.LENGTH_LONG)
                        .show()
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            context,
                            jObjError.getJSONObject("errors").getString("password"),
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Toast.makeText(context, "Error" + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserProfile().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecycler()
    }

    fun initRecycler(){
        recyclerPets.layoutManager = LinearLayoutManager(context)
        val adapter = RecyclerPetAdapter(listaMascotaUsuario)
        recyclerPets.adapter = adapter
    }

}