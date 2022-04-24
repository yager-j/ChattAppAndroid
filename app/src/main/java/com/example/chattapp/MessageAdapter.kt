package com.example.chattapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chattapp.firebase.ImageManager
import com.example.chattapp.models.Message

class MessageAdapter(private var context: Context, messageList: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val storageManager = ImageManager()

    private val VIEW_TYPE_USER_MESSAGE_ME = 10
    private val VIEW_TYPE_USER_MESSAGE_OTHER = 11

    private var messages = messageList

    private val currentUser = "A0CC5F6F-E5E1-461F-A737-E373C8F30E34"

    fun loadMessages(messages: ArrayList<Message>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    fun addFirst(message: Message) {
        messages.add(0, message)
        notifyDataSetChanged()
    }

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

        val message = messages.get(position)


        return if (message.sender == currentUser) VIEW_TYPE_USER_MESSAGE_ME
        else VIEW_TYPE_USER_MESSAGE_OTHER

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_USER_MESSAGE_ME -> {
                holder as CurrentUserHolder
                holder.bindView(context, messages.get(position) as Message)
            }
            VIEW_TYPE_USER_MESSAGE_OTHER -> {
                holder as OtherUserHolder
                holder.bindView(context, messages.get(position) as Message)
            }

        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    inner class CurrentUserHolder(view: View): RecyclerView.ViewHolder(view){

        val messageText = view.findViewById<TextView>(R.id.text_current_user)

        fun bindView(context: Context, message: Message) {

            messageText.text = message.text

        }
    }

    inner class OtherUserHolder(view: View) : RecyclerView.ViewHolder(view) {

        val messageText = view.findViewById<TextView>(R.id.text_other_user)
        val profilePic = view.findViewById<ImageView>(R.id.image_profile_other)

        fun bindView(context: Context, message: Message) {

            messageText.text = message.text

            val imageRef = storageManager.getImageURL(message.sender)
            imageRef.downloadUrl.addOnSuccessListener {
                Glide.with(context).load(it).into(profilePic)
            }.addOnFailureListener {
                println("Failed to load image")
            }

        }
    }
}
