package com.main.pawtroller_psm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.main.pawtroller_psm.datos.DatosMascota
import com.main.pawtroller_psm.datos.DatosUsuario
import com.main.pawtroller_psm.models.Pet_media
import com.main.pawtroller_psm.models.ResponseEstatusPet
import kotlinx.android.synthetic.main.fragment_news_feed.*
import kotlinx.android.synthetic.main.fragment_pet_profile.*

/**
 * A simple [Fragment] subclass.
 * Use the [NewsFeed.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsFeed : Fragment() {

    var datosUsuario = DatosUsuario()
    var datosMascota= DatosMascota()


    var listaPetMedia: List<Pet_media> = listOf()
    var listaMascotaEstatusPerdido: List<ResponseEstatusPet> = listOf()

    var listaMascotaEstatusPerdidoString: String = "[]"

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
        val view =  inflater.inflate(R.layout.fragment_news_feed, container, false)

        datosUsuario.obtenerDatosFragment(arguments)
        datosMascota.obtenerDatosFragment(arguments)

        listaMascotaEstatusPerdidoString= arguments?.getString("listaMascotaEstatusPerdidoString").toString()
        var gson = Gson()



        if(!"[]".equals(listaMascotaEstatusPerdidoString)) {
            listaMascotaEstatusPerdido =
                ArrayList(
                    gson.fromJson(listaMascotaEstatusPerdidoString, Array<ResponseEstatusPet>::class.java)
                        .toList()
                )
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
         * @return A new instance of fragment NewsFeed.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewsFeed().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        recyclerViewPetPerdida.layoutManager = LinearLayoutManager (context,LinearLayoutManager.HORIZONTAL, false)
        val adapter = RecyclerPetPerdidaAdapter(listaMascotaEstatusPerdido)
        recyclerViewPetPerdida.adapter = adapter
    }
}