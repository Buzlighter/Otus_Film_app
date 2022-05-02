package com.test.otus_film_app.view.favorites_screen

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.otus_film_app.App.Companion.filmDB
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.util.FilmClickListener
import com.test.otus_film_app.util.FilmItemDecoration
import com.test.otus_film_app.view.film_list_screen.FilmAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.notifyAll

class FavoriteListFragment : Fragment(R.layout.fragment_favorites) {

    private lateinit var favoriteRecycler: RecyclerView

    var favoriteAdapter: FavoritesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteRecycler = view.findViewById(R.id.favorites_recycler)
        fitRecyclerView()
    }


    private fun fitRecyclerView() {
        setScreenLayout()
        favoriteRecycler.apply {
            favoriteAdapter = FavoritesAdapter(favoriteListener, requireContext())
            adapter = favoriteAdapter
            setHasFixedSize(true)

            var list: List<Film>?
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    list = filmDB.filmDao().getAll()
                }
                withContext(Dispatchers.Main) {
                    list?.let { favoriteAdapter?.setFavorites(it) }
                }
            }
        }
    }

    private fun setScreenLayout() {
        val orientation = activity?.resources?.configuration?.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setDecorator()
            favoriteRecycler.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false)

        } else {
            favoriteRecycler.layoutManager = GridLayoutManager(
                requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setDecorator() {
        favoriteRecycler.addItemDecoration(FilmItemDecoration())
    }

    private val favoriteListener = object: FilmClickListener {

        override fun onFilmClick(film: Film, position: Int) {
            TODO("Not yet implemented")
        }

        override fun onFilmLongClick(film: Film) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    filmDB.filmDao().delete(film)
                }
                withContext(Dispatchers.Main) {
                    favoriteAdapter?.deleteFavorites(film)
                }
            }
            Toast.makeText(requireContext(), "Удалено из избранного", Toast.LENGTH_SHORT).show()
        }
    }
}