package com.test.otus_film_app.view.details_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.util.Constants.Companion.DETAILS_FRAGMENT_BUNDLE_KEY


class DetailsFragment : Fragment(R.layout.fragment_details) {

    lateinit var postDetailImg: ImageView
    lateinit var nameDetailText: TextView
    lateinit var yearDetailText: TextView
    lateinit var countriesDetailText: TextView
    lateinit var genreDetailText: TextView
    lateinit var durationDetailText: TextView
    lateinit var premierDetailText: TextView
    lateinit var shareImg: ImageButton
    var toolBar: CollapsingToolbarLayout? = null
    var bottomNavigationView: BottomNavigationView? = null

    private lateinit var film: Film

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationView = activity?.findViewById(R.id.bottom_navigation_view)
        bottomNavigationView?.visibility = View.GONE

        checkOrientation(view)
        postDetailImg = view.findViewById(R.id.detail_img)
        nameDetailText = view.findViewById(R.id.detail_name)
        yearDetailText = view.findViewById(R.id.detail_year)
        countriesDetailText = view.findViewById(R.id.detail_countries)
        genreDetailText = view.findViewById(R.id.detail_genre)
        durationDetailText = view.findViewById(R.id.detail_duration)
        premierDetailText = view.findViewById(R.id.detail_premier)
        shareImg = view.findViewById(R.id.detail_share)


        film = arguments?.get(DETAILS_FRAGMENT_BUNDLE_KEY) as Film

        shareImg.setOnClickListener(shareClickListener)
        setDataDetail(film)
    }

    fun checkOrientation(view: View) {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            toolBar = view.findViewById(R.id.detail_toolbar)
        } else {
            toolBar = null
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setDataDetail(film: Film) {
        Glide.with(this).load(film.posterUrlPreview).into(postDetailImg)
        nameDetailText.text = film.nameRu ?: "?"
        yearDetailText.text = film.year?.toString() ?: "?"
        durationDetailText.text = film.duration?.toString() ?: "?"
        premierDetailText.text = film.premiereRu ?: "?"

        countriesDetailText.text = buildString {
            film.countryList.forEach{ append("${it?.country} ") }
        }

        genreDetailText.text = buildString {
            film.genreList.forEach { append("${it?.genre} ") }
        }

        toolBar?.title = film.nameRu ?: ""
    }

    private val shareClickListener = View.OnClickListener {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, film.posterUrlPreview ?: "")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}