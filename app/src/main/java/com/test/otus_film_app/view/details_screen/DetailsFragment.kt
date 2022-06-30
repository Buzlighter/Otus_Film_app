package com.test.otus_film_app.view.details_screen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.test.otus_film_app.App
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.util.*
import com.test.otus_film_app.util.Constants.Companion.DETAILS_BUNDLE
import com.test.otus_film_app.util.Constants.Companion.FROM_NOTIFICATION
import com.test.otus_film_app.viewmodel.PushServiceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

const val CALENDAR_GROUP_VISIBILITY_TAG = "CALENDAR_GROUP_VISIBILITY_TAG"
const val CALENDAR_DATE_TAG = "CALENDAR_DATE_TAG"
const val TOPIC = "/topics/myTopic"

class DetailsFragment : Fragment(R.layout.fragment_details) {

    lateinit var postDetailImg: ImageView
    lateinit var nameDetailText: TextView
    lateinit var yearDetailText: TextView
    lateinit var countriesDetailText: TextView
    lateinit var genreDetailText: TextView
    lateinit var durationDetailText: TextView
    lateinit var premierDetailText: TextView
    lateinit var shareImg: ImageButton
    lateinit var calendarViewGroup: LinearLayout
    lateinit var calendarText: TextView
    lateinit var calendarSetButton: ImageButton
    lateinit var checkBoxWatchLater: CheckedTextView

    private val pushViewModel: PushServiceViewModel by viewModels()

    var fromNotification = false

    var toolBar: CollapsingToolbarLayout? = null
    var bottomNavigationView: BottomNavigationView? = null

    private lateinit var film: Film

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fromNotification = arguments?.getBoolean(FROM_NOTIFICATION) ?: false
    }

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
        checkBoxWatchLater = view.findViewById(R.id.watch_later_checkbox)
        calendarViewGroup = view.findViewById(R.id.calendar_group)
        calendarText = view.findViewById(R.id.calendar_text)
        calendarSetButton = view.findViewById(R.id.calendar_btn)


        if (!fromNotification) {
            film = arguments?.get(DETAILS_BUNDLE) as Film
        } else {
            film = arguments?.get(FILM_EXTRA) as Film
        }

        setDataDetail(film)
        shareImg.setOnClickListener(shareClickListener)
        checkBoxWatchLater.setOnClickListener(watchLaterClickListener)
        calendarSetButton.setOnClickListener(setCalendarListener)

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

    private val watchLaterClickListener = View.OnClickListener {
        checkBoxWatchLater.toggle()
        if (checkBoxWatchLater.isChecked) {
            calendarViewGroup.visibility = View.VISIBLE
            if (film.notificationDate != null) {
                calendarText.text = film.notificationDate
            }
        } else {
            calendarViewGroup.visibility = View.INVISIBLE
        }
    }


    private val setCalendarListener = View.OnClickListener {
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val sendNotificationAlarmService = SendNotificationAlarmService(requireContext(), calendar)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val finalDate = sendNotificationAlarmService.dateFormat(day,month, year)
                calendar.set(year, month, day)
                calendarText.text = finalDate
                setDateIntoDb(finalDate)

                val title = "Посмотреть фильм"
                val message = film.nameRu ?: ""

                sendNotificationAlarmService.send(
                    title, message, film, pushViewModel,
                    currentDay, currentMonth + 1, currentYear,
                    day, month + 1, year
                )
            },
            currentYear, currentMonth, currentDay)
        datePickerDialog.show()
    }

    private fun setDateIntoDb(finalDate: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            film.notificationDate = finalDate
            App.filmDB.filmDao().insertWatchLaterFilm(film)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CALENDAR_GROUP_VISIBILITY_TAG, calendarViewGroup.visibility)
        outState.putCharSequence(CALENDAR_DATE_TAG, calendarText.text)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        calendarViewGroup.visibility = savedInstanceState?.getInt(CALENDAR_GROUP_VISIBILITY_TAG) ?: View.INVISIBLE
        calendarText.text = savedInstanceState?.getCharSequence(CALENDAR_DATE_TAG)
    }
}