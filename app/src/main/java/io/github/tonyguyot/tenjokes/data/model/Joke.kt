package io.github.tonyguyot.tenjokes.data.model

data class Joke(
    val id: Int,
    val type: String,
    val setup: String,
    val punchline: String
)
