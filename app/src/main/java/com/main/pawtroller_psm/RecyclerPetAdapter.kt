package com.main.pawtroller_psm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.main.pawtroller_psm.Models.Pet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_pet.view.*

class RecyclerPetAdapter (val listaPet:List<Pet>):RecyclerView.Adapter<RecyclerPetAdapter.PetHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        return PetHolder(layoutInflater.inflate(R.layout.card_pet, parent,false))
    }

    override fun onBindViewHolder(holder: PetHolder, position: Int) {
        holder.render(listaPet[position])
    }

    override fun getItemCount(): Int =listaPet.size


    class PetHolder(val view: View):RecyclerView.ViewHolder(view){

        fun render(pet: Pet){
            view.nombreRecyclerPet.text = pet.name
            view.edadRecyclerPet.text = pet.age
            view.descRecyclerPet.text = pet.description
            Picasso.get().load(pet.img_path).into(view.imagenAvatarPet)
        }
    }
}