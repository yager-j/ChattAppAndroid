package com.example.chattapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private val list: ArrayList<User>,
    private val onItemClicked: (position: Int) -> Unit,
    private val onItemLongClicked: (position: Int) -> Unit
) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_list_layout, parent, false)


        return ViewHolder(view, onItemClicked, onItemLongClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.textView.text = list[position].userName

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View, private val onItemClicked: (position: Int) -> Unit, private val onItemLongClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(ItemView), View.OnClickListener, View.OnLongClickListener {

        val textView: TextView = itemView.findViewById(R.id.name_text_view)

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