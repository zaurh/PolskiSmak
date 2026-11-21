Polski Smak is a recipe app where you can find popular recipes for Polish dishes.

[<img src="https://i.hizliresim.com/rbn6uq0.png" height="100">](https://play.google.com/store/apps/details?id=com.zaurh.polskismak)


# Preview

![App Screenshot](https://i.hizliresim.com/8cotxxq.png)

https://github.com/zaurh/PolskiSmak/assets/56722725/ab20d67e-6d85-4418-a80a-3eae743b0db2


# This app contains:

Kotlin & Jetpack Compose (Material 3, edge-to-edge, light/dark theme)

MVI architecture – every screen has a `Contract` (State / Intent / Effect), a ViewModel built on a small `MviViewModel` base, and a stateless Composable

Clean Architecture (domain models & repository interfaces, data implementations)

Fully offline – all recipes and images ship inside the app, no internet permission

DataStore (favorites, theme and onboarding state)

Type-safe Navigation Compose

Dagger Hilt + KSP (dependency injection)

Unit tests for the ViewModels and bundled data

English & Polish (per-app language on Android 13+)

## License
```
Copyright (c) 2024 Zaur Hasanov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
