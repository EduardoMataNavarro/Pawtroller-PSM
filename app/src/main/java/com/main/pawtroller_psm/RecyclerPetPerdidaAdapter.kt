package com.main.pawtroller_psm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.main.pawtroller_psm.Models.ResponseEstatusPet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_pet_perdida.view.*

class RecyclerPetPerdidaAdapter (val listaMascotaEstatusPerdido:List<ResponseEstatusPet>):RecyclerView.Adapter<RecyclerPetPerdidaAdapter.PetPerdidaHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetPerdidaHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        return PetPerdidaHolder(layoutInflater.inflate(R.layout.card_pet_perdida, parent,false))
    }

    override fun onBindViewHolder(holder: PetPerdidaHolder, position: Int) {
        holder.render(listaMascotaEstatusPerdido[position])
    }

    override fun getItemCount(): Int =listaMascotaEstatusPerdido.size


    class PetPerdidaHolder(val view: View):RecyclerView.ViewHolder(view){

        fun render(petPerdida: ResponseEstatusPet){
            view.nombreRecyclerPetPerdida.text = petPerdida.pet.name
            view.edadRecyclerPetPerdida.text = petPerdida.pet.age
            view.descRecyclerPetPerdida.text = petPerdida.pet.description
            Picasso.get().load(petPerdida.pet.img_path).into(view.fotoPetPerdida)
        }
    }
}