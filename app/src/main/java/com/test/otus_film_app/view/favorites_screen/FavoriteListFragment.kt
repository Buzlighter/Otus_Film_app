package com.test.otus_film_app.view.favorites_screen

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.test.otus_film_app.App.Companion.filmDB
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.util.FilmClickListener
import com.test.otus_film_app.util.FilmItemDecoration
import com.test.otus_film_app.view.common.FilmAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteListFragment : Fragment(R.layout.fragment_favorites) {

    private lateinit var favoriteRecycler: RecyclerView
    private lateinit var favoriteAdapter: FilmAdapter

    private var favoriteFilmList = ArrayList<Film>()

    private var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteRecycler = view.findViewById(R.id.favorites_recycler)
        fitRecyclerView()
    }

    private fun fitRecyclerView() {
        setScreenLayout()
        favoriteRecycler.apply {
            favoriteAdapter = FilmAdapter(favoriteListener)
            adapter = favoriteAdapter
            setHasFixedSize(false)
        }
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                filmDB.filmDao().getAll().forEach {
                    if (it.isFavorite && !favoriteFilmList.contains(it)) {
                        favoriteFilmList.add(it)
                    }
                }
            }
            withContext(Dispatchers.Main) {
                favoriteAdapter.differ.submitList(favoriteFilmList)
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
        override fun onFilmClick(film: Film) {
            TODO("Not yet implemented")
        }

        override fun onFilmLongClick(film: Film, position: Int) {
            lifecycleScope.launch(Dispatchers.IO) {
               film.isFavorite = false
               filmDB.filmDao().insertFavoriteFilm(film)
            }

            favoriteFilmList.remove(film)
            favoriteAdapter.apply {
                notifyItemRemoved(position)
                notifyItemRangeChanged(0, favoriteAdapter.itemCount)
            }
            getDeletedSnackBar().show()
        }
    }

    fun getDeletedSnackBar(): Snackbar {
        snackbar = Snackbar.make(favoriteRecycler, R.string.snackBar_deleted_favorites, Snackbar.LENGTH_SHORT)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
            .setAnchorView(activity?.findViewById(R.id.bottom_navigation_view))
        return snackbar as Snackbar
    }

}