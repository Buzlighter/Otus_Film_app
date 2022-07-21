package com.test.otus_film_app.view.film_list_screen
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
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
import com.test.otus_film_app.App.Companion.appComponent
import com.test.otus_film_app.R
import com.test.otus_film_app.di.modules.KinpoiskApiModule
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.model.KinopoiskResponse
import com.test.otus_film_app.util.Constants.Companion.DETAILS_BUNDLE
import com.test.otus_film_app.util.Constants.Companion.FROM_FILM_LIST_NOTIFY_BUNDLE
import com.test.otus_film_app.util.Constants.Companion.FROM_NOTIFICATION
import com.test.otus_film_app.util.FILM_EXTRA
import com.test.otus_film_app.util.FilmClickListener
import com.test.otus_film_app.util.FilmItemDecoration
import com.test.otus_film_app.util.Resource
import com.test.otus_film_app.view.common.FilmAdapter
import com.test.otus_film_app.view.details_screen.DetailsFragment
import com.test.otus_film_app.viewmodel.FilmViewModel
import com.test.otus_film_app.viewmodel.FilmViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FilmListFragment : Fragment(R.layout.fragment_filmlist) {

    @Inject
    lateinit var filmViewModelFactory: FilmViewModelFactory

    val filmViewModel: FilmViewModel by viewModels { filmViewModelFactory }

    private lateinit var filmRecycler: RecyclerView
    private lateinit var filmAdapter: FilmAdapter
    private var filmList: List<Film>? = null

    private var bottomNav: BottomNavigationView? = null
    var addFavoriteSnackBar: Snackbar? = null
    var errorSnackBar: Snackbar? = null
    lateinit var progressBar: ProgressBar
    lateinit var errorLayout: ConstraintLayout
    lateinit var pullToRefresh: SwipeRefreshLayout
    var fromNotification = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fromNotification = arguments?.getBoolean(FROM_NOTIFICATION) ?: false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filmRecycler = view.findViewById(R.id.film_recycler)
        bottomNav = activity?.findViewById(R.id.bottom_navigation_view)
        progressBar = view.findViewById(R.id.progress_bar)
        errorLayout = view.findViewById(R.id.error_layout)
        pullToRefresh = view.findViewById(R.id.pull_to_refresh)

        bottomNav?.visibility = View.VISIBLE

        if (fromNotification) {
            transferFromNotification(savedInstanceState)
            fromNotification = false
        }

        appComponent.filmListFragmentComponentBuilder()
            .kinopoiskModule(KinpoiskApiModule())
            .build()
            .inject(this)

        fitRecyclerView()

        fillFilmList()
    }

    private fun transferFromNotification(savedInstanceState: Bundle?) {
        val notifyBundle = Bundle()
        val filmFromNotification = arguments?.getSerializable(FILM_EXTRA)
        notifyBundle.apply {
            putSerializable(FILM_EXTRA, filmFromNotification)
            putBoolean(FROM_NOTIFICATION, fromNotification)
        }
        parentFragmentManager.commit {
            if (savedInstanceState == null) {
                replace<DetailsFragment>(R.id.fragment_main_container, FROM_FILM_LIST_NOTIFY_BUNDLE, notifyBundle)
                addToBackStack(null)
                setReorderingAllowed(true)
            }
        }
    }

    private fun fitRecyclerView() {
        setScreenLayout()
        pullToRefresh.setOnRefreshListener(filmRefreshLister)
        filmRecycler.apply {
            filmAdapter = FilmAdapter(filmListener)
            adapter = filmAdapter
            setHasFixedSize(true)
        }
    }

    private fun fillFilmList() {
        filmViewModel.filmData.observe(viewLifecycleOwner) { response ->
            checkRequest(response)
        }
    }

    private fun checkRequest(response: Resource<KinopoiskResponse>) {
        filmViewModel.cacheData.observe(viewLifecycleOwner) { cacheData ->
            filmList = cacheData
            filmAdapter.differ.submitList(cacheData)
        }
        when (response) {
            is Resource.Success -> {
                hideProgressBar()
                bottomNav?.visibility = View.VISIBLE
                pullToRefresh.visibility = View.VISIBLE
                errorLayout.visibility = View.GONE
                pullToRefresh.isRefreshing = false
                if (errorSnackBar != null)  errorSnackBar?.dismiss()
            }
            is Resource.Error -> {
                hideProgressBar()
                pullToRefresh.isRefreshing = false
                lifecycleScope.launch {
                    var dbIsEmpty = false
                    withContext(Dispatchers.IO)  {
                        if (appComponent.getFilmDao().checkIsDbContain() == 0)  dbIsEmpty = true
                    }
                    withContext(Dispatchers.Main) {
                        if (dbIsEmpty)  {
                            getInstanceErrorScreenSnackBar(null).show()
                            errorLayout.visibility = View.VISIBLE
                            pullToRefresh.visibility = View.GONE
                            bottomNav?.visibility = View.INVISIBLE
                        }
                        else {
                            getInstanceErrorScreenSnackBar(bottomNav).show()
                            bottomNav?.visibility = View.VISIBLE
                            pullToRefresh.visibility = View.VISIBLE
                            errorLayout.visibility = View.GONE
                        }
                    }
                }

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

    private fun showProgressBar() {
        if(!isFromRefresher) progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private val filmListener = object: FilmClickListener {
        override fun onFilmClick(film: Film, position: Int) {
            val bundle = Bundle()
            bundle.putSerializable(DETAILS_BUNDLE, film)
            parentFragmentManager.commit {
                replace<DetailsFragment>(R.id.fragment_main_container, DETAILS_BUNDLE, bundle)
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
                    appComponent.getFilmDao().insertFavoriteFilm(film)
                }
            }
            getInstanceFavoritesSnackBar().show()
        }
    }

    var isFromRefresher = false
    private val filmRefreshLister = SwipeRefreshLayout.OnRefreshListener {
        filmViewModel.getFilms()
        isFromRefresher = true
    }

    fun getInstanceFavoritesSnackBar(): Snackbar {
        addFavoriteSnackBar = Snackbar.make(filmRecycler, R.string.snackBar_added_favorites, Snackbar.LENGTH_SHORT)
            .setAnimationMode(ANIMATION_MODE_FADE)
            .setAnchorView(bottomNav)
        return addFavoriteSnackBar as Snackbar
    }

    fun getInstanceErrorScreenSnackBar(view: View?): Snackbar {
        errorSnackBar = Snackbar.make(errorLayout, R.string.snackBar_error_connection, Snackbar.LENGTH_INDEFINITE)
            .setAnimationMode(ANIMATION_MODE_SLIDE)
            .setAnchorView(view)
            .setAction(R.string.snackBar_error_retry) {
                filmViewModel.getFilms()
            }
        return errorSnackBar as Snackbar
    }


    private fun fillListFromRemote(remoteDataYear: String?) {
        val remoteList = filmList?.filter {
            (it.year ?: 0) > (remoteDataYear?.toInt() ?: 0)
        }
        filmAdapter.differ.submitList(remoteList)
        filmAdapter.notifyDataSetChanged()
    }
}
