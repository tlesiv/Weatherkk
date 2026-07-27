# Weather App 🌤

A clean, elegant Android weather application built with **Jetpack Compose**, following modern **Clean Architecture + MVVM**. Shows current conditions, hourly and 7-day forecasts, and detailed metric cards in a cohesive warm-toned UI.

## 📸 Sneak Peek

### Main Screen
Current weather, today's hourly forecast and the weekly outlook at a glance.

![Main Screen](screenshots/today.png)

### City Search
Minimal custom search dialog, styled to match the app's theme.

![City Search](screenshots/search.png)

### Detailed Metrics
Feels-like, UV index, wind, precipitation, visibility, pressure, humidity and dew point — each with a short human-readable description.

![Detailed Metrics](screenshots/cards.png)

## ⚡ Features

- **🌤 Current Weather:** Temperature, condition, min/max for the day and "feels like" comparison.
- **⏱ Hourly Forecast:** Scrollable hourly strip with custom condition icons.
- **📅 7-Day Forecast:** Weekday names with condition icons and temperature ranges.
- **🃏 Metric Cards:** UV index, wind speed/gusts/direction, precipitation (snow-aware), visibility, pressure, humidity and dew point.
- **🔍 City Search:** Instant forecast reload with keyboard search action support.
- **🎨 Cohesive Design:** Warm brown-toned cards, custom Ubuntu typography, custom launcher icon.
- **🔐 Safe Secrets:** API key is kept out of version control via `local.properties` + `BuildConfig`.

## 🛠 Tech Stack

- **Language:** [Kotlin](https://kotlinlang.org/) (100%)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture:** Clean Architecture (data / domain / ui) + MVVM
- **Networking:** Retrofit 2 + OkHttp (logging interceptor) + kotlinx.serialization
- **DI:** Hilt (KSP)
- **Async & State:** Kotlin Coroutines + StateFlow
- **Images:** Coil
- **Data Source:** [WeatherAPI](https://www.weatherapi.com/)

## 📂 Project Structure

The project is divided into three layers with dependencies pointing inwards — the domain layer knows nothing about Android, networking or UI:

```text
app/src/main/java/com/example/weather/
├── WeatherApplication.kt        # @HiltAndroidApp entry point
├── data/                        # Networking & data layer
│   ├── remote/
│   │   ├── dto/                 # DTOs mirroring WeatherAPI JSON
│   │   ├── WeatherApi.kt        # Retrofit interface
│   │   ├── WeatherRemoteDataSource.kt
│   │   ├── RetrofitWeatherRemoteDataSource.kt
│   │   └── WeatherMapper.kt     # DTO → domain mapping
│   └── repository/
│       └── WeatherRepositoryImpl.kt
├── di/
│   └── AppModule.kt             # Hilt module (wiring the whole graph)
├── domain/                      # Pure Kotlin, no framework imports
│   ├── model/Weather.kt         # Typed domain models
│   ├── repository/WeatherRepository.kt
│   └── usecase/GetForecastUseCase.kt
└── ui/                          # Presentation layer
    ├── theme/Fonts.kt
    ├── components/              # Reusable composable cards & rows
    │   ├── DialogSearch.kt
    │   ├── TodayWeather.kt      # (+ hourly strip)
    │   ├── WeekWeather.kt
    │   ├── WeatherCards.kt
    │   └── WeatherIcons.kt
    └── main/
        ├── MainActivity.kt
        ├── MainViewModel.kt     # StateFlow<WeatherUiState>
        ├── WeatherUiState.kt
        └── WeatherApp.kt        # Root composable
```

## 🚀 Getting Started

1. **Clone the repository.**
2. **Get an API Key:** Go to [weatherapi.com](https://www.weatherapi.com/) and generate your free key.
3. **Configure the key:** In the **root directory of the project** (next to `settings.gradle.kts`), open `local.properties` and add:
   ```properties
   WEATHER_API_KEY=your_api_key_here
   ```
4. **Sync Gradle** and run the app on a device or emulator (min SDK 27).

## 👨‍💻 Author

Developed by **Taras Lesiv**.
