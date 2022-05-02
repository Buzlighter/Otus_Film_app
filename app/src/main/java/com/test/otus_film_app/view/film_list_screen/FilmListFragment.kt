package com.test.otus_film_app.view.film_list_screen
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.test.otus_film_app.App
import com.test.otus_film_app.App.Companion.filmDB
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.util.Access.Companion.DETAILS_FRAGMENT_BUNDLE_KEY
import com.test.otus_film_app.util.FilmClickListener
import com.test.otus_film_app.util.FilmItemDecoration
import com.test.otus_film_app.view.details_screen.DetailsFragment
import com.test.otus_film_app.viewmodel.FilmViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FilmListFragment : Fragment(R.layout.fragment_filmlist) {

    private val filmViewModel: FilmViewModel by lazy {
        ViewModelProvider(this)[FilmViewModel::class.java]
    }
    private lateinit var filmRecycler: RecyclerView
    private var bottomNav: BottomNavigationView? = null
    var filmAdapter: FilmAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filmRecycler = view.findViewById(R.id.film_recycler)
        bottomNav = activity?.findViewById(R.id.bottom_navigation_view)
        bottomNav?.visibility = View.VISIBLE

        filmViewModel.getFilms()
        fitRecyclerView()
    }

    private fun fitRecyclerView() {
        setScreenLayout()
        filmRecycler.apply {
            filmAdapter = FilmAdapter(filmListener, requireContext())
            adapter = filmAdapter
            setHasFixedSize(true)
            filmViewModel.filmData.observe(viewLifecycleOwner) {
                filmAdapter?.setFilms(it.filmList)
            }
        }
    }

    private fun setScreenLayout() {
        val orientation = activity?.resources?.configuration?.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setDecorator()
            filmRecycler.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false)

        } else {
            filmRecycler.layoutManager = GridLayoutManager(
                requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setDecorator() {
        filmRecycler.addItemDecoration(FilmItemDecoration())
    }


    private val filmListener = object: FilmClickListener {
        override fun onFilmClick(film: Film, position: Int) {
            App.arrayOfPosition.add(position)
            bottomNav?.visibility = View.GONE
            val bundle = Bundle()
            bundle.putSerializable(DETAILS_FRAGMENT_BUNDLE_KEY, film)
            parentFragmentManager.commit {
                replace<DetailsFragment>(R.id.fragment_main_container, DETAILS_FRAGMENT_BUNDLE_KEY, bundle)
                addToBackStack(null)
                setReorderingAllowed(true)
            }

            Log.d("colorPos", App.arrayOfPosition.toString())

        }

        override fun onFilmLongClick(film: Film) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    filmDB.filmDao().insertFavoriteFilm(film)
                }
            }
            Toast.makeText(requireContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show()
        }
    }
}
