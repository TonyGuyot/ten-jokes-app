package io.github.tonyguyot.tenjokes.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.tonyguyot.tenjokes.data.JokeRepository
import io.github.tonyguyot.tenjokes.data.model.Joke
import io.github.tonyguyot.tenjokes.data.remote.JokeRemoteDataSource
import io.github.tonyguyot.tenjokes.data.remote.Resource
import io.github.tonyguyot.tenjokes.data.remote.RestJokeService
import io.github.tonyguyot.tenjokes.data.remote.provideService
import io.github.tonyguyot.tenjokes.databinding.FragmentMainBinding
import io.github.tonyguyot.tenjokes.extensions.showIf
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // inflate the layout for this fragment
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        context ?: return binding.root

        // retrieve or create the associated view model
        val viewModel: MainViewModel by viewModels {
            MainViewModel.Factory(createRepository())
        }

        // setup the list
        binding.jokeList.layoutManager = LinearLayoutManager(activity)
        val adapter = JokeListItemAdapter { view, joke ->
            // TODO: display details dialog box
        }
        binding.jokeList.adapter = adapter

        // subscribe to data changes
        viewModel.list.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                Timber.d("[LiveData] New result for list: %s", result.status.name)
                handleResult(binding, adapter, result)
            }
        }

        // return root element
        return binding.root
    }

    private fun handleResult(
        binding: FragmentMainBinding,
        adapter: JokeListItemAdapter,
        result: Resource<List<Joke>>
    ) {
        // if loading in progress, show the progress bar
        binding.progressBar.showIf { result.isLoading() }

        // if loading failed, show the error message
        binding.errorMessage.showIf { result.isError() }

        // if data available, display it
        binding.jokeList.showIf { result.isSuccess() }
        result.data?.let { adapter.submitList(it) }
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