package com.example.chattapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chattapp.models.User

class NewChatAdapter( private val userList: ArrayList<User>, private val onItemClicked: (position: Int) -> Unit) : RecyclerView.Adapter<NewChatAdapter.ViewHolder>() {

    private var selectedItems : ArrayList<User> = arrayListOf()

    inner class ViewHolder(itemView: View, private val onItemClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val username: TextView = itemView.findViewById(R.id.username_new_chat)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            onItemClicked(position)

            if(selectedItems.contains(userList[position])){
                itemView.setBackgroundColor(Color.TRANSPARENT)
                selectedItems.remove(userList[position])
            } else {
                itemView.setBackgroundColor(Color.LTGRAY)
                selectedItems.add(userList[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.new_chat_list_layout, parent, false)
        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = userList[position].userName
        println(userList[position].toString())
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}