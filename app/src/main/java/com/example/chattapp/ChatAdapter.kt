package com.example.chattapp

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chattapp.firebase.ImageManager
import com.example.chattapp.models.Chat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class ChatAdapter(
    private val list: ArrayList<Chat>,
    private val mCotext: Context,
    private val onItemClicked: (position: Int) -> Unit
) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_list_layout, parent, false)


        return ViewHolder(view, onItemClicked)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var chatname = list[position].chatName
        chatname = chatname.replace(UserManager.currentUser!!.username, "")
        chatname = chatname.removePrefix(", ")
        chatname = chatname.removeSuffix(", ")
        chatname = chatname.replace(", ,", ", ")

        holder.tvName.text = chatname
        holder.tvLastMessage.text = list[position].lastMessage

        val imageRef = ImageManager.getImageURL(list[position].usersInChat[0])
        imageRef.downloadUrl.addOnSuccessListener {
            Glide.with(mCotext).load(it).into(holder.ivProfilePic)
        }.addOnFailureListener {
            println("Failed to load image")
        }

        val currentTimeMinusOneDay = LocalDate.now().minusDays(1)
        val timestamp = list[position].timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

        if(timestamp.toLocalDate() > currentTimeMinusOneDay){
            holder.tvTimestamp.text = timestamp.format(DateTimeFormatter.ofPattern("HH:mm"))
        } else if(timestamp.toLocalDate() == currentTimeMinusOneDay) {
            holder.tvTimestamp.text = "Yesterday"
        } else {
            holder.tvTimestamp.text = timestamp.format(DateTimeFormatter.ofPattern("dd MMM"))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View, private val onItemClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(ItemView), View.OnClickListener{

        val tvName: TextView = itemView.findViewById(R.id.name_text_view)
        val tvLastMessage: TextView = itemView.findViewById(R.id.last_message_text_view)
        val tvTimestamp: TextView = itemView.findViewById(R.id.timestamp_text_view)
        val ivProfilePic: ImageView = itemView.findViewById(R.id.profile_chat_image_view)

        init {
            ItemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            val position = adapterPosition
            onItemClicked(position)
        }
    }
}