package com.main.pawtroller_psm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.main.pawtroller_psm.Models.Pet
import com.main.pawtroller_psm.Models.TipoMascota
import kotlinx.android.synthetic.main.fragment_pet_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PetProfile : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_pet_profile, container, false)

        var userString:String = ""
        var listaMascotaUsuarioSting:String = ""
        userString= arguments?.getString("userString").toString()
        listaMascotaUsuarioSting= arguments?.getString("listaMascotaUsuarioSting").toString()
        var gson = Gson()
        var listaMascotaUsuario:List<Pet> = gson.fromJson(listaMascotaUsuarioSting, Array<Pet>::class.java).toList() as ArrayList<Pet>

        view.nombrePet.text = listaMascotaUsuario[0].name
        view.edadPet.text ="Edad: " +listaMascotaUsuario[0].age
        view.descripcionPet.text = "Descripción: " +listaMascotaUsuario[0].description
        view.fecNacPet.text = "Fecha de nacimiento: " + listaMascotaUsuario[0].birthdate

        view.fabCrearPet.setOnClickListener() {

            abrirCrearPet(userString)
        }

        return view
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PetProfile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PetProfile().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun abrirCrearPet(userString:String) {

        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<List<TipoMascota>>> = service.obtenerMascotas()
        var arrayMascotas: List<List<TipoMascota>> = listOf()

        result.enqueue(object : Callback<List<List<TipoMascota>>> {
            override fun onResponse(call: Call<List<List<TipoMascota>>>, response: Response<List<List<TipoMascota>>>) {
                arrayMascotas = response.body()!!
                var gson = Gson()
                var listaMascostaString = gson.toJson(arrayMascotas[0])

                val iCrearPetActivity = Intent(context,CrearPetActivity::class.java)
                iCrearPetActivity.putExtra("listaMascostaString",listaMascostaString)
                iCrearPetActivity.putExtra("userString",userString)
                startActivity(iCrearPetActivity)
            }

            override fun onFailure(call: Call<List<List<TipoMascota>>>, t: Throwable) {
                Toast.makeText(context, "Error" + t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

}