package io.github.tonyguyot.tenjokes.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import io.github.tonyguyot.tenjokes.R
import io.github.tonyguyot.tenjokes.data.JokeRepository
import io.github.tonyguyot.tenjokes.data.remote.JokeRemoteDataSource
import io.github.tonyguyot.tenjokes.data.remote.RestJokeService
import io.github.tonyguyot.tenjokes.data.remote.provideService

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        // Retrieve or create the associated view model
        val viewModel: MainViewModel by viewModels {
            MainViewModel.Factory(createRepository())
        }

        // Subscribe to data changes
        val title = root.findViewById<TextView>(R.id.mainTitle)
        viewModel.list.observe(viewLifecycleOwner) { jokes ->
            if (jokes != null) {
                title.text = jokes.status.name
            }
        }

        // return root element
        return root
    }

    // temporary => replace by dependency injection
    private fun createRepository(): JokeRepository {
        val service = provideService(RestJokeService::class.java, RestJokeService.ENDPOINT)
        val remote = JokeRemoteDataSource(service)
        return JokeRepository(remote)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of this fragment.
         *
         * @return A new instance of fragment MainFragment.
         */
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}