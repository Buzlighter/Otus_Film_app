package com.test.otus_film_app.view.film_list_screen
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_FADE
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import com.test.otus_film_app.App.Companion.filmDB
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.model.KinopoiskResponse
import com.test.otus_film_app.util.Constants.Companion.DETAILS_FRAGMENT_BUNDLE_KEY
import com.test.otus_film_app.util.FilmClickListener
import com.test.otus_film_app.util.FilmItemDecoration
import com.test.otus_film_app.util.Resource
import com.test.otus_film_app.view.common.FilmAdapter
import com.test.otus_film_app.view.details_screen.DetailsFragment
import com.test.otus_film_app.viewmodel.FilmViewModel
import com.test.otus_film_app.viewmodel.FilmViewModelFactory
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.schedule
import kotlin.coroutines.suspendCoroutine


class FilmListFragment : Fragment(R.layout.fragment_filmlist) {

    private val filmViewModel: FilmViewModel by viewModels { FilmViewModelFactory() }

    private lateinit var filmRecycler: RecyclerView
    private lateinit var filmAdapter: FilmAdapter

    private var bottomNav: BottomNavigationView? = null
    var addFavoriteSnackBar: Snackbar? = null
    var errorSnackBar: Snackbar? = null
    lateinit var progressBar: ProgressBar
    lateinit var errorLayout: ConstraintLayout
    lateinit var pullToRefresh: SwipeRefreshLayout


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filmRecycler = view.findViewById(R.id.film_recycler)
        bottomNav = activity?.findViewById(R.id.bottom_navigation_view)
        progressBar = view.findViewById(R.id.progress_bar)
        errorLayout = view.findViewById(R.id.error_layout)
        pullToRefresh = view.findViewById(R.id.pull_to_refresh)

        bottomNav?.visibility = View.VISIBLE

        fitRecyclerView()
        pagination()
    }

    private fun fitRecyclerView() {
        setScreenLayout()
        pullToRefresh.setOnRefreshListener(filmRefreshLister)
        filmRecycler.apply {
            filmAdapter = FilmAdapter(filmListener)
            adapter = filmAdapter
            setHasFixedSize(true)
        }
        filmViewModel.filmData.observe(viewLifecycleOwner) { response ->
            checkRequest(response)
        }
    }

    private fun checkRequest(response: Resource<KinopoiskResponse>) {
        when (response) {
            is Resource.Success -> {
                hideProgressBar()
                filmAdapter.differ.submitList(response.data?.filmList)

                bottomNav?.visibility = View.VISIBLE
                pullToRefresh.visibility = View.VISIBLE
                errorLayout.visibility = View.GONE
                pullToRefresh.isRefreshing = false
                if (errorSnackBar != null)  errorSnackBar?.dismiss()
            }
            is Resource.Error -> {
                hideProgressBar()
                pullToRefresh.visibility = View.GONE
                errorLayout.visibility = View.VISIBLE
                bottomNav?.visibility = View.INVISIBLE
                getErrorScreenSnackBar().show()
            }
            is Resource.Loading -> {
                showProgressBar()
            }
        }
    }

    private fun setScreenLayout() {
        val orientation = activity?.resources?.configuration?.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            filmRecycler.apply {
                addItemDecoration(FilmItemDecoration())
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        } else {
            filmRecycler.layoutManager = GridLayoutManager(
                requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun showProgressBar() {
        if(!isFromRefresher) progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private val filmListener = object: FilmClickListener {
        override fun onFilmClick(film: Film) {
            val bundle = Bundle()
            bundle.putSerializable(DETAILS_FRAGMENT_BUNDLE_KEY, film)
            parentFragmentManager.commit {
                replace<DetailsFragment>(R.id.fragment_main_container, DETAILS_FRAGMENT_BUNDLE_KEY, bundle)
                addToBackStack(null)
                setReorderingAllowed(true)
            }
            if (addFavoriteSnackBar != null) {
                addFavoriteSnackBar?.dismiss()
            }
        }

        override fun onFilmLongClick(film: Film, position: Int) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    film.isFavorite = true
                    filmDB.filmDao().insertFavoriteFilm(film)
                }
            }
            getFavoritesSnackBar().show()
        }
    }

    var isFromRefresher = false
    private val filmRefreshLister = SwipeRefreshLayout.OnRefreshListener {
        filmViewModel.getFilms()
        isFromRefresher = true
    }

    fun getFavoritesSnackBar(): Snackbar {
        addFavoriteSnackBar = Snackbar.make(filmRecycler, R.string.snackBar_added_favorites, Snackbar.LENGTH_SHORT)
            .setAnimationMode(ANIMATION_MODE_FADE)
            .setAnchorView(bottomNav)
        return addFavoriteSnackBar as Snackbar
    }

    fun getErrorScreenSnackBar(): Snackbar {
        errorSnackBar = Snackbar.make(errorLayout, R.string.snackBar_error_connection, Snackbar.LENGTH_INDEFINITE)
            .setAnimationMode(ANIMATION_MODE_SLIDE)
            .setAction(R.string.snackBar_error_retry) {
                filmViewModel.getFilms()
            }
        return errorSnackBar as Snackbar
    }

    fun pagination() {
        filmRecycler.addOnScrollListener(filmScrollListener)
    }

    /* Временно искуственная пагинация, так как из API получаю объект разом и
    нет возможности раздробить на порции получение данных, чтобы далее дробленные списки передавать в адаптер.
    Поэтому в пагинации в данном случае нет смысла, к тому же размер списка в целом меньше 50 составляет и ячейки легкие
     */

    var offsetY = 0
    private val filmScrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val y = filmRecycler.computeVerticalScrollOffset()
            if ((y - offsetY) in 10000..12000) {
                offsetY = y
                val timer = object: CountDownTimer(1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        showProgressBar()
                        recyclerView.stopScroll()
                        recyclerView.suppressLayout(true)
                    }

                    override fun onFinish() {
                        hideProgressBar()
                        recyclerView.suppressLayout(false)
                    }
                }.start()
            }
            Log.d("sss", "${y - offsetY}")
        }
    }

}
