package com.test.otus_film_app.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film

class FilmAdapter(private val filmList: List<Film>,
                  private val listener: OnItemClickListener,
                  private val context: Context,
                  private val color: Int): RecyclerView.Adapter<FilmAdapter.FilmHolder>() {

    interface OnItemClickListener {
        fun onItemClick(film: Film, headerText: TextView)
    }

    inner class FilmHolder(view: View, itemListener: OnItemClickListener): RecyclerView.ViewHolder(view) {
        val headerText: TextView = view.findViewById(R.id.item_film_name)
        val filmImg: ImageView = view.findViewById(R.id.item_film_img)

        init {
            itemView.setOnClickListener {
                itemListener.onItemClick(filmList[bindingAdapterPosition], headerText)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmHolder {
        val inflater = LayoutInflater.from(parent.context)
        val countryView = inflater.inflate(R.layout.film_item, parent, false)

        return FilmHolder(countryView, listener)
    }

    override fun onBindViewHolder(viewHolder: FilmHolder, position: Int) {
        val filmItem = filmList[position]

        Glide.with(context).load(filmItem.posterUrlPreview).into(viewHolder.filmImg)
        viewHolder.headerText.text = filmItem.nameRu
        viewHolder.headerText.setTextColor(color)
    }

    override fun getItemCount(): Int {
        return filmList.size
    }
}