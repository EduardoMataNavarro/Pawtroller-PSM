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
import com.main.pawtroller_psm.Models.Pet_media
import com.main.pawtroller_psm.Models.TipoMascota
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_pet_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PetProfile : Fragment() {

    var idPet = 0
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
        var listaMascotaUsuarioString:String = ""
        userString= arguments?.getString("userString").toString()
        listaMascotaUsuarioString= arguments?.getString("listaMascotaUsuarioSting").toString()
        var gson = Gson()

        if(!"[]".equals(listaMascotaUsuarioString)) {
            var listaMascotaUsuario: List<Pet> =
                ArrayList(gson.fromJson(listaMascotaUsuarioString, Array<Pet>::class.java)
                    .toList())

            // TODO validar si viene vacia la lista de mascotas
            actualizaVista(view, listaMascotaUsuario)

            view.btnBack.setOnClickListener() {
                cambiarPetAtras(listaMascotaUsuario.size, view, listaMascotaUsuario)
            }

            view.btnFwd.setOnClickListener() {
                cambiarPetAdelante(listaMascotaUsuario.size, view, listaMascotaUsuario)
            }
        }

        view.fabCrearPet.setOnClickListener() {
            abrirCrearPet(userString)
        }

        return view
    }

    private fun actualizaVista(view: View, listaMascotaUsuario: List<Pet>) {
        view.nombrePet.text = listaMascotaUsuario[idPet].name
        view.edadPet.text ="Edad: " +listaMascotaUsuario[idPet].age
        view.descripcionPet.text = "Descripción: " +listaMascotaUsuario[idPet].description
        view.fecNacPet.text = "Fecha de nacimiento: " + listaMascotaUsuario[idPet].birthdate
        view.nicknamePet.text = "Apodo: " + listaMascotaUsuario[idPet].nickname
        Picasso.get().load(listaMascotaUsuario[idPet].img_path).into(view.avatarPet)
        // TODO cargar imagenes de la pet
       // cargarImagenesPet(view,listaMascotaUsuario[idPet].id)
    }

    private fun cambiarPetAtras(size:Int,view: View, listaMascotaUsuario: List<Pet>) {
       if (idPet>0){
            idPet--
        }else{
            idPet = size-1
        }
        actualizaVista(view,listaMascotaUsuario)
    }

    private fun cambiarPetAdelante(size:Int,view: View, listaMascotaUsuario: List<Pet>) {
        if (idPet<size-1){
            idPet++
        }else{
            idPet = 0
        }
        actualizaVista(view,listaMascotaUsuario)
    }

    fun cargarImagenesPet(view: View,id: String) {
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<List<Pet_media>>> = service.consultaImagenPorMascota(1)
        var arrayImagenes: List<List<Pet_media>> = listOf()

        result.enqueue(object : Callback<List<List<Pet_media>>> {
            override fun onResponse(call: Call<List<List<Pet_media>>>, response: Response<List<List<Pet_media>>>) {
                arrayImagenes = response.body()!!
                Picasso.get().load(arrayImagenes[0][0].path).into(view.avatarPet)
                Picasso.get().load(arrayImagenes[0][0].path).into(view.imageView3)
            }

            override fun onFailure(call: Call<List<List<Pet_media>>>, t: Throwable) {
                Toast.makeText(context, "Error" + t.message, Toast.LENGTH_LONG).show()
            }

        })

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