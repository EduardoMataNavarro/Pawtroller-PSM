package com.main.pawtroller_psm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.main.pawtroller_psm.models.Pet_media
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_pet.view.*

class RecyclerPetFotoAdapter (val listaPetMedia:List<Pet_media>):RecyclerView.Adapter<RecyclerPetFotoAdapter.PetFotoHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetFotoHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        return PetFotoHolder(layoutInflater.inflate(R.layout.card_foto_pet, parent,false))
    }

    override fun onBindViewHolder(holder: PetFotoHolder, position: Int) {
        holder.render(listaPetMedia[position])
    }

    override fun getItemCount(): Int =listaPetMedia.size


    class PetFotoHolder(val view: View):RecyclerView.ViewHolder(view){

        fun render(pet_media: Pet_media){
            Picasso.get().load(pet_media.path).into(view.fotoPet)
        }
    }
}