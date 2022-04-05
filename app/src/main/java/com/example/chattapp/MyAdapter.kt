package com.example.chattapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter (private val list: ArrayList<User>, private val onItemClicked: (position: Int) -> Unit) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_layout, parent, false)


        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.textView.text = list[position].username
        if (list[position].profilePic == "null"){
            holder.profilePic.setImageResource(R.drawable.standard_profile_pic)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View, private val onItemClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(ItemView), View.OnClickListener {

        val textView: TextView = itemView.findViewById(R.id.name_text_view)
        val profilePic: ImageView = itemView.findViewById(R.id.profile_pic)

        init {
            ItemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            val position = adapterPosition
            onItemClicked(position)
        }


    }

}