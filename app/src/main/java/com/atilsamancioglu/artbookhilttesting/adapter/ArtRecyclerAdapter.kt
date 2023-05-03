package com.atilsamancioglu.artbookhilttesting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.atilsamancioglu.artbookhilttesting.R
import com.atilsamancioglu.artbookhilttesting.databinding.ArtRowBinding
import com.atilsamancioglu.artbookhilttesting.roomdb.Art
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class ArtRecyclerAdapter @Inject constructor(
        val glide : RequestManager
) : RecyclerView.Adapter<ArtRecyclerAdapter.ArtViewHolder>() {


    class ArtViewHolder(val binding: ArtRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    private val diffUtil = object : DiffUtil.ItemCallback<Art>() {
        override fun areItemsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }


    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)
    var arts: List<Art>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtViewHolder {
        val binding = ArtRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArtViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
        holder.binding.artRowArtNameText.text = "Name: ${arts[position].name}"
        holder.binding.artRowArtistNameText.text = "Artist Name: ${arts[position].artistName}"
        holder.binding.artRowYearText.text = "Year: ${arts[position].year}"
        glide.load(arts[position].imageUrl).into(holder.binding.artRowImageView)
    }

    override fun getItemCount(): Int {
        return arts.size
    }

}