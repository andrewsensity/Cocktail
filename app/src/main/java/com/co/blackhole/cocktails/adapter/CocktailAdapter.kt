package com.co.blackhole.cocktails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.co.blackhole.cocktails.R
import com.co.blackhole.cocktails.data.model.Cocktail
import com.google.android.material.textview.MaterialTextView

class CocktailAdapter(private val listener: OnItemClickListener): RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder>() {

    private var listCocktail: List<Cocktail> = mutableListOf()

    inner class CocktailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val ivMain = itemView.findViewById<AppCompatImageView>(R.id.ivMain)
        val tvTitle = itemView.findViewById<MaterialTextView>(R.id.tvTitle)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            listener.onItemClick(position)
            if (position != RecyclerView.NO_POSITION) {
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocktailViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cocktail, parent, false)
        return CocktailViewHolder(view)
    }

    override fun onBindViewHolder(holder: CocktailViewHolder, position: Int) {
        val currentList = listCocktail[position]
        Glide.with(holder.itemView.context).load(currentList.image).into(holder.ivMain)
        holder.tvTitle.text = currentList.name
    }

    override fun getItemCount(): Int = listCocktail.size

    fun setData(cocktail: MutableList<Cocktail>) {
        listCocktail = cocktail
        notifyDataSetChanged()
    }
}