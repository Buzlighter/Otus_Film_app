package com.test.otus_film_app.view.film_list_screen

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.otus_film_app.App
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.util.FilmClickListener
import java.text.FieldPosition

class FilmHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val headerText: TextView = itemView.findViewById(R.id.item_film_name)
    val filmImg: ImageView = itemView.findViewById(R.id.item_film_img)


    fun bind(film: Film, listener: FilmClickListener, context: Context, position: Int = 0) {
        Glide.with(context).load(film.posterUrlPreview).into(filmImg)
        headerText.text = film.nameRu
        if (App.arrayOfPosition.contains(position)) {
            headerText.setTextColor(ContextCompat.getColor(context, R.color.purple_200))
        }

        itemView.setOnClickListener {
            listener.onFilmClick(film, position)
        }
        itemView.setOnLongClickListener {
            listener.onFilmLongClick(film)
            true
        }
    }
}