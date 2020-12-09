package com.main.pawtroller_psm.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.main.pawtroller_psm.Models.Comment
import com.main.pawtroller_psm.R
import com.squareup.picasso.Picasso

class CommentRecyclerAdapter (private var CommentsList:List<Comment>) :
    RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userName: TextView = itemView.findViewById(R.id.comment_username)
        val userImage: ImageView = itemView.findViewById(R.id.comment_userimage)
        val timestamp: TextView = itemView.findViewById(R.id.comment_timestamp)
        val content: TextView = itemView.findViewById(R.id.comment_content)

        init {
            itemView.setOnClickListener { view: View ->
                val position:Int = adapterPosition
                //Toast.makeText(itemView.context, "Hola este es el post ${ CommentsList[position].id }", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun updateData(newData : List<Comment>){
        this.CommentsList = newData
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return CommentsList.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.userName.text = CommentsList[position].user.name
        Picasso.get().load(CommentsList[position].user.avatar_pic_path).into(holder.userImage)
        holder.content.text = CommentsList[position].comment
        holder.timestamp.text = CommentsList[position].created_at.toString()
    }
}