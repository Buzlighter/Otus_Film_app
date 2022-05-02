package com.test.otus_film_app.view.favorites_screen

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.util.FilmClickListener
import com.test.otus_film_app.view.film_list_screen.FilmHolder

class FavoritesAdapter(private val listener: FilmClickListener,
                       private val context: Context): RecyclerView.Adapter<FilmHolder>() {

    private val favoriteList = mutableListOf<Film>()

    fun setFavorites(list: List<Film>) {
        favoriteList.clear()
        favoriteList.addAll(list)
        notifyDataSetChanged()
    }

    fun deleteFavorites(film: Film) {
        val deletePosition = favoriteList.indexOf(film)
        favoriteList.remove(film)
        notifyItemRemoved(deletePosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.film_item,parent,false)
        return FilmHolder(view)
    }

    override fun onBindViewHolder(holder: FilmHolder, position: Int) {
        val filmItem = favoriteList[position]
        holder.bind(filmItem, listener, context)
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }
}