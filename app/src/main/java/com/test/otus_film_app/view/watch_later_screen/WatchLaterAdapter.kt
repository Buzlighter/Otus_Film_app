package com.test.otus_film_app.view.watch_later_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.util.FilmClickListener

class WatchLaterAdapter(private val clickListener: FilmClickListener): RecyclerView.Adapter<WatchLaterHolder>() {


    private val differCallBack = object: DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchLaterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.watch_later_item,parent,false)
        return WatchLaterHolder(view)
    }

    override fun onBindViewHolder(holder: WatchLaterHolder, position: Int) {
        val filmItem = differ.currentList[position]
        holder.bind(filmItem, clickListener, position)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}