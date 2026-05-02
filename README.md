# UrlOpener

UrlOpener is a small Android app for opening URLs quickly and keeping frequently used links grouped
and ready to launch.

## Features

- open an entered URL in an external Android app;
- save links with readable names;
- organize saved links into groups;
- add, edit, and delete groups;
- add, edit, and delete links inside groups;
- optionally confirm link and group deletion;
- optionally confirm opening a saved link;
- light and dark themes;
- edge-to-edge screen layout.

## Tech Stack

- Kotlin 2.3.21
- Android Gradle Plugin 9.2.0
- Jetpack Compose with Compose BOM 2026.04.01
- Material 3
- Kotlin Coroutines
- Room 2.8.4
- DataStore Preferences 1.2.1

## Android Configuration

- Application ID: `software.kanunnikoff.urlopener`
- Minimum SDK: 24
- Target SDK: 37
- Compile SDK: 37
- Version name: `2.0.0`
- Version code: `2`

## Build

Build a debug APK:

```bash
./gradlew :app:assembleDebug
```

Run unit tests:

```bash
./gradlew :app:testDebugUnitTest
```

Build instrumented tests:

```bash
./gradlew :app:assembleDebugAndroidTest
```

Build a release APK:

```bash
./gradlew :app:assembleRelease
```

## License

This project is distributed under the MIT License. See [LICENSE](LICENSE) for details.
