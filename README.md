# TENJOKES

## Description

Display a list of random jokes.

## Technology stack

Uses the single activity pattern (one activity for the whole app, screens are represented by fragments) using the Jetpack Navigation Component.

Architecture is a standard MVVM as described in the Android Archicture Component guide from Google. LiveData is used to handle the view states and coroutines are used to perform any background operation. Data source is managed using the repository pattern.

Communication with the Rest server is done with Retrofit. There is no local persistence.

The splash screen does not use a real repository but a simple shared preferences.

