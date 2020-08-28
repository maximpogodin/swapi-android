# SWAPI Android Test App
## Contents
1. [Description](#description)
2. [Plugins, library and stuff](#plugins-library-and-stuff)
3. [SDK specification](#sdk-specification)
4. [Setting up the project](#setting-up-the-project)
5. [APK](#apk)
6. [App in action](#app-in-action)
## Description
The test project was developed in Android Studio using Kotlin. The build is based on the Gradle build system.
## Plugins, library and stuff:
- Kotlin - ver. 1.4.0
- [GSON](https://github.com/google/gson) (Google open source library) for serialization-deserialization JSON - ver. 2.8.5
- [Volley](https://github.com/google/volley) (Google open source HTTP library) - ver. 1.1.1
- The application was launched on the Pixel XL emulator API 29 (Android 10.0)
## SDK specification:
- Target sdk version 30
- Min sdk version 21
- Build tools version 30.0.1
## Setting up the project
1. Start Android Studio
2. Select "Get from Version Control"
![](https://github.com/maximpogodin/swapi-android/blob/master/screenshots/step1.png)
3. Configure properties:
    * Version control: Git
    * URL: https://github.com/maximpogodin/swapi-android
    * Directory: your_path</br>
![](https://github.com/maximpogodin/swapi-android/blob/master/screenshots/step2.png)
4. Click on Clone button
5. Open project
___
Since build.gradle already exists, Android Studio will set everything else up automatically.
## APK
The build result is [here](https://github.com/maximpogodin/swapi-android/tree/master/apk).
## App in action
- Fragment **Planets**</br>
![](https://github.com/maximpogodin/swapi-android/blob/master/screenshots/fragment-planets.jpg)
- Fragment **Details** with no loaded spinners</br>
![](https://github.com/maximpogodin/swapi-android/blob/master/screenshots/fragment-details-not-loaded.jpg)
- Fragment **Details**. Choose item from spinner</br>
![](https://github.com/maximpogodin/swapi-android/blob/master/screenshots/fragment-details-choose-item.jpg)
- Fragment **Details** with loaded spinners</br>
![](https://github.com/maximpogodin/swapi-android/blob/master/screenshots/fragment-details-loaded.jpg)
- Fragment **Statistics** with no loaded spinners</br>
![](https://github.com/maximpogodin/swapi-android/blob/master/screenshots/fragment-stats-not-loaded.jpg)
- Fragment **Statistics** with loaded spinners</br>
![](https://github.com/maximpogodin/swapi-android/blob/master/screenshots/fragment-stats-loaded.jpg)
