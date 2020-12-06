package com.main.pawtroller_psm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.main.pawtroller_psm.Models.Pet

class RecyclerPetAdapter (val listaPet:List<Pet>):RecyclerView.Adapter<RecyclerPetAdapter.PetHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        return PetHolder(layoutInflater.inflate(R.layout.card_pet, parent,false))
    }

    override fun onBindViewHolder(holder: PetHolder, position: Int) {
        holder.render(listaPet[position])
    }

    override fun getItemCount(): Int =listaPet.size


    class PetHolder(view: View):RecyclerView.ViewHolder(view){

        fun render(pet: Pet){

        }
    }
}