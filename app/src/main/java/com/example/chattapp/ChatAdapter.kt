package com.example.chattapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chattapp.models.Chat

class ChatAdapter(
    private val list: ArrayList<Chat>,
    private val onItemClicked: (position: Int) -> Unit,
    private val onItemLongClicked: (position: Int) -> Unit
) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_list_layout, parent, false)


        return ViewHolder(view, onItemClicked, onItemLongClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvName.text = list[position].chatName

        holder.tvLastMessage.text = list[position].lastMessage

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View, private val onItemClicked: (position: Int) -> Unit, private val onItemLongClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(ItemView), View.OnClickListener, View.OnLongClickListener {

        val tvName: TextView = itemView.findViewById(R.id.name_text_view)
        val tvLastMessage: TextView = itemView.findViewById(R.id.last_message_text_view)

        init {
            ItemView.setOnClickListener(this)
            ItemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {

            val position = adapterPosition
            onItemClicked(position)
        }

        override fun onLongClick(v: View?): Boolean {

            val position = adapterPosition
            onItemLongClicked(position)
            return true

        }


    }

}