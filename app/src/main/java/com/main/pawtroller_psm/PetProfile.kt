package com.main.pawtroller_psm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.main.pawtroller_psm.Models.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_pet_profile.*
import kotlinx.android.synthetic.main.fragment_pet_profile.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class PetProfile : Fragment() {

    var idPet = 0
    private val FOTO_PET = 2000

    var user: User ?=null
    var listaMascotaUsuario: List<Pet> = listOf()
    var listaPetMedia: List<Pet_media> = listOf()
    var listaTipoMascota: List<TipoMascota> = listOf()
    var listaMascotaEstatusPerdido: List<ResponseEstatusPet> = listOf()
    var listaMascotaEstatusFallecida: List<ResponseEstatusPet> = listOf()

    var userString: String ?= null
    var listaMascotaUsuarioString: String = "[]"
    var listaTipoMascotaString:  String = "[]"
    var listaMascotaEstatusPerdidoString: String = "[]"
    var listaMascotaEstatusfallecidaString: String = "[]"


    var estatusPetId: String ?= null
    var statusMascota: String?= null
    var vistas : Int = 0
    var seleccion: Int =0

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

        userString= arguments?.getString("userString").toString()
        listaMascotaUsuarioString= arguments?.getString("listaMascotaUsuarioString").toString()
        listaTipoMascotaString = arguments?.getString("listaTipoMascotaString").toString()
        listaMascotaEstatusPerdidoString = arguments?.getString("listaMascotaEstatusPerdidoString").toString()
        listaMascotaEstatusfallecidaString = arguments?.getString("listaMascotaEstatusfallecidaString").toString()

        var gson = Gson()

        listaTipoMascota = ArrayList(gson.fromJson(listaTipoMascotaString, Array<TipoMascota>::class.java).toList())


        if(!"[]".equals(listaMascotaEstatusPerdidoString)) {
            listaMascotaEstatusPerdido =
                ArrayList(
                    gson.fromJson(listaMascotaEstatusPerdidoString, Array<ResponseEstatusPet>::class.java)
                        .toList()
                )
        }

        if(!"[]".equals(listaMascotaEstatusfallecidaString)) {
            listaMascotaEstatusFallecida =
                ArrayList(
                    gson.fromJson(listaMascotaEstatusfallecidaString, Array<ResponseEstatusPet>::class.java)
                        .toList()
                )
        }

        if(!"[]".equals(listaMascotaUsuarioString)) {
                 listaMascotaUsuario =
                ArrayList(gson.fromJson(listaMascotaUsuarioString, Array<Pet>::class.java)
                    .toList())


            actualizaVista(view)

            view.btnBack.setOnClickListener() {
                cambiarPetAtras(listaMascotaUsuario.size, view)
            }

            view.btnFwd.setOnClickListener() {
                cambiarPetAdelante(listaMascotaUsuario.size, view)
            }
        }

        view.buttonCambiarEstatus.setOnClickListener(){
            cambiarEstatusPet()
        }
        view.fabCrearPet.setOnClickListener() {
            abrirCrearPet(userString!!)
        }

        view.fabCargarFotoPet.setOnClickListener(){
            agregarFotoPet()
        }

        return view
    }

    private fun cambiarEstatusPet() {
        val iCrearPetActivity = Intent(context,CambiarEstatusActivity::class.java)
        iCrearPetActivity.putExtra("listaTipoMascotaString",listaTipoMascotaString)
        iCrearPetActivity.putExtra("userString",userString)
        iCrearPetActivity.putExtra("petid",listaMascotaUsuario[idPet].id)
        iCrearPetActivity.putExtra("estatus", statusMascota)
        startActivity(iCrearPetActivity)
    }

    private fun agregarFotoPet() {
        val imagenIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagenIntent.setType("image/")
        startActivityForResult(
            Intent.createChooser(imagenIntent, "Selecciona la aplicacion"),
            FOTO_PET
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === AppCompatActivity.RESULT_OK) {
            if(requestCode===FOTO_PET) {
                val path: Uri? = data?.data

                val parcelFileDescriptor =
                    context!!.contentResolver.openFileDescriptor(path!!, "r", null) ?: return

                var file = File(context!!.cacheDir, context!!.contentResolver.getFileName(path))
                val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)

                enviarFoto(file)
            }

        }
    }

    private fun enviarFoto(file: File) {

        var requestBody: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file!!)
        var filePart = MultipartBody.Part.createFormData("media", file!!.name, requestBody)

        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<ResponsePetMedia> = service.agregarMediaPet(
            filePart, RequestBody.create("multipart/form-data".toMediaTypeOrNull(),"image"),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), listaMascotaUsuario[idPet].id)
        )

        result.enqueue(object : Callback<ResponsePetMedia> {
            override fun onResponse(call: Call<ResponsePetMedia>, response: Response<ResponsePetMedia>) {
                val respuesta = response.body()
                if (response.isSuccessful) {
                    Toast.makeText(context, "Foto agregada con éxito", Toast.LENGTH_LONG)
                        .show()
                    //TODO actualizar la vista para que se vea el cambio de banner y avatar
                    cargarImagenesPet()
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            context,
                            jObjError.getJSONObject("errors").toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponsePetMedia>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun actualizaVista(view: View ) {
        view.nombrePet.text = listaMascotaUsuario[idPet].name
        view.edadPet.text ="Edad: " +listaMascotaUsuario[idPet].age
        view.descripcionPet.text = "Descripción: " +listaMascotaUsuario[idPet].description
        view.fecNacPet.text = "Fecha de nacimiento: " + listaMascotaUsuario[idPet].birthdate
        view.nicknamePet.text = "Apodo: " + listaMascotaUsuario[idPet].nickname
        var tipo: String = ""
        for ( item in listaTipoMascota){
            if(listaMascotaUsuario[idPet].type_id.equals(item.id)) {
                tipo = item.name
                view.tipoPet.text = "Tipo de Mascota:" + tipo
                break
            }
        }
        view.tipoPet.text = "Tipo de Mascota:" + tipo
        Picasso.get().load(listaMascotaUsuario[idPet].img_path).into(view.avatarPet)

        asignaEstatusPet()
        view.estatusPet3.text = statusMascota
        cargarImagenesPet()
    }

    private fun asignaEstatusPet() {
        if(listaMascotaEstatusFallecida.size>0){
            for (item in listaMascotaEstatusFallecida){
                if(listaMascotaUsuario[idPet].id.equals(item.pet.id)){
                    estatusPetId = "2"
                    statusMascota= "fallecido"
                    return
                }
            }
        }

        if(listaMascotaEstatusPerdido.size>0){
            for (item in listaMascotaEstatusPerdido){
                if(listaMascotaUsuario[idPet].id.equals(item.pet.id)){
                    estatusPetId= "1"
                    statusMascota="perdido"
                    return
                }
            }
        }

        estatusPetId = "0"
        statusMascota = "bien"
    }

    private fun cambiarPetAtras(size:Int,view: View) {
       if (idPet>0){
            idPet--
        }else{
            idPet = size-1
        }
        actualizaVista(view)
    }

    private fun cambiarPetAdelante(size:Int,view: View) {
        if (idPet<size-1){
            idPet++
        }else{
            idPet = 0
        }
        actualizaVista(view)
    }

    fun cargarImagenesPet() {
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<List<Pet_media>>> = service.consultaImagenPorMascota(listaMascotaUsuario[idPet].id.toInt())

        result.enqueue(object : Callback<List<List<Pet_media>>> {
            override fun onResponse(call: Call<List<List<Pet_media>>>, response: Response<List<List<Pet_media>>>) {
                var respuesta = response.body()!!
                if (response.isSuccessful) {
                    listaPetMedia = respuesta[0]
                    initRecycler()
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            context,
                            jObjError.getJSONObject("errors").toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
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

        val iCrearPetActivity = Intent(context,CrearPetActivity::class.java)
        iCrearPetActivity.putExtra("listaTipoMascotaString",listaTipoMascotaString)
        iCrearPetActivity.putExtra("userString",userString)
        startActivity(iCrearPetActivity)

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        recyclerFotoPet.layoutManager = GridLayoutManager (context,2)
        val adapter = RecyclerPetFotoAdapter(listaPetMedia)
        recyclerFotoPet.adapter = adapter
    }

}