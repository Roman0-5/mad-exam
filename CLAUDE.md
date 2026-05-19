# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Procrasti-not** is an Android/Kotlin productivity app built with Jetpack Compose. It follows **MVVM + Clean Architecture** across four feature screens.

## Build Commands/

```bash
# Build
./gradlew assembleDebug
./gradlew assembleRelease
./gradlew installDebug

# Test
./gradlew test
./gradlew test --tests "at.ac.hcw.procrastinot.SomeClass"
./gradlew test --tests "at.ac.hcw.procrastinot.SomeClass.methodName"
./gradlew connectedAndroidTe?st      # requires device/emulator
./gradlew test jacocoTestReport     # with coverage

# Lint & clean
./gradlew lint
./gradlew clean build
```

## Architecture

Three layers with clear separation:

**Presentation** — `screen1/` through `screen4/`, each containing a ViewModel and Compose UI. Navigation is handled via Jetpack Navigation Compose with Hilt integration from `MainActivity`.

**Domain / Data** — `data/Repository.kt` is the interface. Implementations pull from:
- `data/source/local/` — Room DAOs and entities (KSP-generated)
- `data/source/network/` — Retrofit-based remote data source

**DI** — Hilt modules in `di/`. `MainApplication` is the `@HiltAndroidApp` entry point; Timber is initialized there for DEBUG builds. The `debug/` source set provides `HiltTestActivity` for instrumented DI tests.

## Key Libraries

| Category     | Library                                                       |
|--------------|---------------------------------------------------------------|
| UI           | Jetpack Compose BOM 2024.12.01, Material3, Coil 2.7.0         |
| Architecture | Lifecycle/ViewModel 2.8.7, Navigation Compose                 |
| DI           | Hilt 2.53.1 + KSP                                             |
| Local DB     | Room 2.6.1 + KSP                                              |
| Network      | Retrofit 2.9.0 + Kotlinx Serialization 1.8.0                  |
| Async        | Coroutines 1.10.1                                             |
| Testing      | JUnit 4, Truth, Mockito, Espresso, Compose UI test, Hilt test |

All versions are centralized in `gradle/libs.versions.toml`.

## Logcat Logging

Always add Timber log statements for observable runtime events so behavior is visible in Logcat during development. Use `Timber.d()` for debug-level events and `Timber.e()` for errors. Required logging sites:

- **Lifecycle**: log each `Activity`/`Fragment` lifecycle method that is overridden (e.g. `onCreate`, `onResume`, `onDestroy`) and each Compose `DisposableEffect` or `LaunchedEffect` entry/exit that represents a lifecycle boundary.
- **Navigation**: log every `navController.navigate()` call and `NavBackStackEntry` destination change, including the destination route and any arguments passed.
- **ViewModel**: log `init {}`, `onCleared()`, and any significant state transitions (e.g. loading → success/error).
- **Data layer**: log Repository function calls at entry and on result (success or error path).

Use the enclosing class name as the tag via Timber's automatic tag inference (no manual `TAG` constant needed). Example: `Timber.d("Navigating to %s with id=%s", route, id)`.

## Development Workflow Instructions

- **Changelog**: Create `CHANGELOG.md` at the project root if it doesn't exist. After every code change, append an entry with a timestamp (ISO 8601), a brief description of what was added or changed, and which Android/Kotlin concept it demonstrates (e.g. "Room DAO", "Hilt module", "StateFlow in ViewModel").
- **Concept annotation**: In the CHANGELOG.md append a short summary when writing or explaining code, always state which concept is being introduced or applied (e.g. "This uses Hilt's `@ViewModelScoped` to tie the repository lifetime to the ViewModel"). 

Example changelog entry format:
```
## [2026-05-19T14:30:00] Added Task entity and DAO
- Concept: Room — defines `@Entity` data class and `@Dao` interface with suspend query functions
```

## Build Configuration Notes

- minSdk 21, targetSdk/compileSdk 35, Java 17
- KSP incremental processing enabled (`ksp.incremental.apt=true` in `gradle.properties`)
- Test coverage collection enabled in debug builds; Proguard minification in release
- Edge-to-edge UI enabled in `MainActivity`
- Configuration cache enabled — avoid `project.afterEvaluate` patterns that break it 