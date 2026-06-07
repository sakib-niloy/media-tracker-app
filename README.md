MovieListApp

Minimal Android Jetpack Compose app that stores movies to watch and watched using Room.

How to use

1. Open Android Studio and choose "Open". Select the `android-app` folder in this workspace.
2. Let Android Studio sync Gradle. You may need to install the Android Gradle Plugin/SDK matching versions.
3. Build and run on an emulator or device.

Features

- "To Watch" list: alphabetical, with checkboxes. Checking moves the movie to Watched.
- "Watched" list: alphabetical, shows poster, year, director, and casts.
- Add movies via the FAB; supply poster as a URL (loads via Coil).

Notes

- This scaffold focuses on core functionality; improve error handling, image caching, and import/export as next steps.
