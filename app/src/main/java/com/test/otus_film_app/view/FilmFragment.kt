package com.test.otus_film_app.view
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.util.Access.Companion.DETAILS_FRAGMENT_BUNDLE_KEY
import com.test.otus_film_app.viewmodel.FilmViewModel
import kotlin.properties.Delegates

class FilmFragment : Fragment(R.layout.fragment_film), FilmAdapter.OnItemClickListener {

    private val filmViewModel: FilmViewModel by lazy {
        ViewModelProvider(this)[FilmViewModel::class.java]
    }
    private lateinit var filmRecycler: RecyclerView
    var filmAdapter: FilmAdapter? = null
    private var dataList: ArrayList<Film> = ArrayList()
    private val bundle = Bundle()

    // Черный по умлочанию
    private var headerColor = -16777216

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filmRecycler = view.findViewById(R.id.film_recycler)

//        if (bundle.containsKey("colorKey")) {
//            if(savedInstanceState != null) {
//                headerColor = bundle.getInt("colorKey")
//            }
//        }

        filmViewModel.getFilms()
        fitRecyclerView()
    }


    private fun fitRecyclerView() {
        filmRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
            AppCompatResources.getDrawable(requireContext(), R.drawable.line_divider_recycler)?.let {
                dividerItemDecoration.setDrawable(it)
            }
            addItemDecoration(dividerItemDecoration)

            filmViewModel.filmData.observe(viewLifecycleOwner) {
                dataList = it.filmList
                filmAdapter = FilmAdapter(dataList, this@FilmFragment, requireContext(), headerColor)
                filmAdapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                filmRecycler.adapter = filmAdapter

            }
        }
    }

    override fun onItemClick(film: Film, headerText: TextView) {
        headerColor = ContextCompat.getColor(requireContext(), R.color.purple_200)
        headerText.setTextColor(headerColor)
        bundle.putSerializable(DETAILS_FRAGMENT_BUNDLE_KEY, film)
        parentFragmentManager.commit {
            replace<DetailsFragment>(R.id.fragment_main_container, DETAILS_FRAGMENT_BUNDLE_KEY, bundle)
            addToBackStack(null)
            setReorderingAllowed(true)
        }
    }

//    override fun onPause() {
//        super.onPause()
//        bundle.putInt("colorKey", headerColor)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        if (savedInstanceState != null) {
//            headerColor = savedInstanceState.get("colorKey") as Int
//        }
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putInt("colorKey", headerColor)
//    }

}
