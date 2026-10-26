# Smart Pantry Manager
A Java Android app that stores pantry ingredients and suggests recipes only when every ingredient is available in sufficient quantity.

## Requirements
Android Studio, Android SDK 35, JDK 17 and internet access to download the Gradle plugin/AndroidX dependency during first sync.

## Setup
1. Open this folder in Android Studio.
2. Let Android Studio sync the included Gradle wrapper and install any missing SDK components it requests.
3. Sync the project, choose an Android emulator/device with API 23+, and run the `app` configuration.
4. On first installation, 20 recipes are seeded in the local `pantry.db` database.

## Storage
SQLiteOpenHelper was chosen because SQLite works offline and is covered in Chapter 3 of the study guide. `pantry` stores user ingredients, `recipes` stores recipe names and methods, and `recipe_ingredients` stores the requirements for each recipe. Settings use SharedPreferences.

## Test walkthrough
- Add 2 pieces of bread and 2 eggs: Egg Toast should appear.
- Edit eggs to 1: Egg Toast should disappear.
- Edit eggs back to 2: Egg Toast should return.
- Delete bread: Egg Toast should disappear.
- Close and reopen the app: remaining pantry items should persist.
- Enter an empty name, zero quantity, or invalid date: saving should be blocked.
- Use `tomatoes` for `tomato` or 1 kg for 1000 g to check basic normalization.

## Limitations
Units are supported only within compatible groups (pieces, mass, volume). Different ingredient forms (e.g. raw/dried) aren't converted. The expiry setting labels pantry items as Soon or Expired, but does not send phone notifications. Pantry list uses a ListView with a custom BaseAdapter.

## Project repository
https://github.com/Joshiieeeee/SmartPantryManager
