package com.test.otus_film_app.view.watch_later_screen

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.otus_film_app.App
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.util.FilmClickListener
import com.test.otus_film_app.util.PushService
import com.test.otus_film_app.util.SendNotificationAlarmService
import com.test.otus_film_app.viewmodel.PushServiceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.notify
import java.util.*
import kotlin.collections.ArrayList


class WatchLaterListFragment : Fragment(R.layout.fragment_watch_later_list) {
    private lateinit var watchLaterRecycler: RecyclerView
    private lateinit var watchLaterAdapter: WatchLaterAdapter

    private var watchLaterFilmList = mutableListOf<Film>()

    private val pushViewModel: PushServiceViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        watchLaterRecycler= view.findViewById(R.id.watch_later_recycler)

        fitRecyclerView()
        fillWatchList()
    }

    private fun fillWatchList() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                App.filmDB.filmDao().getAll().forEach {
                    if (it.notificationDate != null && !watchLaterFilmList.contains(it)) {
                        watchLaterFilmList.add(it)
                    }
                }
            }
            withContext(Dispatchers.Main) {
                watchLaterAdapter.differ.submitList(watchLaterFilmList)
            }
        }
    }


    private fun fitRecyclerView() {
        watchLaterRecycler.apply {
            watchLaterAdapter = WatchLaterAdapter(clickListener)
            adapter = watchLaterAdapter
            setHasFixedSize(false)
        }

        watchLaterRecycler.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun setDateIntoDb(finalDate: String, film: Film) {
        lifecycleScope.launch(Dispatchers.IO) {
            film.notificationDate = finalDate
            App.filmDB.filmDao().insertWatchLaterFilm(film)
        }
    }

    private val clickListener = object: FilmClickListener {
        override fun onFilmClick(film: Film, position: Int) {
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
                    setDateIntoDb(finalDate, film)

                    val oldFilm = watchLaterFilmList.find { it == film }
                    watchLaterFilmList[watchLaterFilmList.indexOf(oldFilm)] = film
                    watchLaterAdapter.differ.submitList(watchLaterFilmList)
                    watchLaterAdapter.notifyItemChanged(position)

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

        override fun onFilmLongClick(film: Film, position: Int) {

        }

    }
}