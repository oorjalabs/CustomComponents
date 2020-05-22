# CustomComponents
Custom components used across my Android projects

[![](https://jitpack.io/v/adityabhaskar/CustomComponents.svg)](https://jitpack.io/#adityabhaskar/CustomComponents)

## Adding to project

Add jitpack repository in project `build.gradle` at the end of list of repositories:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to module `build.gradle`

```gradle
dependencies {
    implementation 'com.github.adityabhaskar:CustomComponents:Tag'
}
```

## Other usage notes

If using custom preferences, ideally extend settings fragment from `CustomSettingsFragment`. The custom fragment handles `onDisplayPreferenceDialog` to show the custom preference dialogs.

Otherwise, use `CustomPreferenceUtils.displayPreferenceDialog` method to let the library handle displaying dialogs. Sample usage:
```kotlin
    override fun onDisplayPreferenceDialog(preference: Preference?) {
        val fm = parentFragment?.childFragmentManager
        
        if(CustomPreferenceUtils.displayPreferenceDialog(preference, fm, this))
            return
        
        // Do any other preference dialog handling here, if needed.
        
        super.onDisplayPreferenceDialog(preference)
    }
```
