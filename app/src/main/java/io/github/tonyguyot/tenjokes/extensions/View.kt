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
package io.github.tonyguyot.tenjokes.extensions

import android.view.View

/** Do not make the [View] visible */
fun View.hide() {
    visibility = View.GONE
}

/** Make the [View] visible */
fun View.show() {
    visibility = View.VISIBLE
}

/** Make the [View] visible if the given [predicate] evaluates to `true` or hide it otherwise */
fun View.showIf(predicate: () -> Boolean) {
    if (predicate()) show() else hide()
}
