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
package io.github.tonyguyot.tenjokes.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import io.github.tonyguyot.tenjokes.data.model.Joke
import io.github.tonyguyot.tenjokes.data.remote.JokeRemoteDataSource
import io.github.tonyguyot.tenjokes.data.remote.generic.Resource
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class JokeRepository(private val remote: JokeRemoteDataSource) : JokeRepositoryInterface {

    /**
     * Retrieve the data as a [Resource] in a LiveData stream, so that the caller will be
     * notified of any change during the data retrieval process.
     *
     * @return the possible returned values are:
     *   - [Resource.Status.SUCCESS] with data from the network
     *   - [Resource.Status.ERROR] if an error has occurred
     *   - [Resource.Status.LOADING] if the process is still in progress
     */
    override fun observeRandomJokes(): LiveData<Resource<List<Joke>>> = liveData(Dispatchers.IO) {
        Timber.d("Loading...")
        emit(Resource.loading<List<Joke>>())
        emit(remote.fetchNewJokes())
    }
}