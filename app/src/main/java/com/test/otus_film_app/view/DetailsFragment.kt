package com.test.otus_film_app.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.model.Genre
import com.test.otus_film_app.util.Access.Companion.DETAILS_FRAGMENT_BUNDLE_KEY


class DetailsFragment : Fragment(R.layout.fragment_details) {

    lateinit var postDetailImg: ImageView
    lateinit var nameDetailText: TextView
    lateinit var yearDetailText: TextView
    lateinit var countriesDetailText: TextView
    lateinit var genreDetailText: TextView
    lateinit var durationDetailText: TextView
    lateinit var premierDetailText: TextView
    lateinit var shareImg: ImageButton

    private var film: Film? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postDetailImg = view.findViewById(R.id.detail_img)
        nameDetailText = view.findViewById(R.id.detail_name)
        yearDetailText = view.findViewById(R.id.detail_year)
        countriesDetailText = view.findViewById(R.id.detail_countries)
        genreDetailText = view.findViewById(R.id.detail_genre)
        durationDetailText = view.findViewById(R.id.detail_duration)
        premierDetailText = view.findViewById(R.id.detail_premier)
        shareImg = view.findViewById(R.id.detail_share)

        film = this.arguments?.get(DETAILS_FRAGMENT_BUNDLE_KEY) as Film

        shareImg.setOnClickListener(shareClickListener)
        setDataDetail(film)
    }

    var tempResult = ""
    @SuppressLint("SetTextI18n")
    private fun setDataDetail(film: Film?) {
        Glide.with(this).load(film?.posterUrlPreview).into(postDetailImg)
        nameDetailText.text = film?.nameRu
        yearDetailText.text = film?.year.toString()
        durationDetailText.text = "${film?.duration.toString()} мин"
        premierDetailText.text = film?.premiereRu

        film?.countryList?.forEach { it -> tempResult += "${it?.country.toString()} " }
        countriesDetailText.text = tempResult

        tempResult = ""
        film?.genreList?.forEach { it -> tempResult += "${it?.genre.toString()} " }
        genreDetailText.text = tempResult
    }

    private val shareClickListener = View.OnClickListener {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, film?.posterUrlPreview ?: "")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}