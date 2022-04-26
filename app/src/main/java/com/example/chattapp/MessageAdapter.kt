package com.example.chattapp

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chattapp.firebase.FirestoreUserDao
import com.example.chattapp.firebase.ImageManager
import com.example.chattapp.models.Message
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MessageAdapter(private var context: Context, messageList: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_USER_MESSAGE_ME = 10
    private val VIEW_TYPE_USER_MESSAGE_OTHER = 11

    private var messages = messageList

    private val currentUser = UserManager.currentUser!!.id
    private val currentUserName = UserManager.currentUser!!.username

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when(viewType) {
            VIEW_TYPE_USER_MESSAGE_ME  -> {
                CurrentUserHolder(layoutInflater.inflate(R.layout.item_message_current_user, parent, false))
            }
            VIEW_TYPE_USER_MESSAGE_OTHER ->  {
                OtherUserHolder(layoutInflater.inflate(R.layout.item_message_other_user, parent, false))
            }
            else -> CurrentUserHolder(layoutInflater.inflate(R.layout.item_message_current_user, parent, false)) //Generic return
        }
    }

    override fun getItemViewType(position: Int): Int {

        val message = messages[position]

        return if (message.sender == currentUser) VIEW_TYPE_USER_MESSAGE_ME
        else VIEW_TYPE_USER_MESSAGE_OTHER

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_USER_MESSAGE_ME -> {
                holder as CurrentUserHolder
                holder.bindView(context, messages[position])
            }
            VIEW_TYPE_USER_MESSAGE_OTHER -> {
                holder as OtherUserHolder
                holder.bindView(context, messages[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    inner class CurrentUserHolder(view: View): RecyclerView.ViewHolder(view){

        val messageText = view.findViewById<TextView>(R.id.text_current_user)
        val username = view.findViewById<TextView>(R.id.username_tv)
        val timestampTextView = view.findViewById<TextView>(R.id.timestamp_message)

        init {
            //Show username and timestamp
            view.setOnClickListener {
                if(timestampTextView.isVisible && username.isVisible){
                    timestampTextView.visibility = View.GONE
                    username.visibility = View.GONE
                } else {
                    timestampTextView.visibility = View.VISIBLE
                    username.visibility = View.VISIBLE
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindView(context: Context, message: Message) {

            messageText.text = message.text
            username.text = currentUserName

            val currentTimeMinusOneDay = LocalDateTime.now().minusDays(1)
            val timestamp = message.timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

            if(timestamp > currentTimeMinusOneDay){
                timestampTextView.text = timestamp.format(DateTimeFormatter.ofPattern("HH:mm"))
            } else {
                timestampTextView.text = timestamp.format(DateTimeFormatter.ofPattern("dd MMM HH:mm"))
            }
        }
    }

    inner class OtherUserHolder(view: View) : RecyclerView.ViewHolder(view) {

        val messageText = view.findViewById<TextView>(R.id.text_other_user)
        val profilePic = view.findViewById<ImageView>(R.id.image_profile_other)
        val username = view.findViewById<TextView>(R.id.username_tv)
        val timestampTextView = view.findViewById<TextView>(R.id.timestamp_message)

        init {
            //Show username and timestamp
            view.setOnClickListener {
                if(timestampTextView.isVisible && username.isVisible){
                    timestampTextView.visibility = View.GONE
                    username.visibility = View.GONE
                } else {
                    timestampTextView.visibility = View.VISIBLE
                    username.visibility = View.VISIBLE
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindView(context: Context, message: Message) {

            messageText.text = message.text
            username.text = FirestoreUserDao.getUsername(message.sender)

            val currentTimeMinusOneDay = LocalDateTime.now().minusDays(1)
            val timestamp = message.timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

            //Format timestamp
            if(timestamp > currentTimeMinusOneDay){
                timestampTextView.text = timestamp.format(DateTimeFormatter.ofPattern("HH:mm"))
            } else {
                timestampTextView.text = timestamp.format(DateTimeFormatter.ofPattern("dd MMM HH:mm"))
            }
            //Profile Pic
            val imageRef = ImageManager.getImageURL(message.sender)
            imageRef.downloadUrl.addOnSuccessListener {
                Glide.with(context).load(it).into(profilePic)
            }.addOnFailureListener {
                println("Failed to load image")
            }
        }
    }
}
