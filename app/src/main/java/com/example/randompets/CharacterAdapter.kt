package com.example.randompets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


data class NasaItem(
    val title: String,
    val explanation: String,
    val url: String
)
class NasaItemAdapter(private val characters: List<NasaItem>) :
    RecyclerView.Adapter<NasaItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val characterImage: ImageView = view.findViewById(R.id.itemImageView)
        val characterName: TextView = view.findViewById(R.id.titleTextView)
        val characterDescription: TextView = view.findViewById(R.id.descriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_item, parent, false) // Use the new item layout
        return ViewHolder(view)
    }

    override fun getItemCount() = characters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characters[position]

        holder.characterName.text = character.title
        holder.characterDescription.text = character.explanation.ifEmpty { "No description available." }

        Glide.with(holder.itemView.context)
            .load(character.url)
            .centerCrop()
            .into(holder.characterImage)
    }
}