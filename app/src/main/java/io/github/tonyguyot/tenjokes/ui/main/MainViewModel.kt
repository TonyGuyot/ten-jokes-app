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

import androidx.lifecycle.*
import io.github.tonyguyot.tenjokes.data.JokeRepositoryInterface
import io.github.tonyguyot.tenjokes.data.model.Joke
import io.github.tonyguyot.tenjokes.data.remote.generic.Resource

/**
 * View model for the [MainFragment].
 *
 * It exposes a single state:
 *   - the list of jokes to be displayed
 */
class MainViewModel(private val repository: JokeRepositoryInterface) : ViewModel() {

    /** Trigger to activate the loading of new jokes */
    private val loadTrigger = MutableLiveData<Unit>()

    /** The list of joke to be displayed */
    val list: LiveData<Resource<List<Joke>>> = loadTrigger.switchMap { loadData() }

    init {
        refresh()
    }

    /** Refresh the list to display new jokes */
    fun refresh() {
        loadTrigger.value = Unit
    }

    private fun loadData(): LiveData<Resource<List<Joke>>> = repository.observeRandomJokes()

    class Factory(private val repository: JokeRepositoryInterface) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(repository) as T
        }
    }
}