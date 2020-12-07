package com.main.pawtroller_psm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.main.pawtroller_psm.Models.Pet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_pet_perdida.view.*

class RecyclerPetPerdidaAdapter (val listaPet:List<Pet>):RecyclerView.Adapter<RecyclerPetPerdidaAdapter.PetPerdidaHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetPerdidaHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        return PetPerdidaHolder(layoutInflater.inflate(R.layout.card_pet_perdida, parent,false))
    }

    override fun onBindViewHolder(holder: PetPerdidaHolder, position: Int) {
        holder.render(listaPet[position])
    }

    override fun getItemCount(): Int =listaPet.size


    class PetPerdidaHolder(val view: View):RecyclerView.ViewHolder(view){

        fun render(pet: Pet){
            view.nombreRecyclerPetPerdida.text = pet.name
            view.edadRecyclerPetPerdida.text = pet.age
            view.descRecyclerPetPerdida.text = pet.description
            Picasso.get().load(pet.img_path).into(view.fotoPetPerdida)
        }
    }
}