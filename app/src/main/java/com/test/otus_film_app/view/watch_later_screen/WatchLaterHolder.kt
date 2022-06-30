package com.test.otus_film_app.view.watch_later_screen

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.util.FilmClickListener

class WatchLaterHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val nameText: TextView = itemView.findViewById(R.id.item_film_name)
    val filmImg: ImageView = itemView.findViewById(R.id.item_film_img)
    val calendarImg: ImageView = itemView.findViewById(R.id.item_calendar_img)
    val dateText: TextView = itemView.findViewById(R.id.item_notification_date)
    val yearText: TextView = itemView.findViewById(R.id.item_film_year)

    fun bind(film: Film, listener: FilmClickListener, position: Int) {
        Glide.with(filmImg.context).load(film.posterUrlPreview).into(filmImg)
        nameText.text = film.nameRu
        yearText.text = film.year.toString()
        dateText.text = film.notificationDate

        itemView.apply {
            setOnClickListener {
                listener.onFilmClick(film, position)
            }

            setOnLongClickListener {
                listener.onFilmLongClick(film, position)
                true
            }
        }
    }
}