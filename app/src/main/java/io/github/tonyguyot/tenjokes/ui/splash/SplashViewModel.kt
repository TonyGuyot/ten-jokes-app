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
package io.github.tonyguyot.tenjokes.ui.splash

import android.content.SharedPreferences
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * View model for the [SplashFragment].
 *
 * It exposes two states:
 *   - the counter to be displayed
 *   - a trigger to indicate the end of the splash screen
 *
 * @property repository a simple [SharedPreferences] object plays the role of a repository
 */
class SplashViewModel(private val repository: SharedPreferences?) : ViewModel() {

    /** Indicate how many times the app has been started before */
    private val _counter = MutableLiveData("")
    val counter: LiveData<String>
        get() = _counter

    /** If `true`, indicate it's time to go to next screen */
    private val _navigate = MutableLiveData(false)
    val navigate: LiveData<Boolean>
        get() = _navigate

    init {
        retrieveAndUpdateCounter()
        startTimer()
    }

    private fun retrieveAndUpdateCounter() {
        viewModelScope.launch {
            // read and report the current value of the counter
            val currentCounter = withContext(Dispatchers.IO) {
                repository?.getInt(APP_START_COUNTER, 0) ?: 0
            }
            _counter.value = currentCounter.toString()

            // increment and save the counter for the next call
            val newCounter = currentCounter + 1
            repository?.let {
                with (it.edit()) {
                    putInt(APP_START_COUNTER, newCounter)
                    apply()
                }
            }
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            delay(TIMER_DURATION_IN_MILLIS)
            _navigate.value = true
        }
    }

    class Factory(private val repository: SharedPreferences?) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SplashViewModel(repository) as T
        }
    }

    companion object {
        private const val APP_START_COUNTER = "pref.app.start.counter"
        private const val TIMER_DURATION_IN_MILLIS = 3000L
    }
}