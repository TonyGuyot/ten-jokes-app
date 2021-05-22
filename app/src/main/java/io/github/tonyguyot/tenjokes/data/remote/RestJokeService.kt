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
package io.github.tonyguyot.tenjokes.data.remote

import io.github.tonyguyot.tenjokes.data.model.Joke
import retrofit2.Response
import retrofit2.http.GET

/**
 * Official joke API access points
 */
interface RestJokeService {

    @GET("random_ten")
    suspend fun getTenRandomJokes(): Response<List<Joke>>

    companion object {
        const val ENDPOINT = "https://official-joke-api.appspot.com/"
    }
}