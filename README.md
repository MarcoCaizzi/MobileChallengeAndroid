# MobileChallengeAndroid

## 1. General Description

MobileChallengeAndroid is an application that allows you to search, filter, and explore cities, displaying relevant information such as weather and map location. The goal is to offer a smooth and fast experience to find cities and consult additional details.

---

## 2. Solution Approach and Architecture

- **Architecture:**  
  MVVM (Model-View-ViewModel) is used with Jetpack Compose for the UI, Room for local persistence, Paging 3 for efficient pagination, and Hilt for dependency injection.  
  This architecture separates responsibilities, facilitates scalability, maintainability, and testability, and enables a reactive and declarative UI.

- **Download and Storage:**  
  On startup, the app downloads the cities file and stores it in a local database (Room).  
  **Justification:**
  - Allows efficient prefix searches using indexes, even with very large lists.
  - Avoids memory issues that would arise if the entire list was kept in memory.
  - Facilitates persistence and efficient pagination.

- **Favorites Persistence:**  
  Favorites are saved in `SharedPreferences`.  
  **Justification:**
  - It is a lightweight and fast solution to store a small set of identifiers.
  - Allows decoupling the favorites logic from the main database.

- **Weather and Maps:**  
  External APIs are consumed to display current weather and static/dynamic maps.

- **Adaptive UI:**  
  The interface automatically adapts to portrait/landscape orientation, showing separate or combined screens as appropriate.

---

## 3. Main Features

- Download the list of cities from a remote gist.
- Filter results by prefix (case-insensitive).
- Search optimized for speed.
- Display cities in a scrollable list, sorted alphabetically (city, country code).
- Reactive UI while typing the filter.
- Update the list with every change in the filter.
- Allow filtering only favorites.
- Each cell shows name, country code, and coordinates.
- Mark/unmark as favorite.
- Tapping a city navigates the map centered on its coordinates.
- Button to open an additional information screen for the city.
- Information screen with weather data and static map.
- Dynamic UI: separate screens in portrait, single screen in landscape.
- Remember favorites between app launches.

---

## 4. Roadmap

If the project evolves, several architectural and maintainability improvements could be implemented. For instance, all hardcoded strings could be externalized to resource files to enable localization and facilitate text management. Similarly, all endpoint URLs and configuration values could be centralized in dedicated resource or configuration files to streamline environment management and reduce the risk of inconsistencies. Additionally, implementing instrumentation and integration tests would allow for comprehensive validation of UI flows and end-to-end scenarios, ensuring robustness across different device states and user interactions. Introducing a refresh mechanism on the city detail screen would further enhance the user experience by enabling real-time data updates. These enhancements would collectively improve scalability, maintainability, and overall code quality.

## 5. How to Build and Run the Project

### a) Load the API Keys

Before building, you need to add your API keys in the `local.properties` file (at the root of the project):
```
MAPS_API_KEY=your_google_maps_key 
WEATHER_API_KEY=your_weather_api_key
```
These keys are required to display maps and obtain weather data.

### b) Build and Run

1. Open the project in Android Studio.
2. Sync the project with Gradle.
3. Connect a device or use an emulator.
4. Click **Run** to build and launch the app.

---

## 6. Running the Tests

To run unit and UI tests:

- From Android Studio:  
  Go to the **Project** window, right-click on the `app` module, and select **Run 'All Tests'**.

- From the terminal:
  ```bash
  ./gradlew testDebugUnitTest
  ./gradlew connectedAndroidTest
