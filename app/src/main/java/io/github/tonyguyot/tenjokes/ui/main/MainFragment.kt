/*
 * Copyright (C) 2021 Tony Guyot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.tonyguyot.tenjokes.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.tonyguyot.tenjokes.R
import io.github.tonyguyot.tenjokes.data.JokeRepository
import io.github.tonyguyot.tenjokes.data.model.Joke
import io.github.tonyguyot.tenjokes.data.remote.JokeRemoteDataSource
import io.github.tonyguyot.tenjokes.data.remote.generic.Resource
import io.github.tonyguyot.tenjokes.data.remote.RestJokeService
import io.github.tonyguyot.tenjokes.data.remote.generic.provideService
import io.github.tonyguyot.tenjokes.databinding.DialogJokeDetailsBinding
import io.github.tonyguyot.tenjokes.databinding.FragmentMainBinding
import io.github.tonyguyot.tenjokes.extensions.showIf
import timber.log.Timber

/**
 * Display the main screen showing the list of jokes.
 *
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

        // setup the UI element
        binding.jokeList.layoutManager = LinearLayoutManager(activity)
        val adapter = JokeListItemAdapter { joke -> showDetails(joke) }
        binding.jokeList.adapter = adapter
        binding.swipeRefreshJokeList.setOnRefreshListener {
            binding.swipeRefreshJokeList.isRefreshing = false
            viewModel.refresh()
        }
        binding.retryButton.setOnClickListener { viewModel.refresh() }

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

    private fun showDetails(joke: Joke) {
        context ?: return
        val dialogBinding = DialogJokeDetailsBinding.inflate(
                LayoutInflater.from(context),
                null,
                false
            )
        dialogBinding.joke = joke
        dialogBinding.title = requireContext().getString(R.string.details_dialog_title, joke.id)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.details_dialog_ok) { dialog, _ -> dialog.dismiss() }
            .create()
        dialog.show()
    }

    private fun handleResult(
        binding: FragmentMainBinding,
        adapter: JokeListItemAdapter,
        result: Resource<List<Joke>>
    ) {
        // if loading in progress, show the progress bar
        binding.progressBar.showIf { result.isLoading() }

        // if loading failed, show the error message & retry button
        binding.errorMessage.showIf { result.isError() }
        binding.retryButton.showIf { result.isError() }

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
         * @return A new instance of fragment [MainFragment].
         */
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}