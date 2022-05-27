package com.test.otus_film_app.view.common

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.util.FilmClickListener

class FilmHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val headerText: TextView = itemView.findViewById(R.id.item_film_name)
    val filmImg: ImageView = itemView.findViewById(R.id.item_film_img)

    fun bind(film: Film, listener: FilmClickListener, position: Int) {
        Glide.with(filmImg.context).load(film.posterUrlPreview).into(filmImg)
        headerText.text = film.nameRu

        itemView.apply {
            setOnClickListener {
                listener.onFilmClick(film)
            }

            setOnLongClickListener {
                listener.onFilmLongClick(film, position)
                true
            }
        }
    }
}