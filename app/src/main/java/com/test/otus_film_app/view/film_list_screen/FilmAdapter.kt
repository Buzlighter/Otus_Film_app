package com.test.otus_film_app.view.film_list_screen

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.util.FilmClickListener
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

import android.view.animation.ScaleAnimation


class FilmAdapter(private val listener: FilmClickListener,
                  private val context: Context): RecyclerView.Adapter<FilmHolder>() {

    private val filmList = mutableListOf<Film>()

    fun setFilms(list: List<Film>) {
        filmList.clear()
        filmList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.film_item,parent,false)
        return FilmHolder(view)
    }

    override fun onBindViewHolder(holder: FilmHolder, position: Int) {
        val filmItem = filmList[position]
        holder.bind(filmItem, listener, context, position)
    }

    override fun getItemCount(): Int {
        return filmList.size
    }
}