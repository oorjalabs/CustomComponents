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

```groovy
dependencies {
    implementation 'io.github.adityabhaskar:CustomComponents:<version>'
}
```
See badge above for current `version` number.


## Other usage notes


### Custom Preferences

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

[Customisable resource values and attributes](/customcomponents/src/main/java/net/c306/customcomponents/preference/README.md)

### Update notes fragment

Details on how to use [are here](/customcomponents/src/main/java/net/c306/customcomponents/updatenotes/README.md).

### Confirmation dialog

Details on how to use [are here](/customcomponents/src/main/java/net/c306/customcomponents/confirmation/README.md).

## Apps using this library

- [Todo.txt for Android](https://play.google.com/store/apps/details?id=net.c306.ttsuper)
- [Autoconvert](https://play.google.com/store/apps/details?id=net.c306.autoconvert)
- [Accelereader for Instapaper](https://play.google.com/store/apps/details?id=net.c306.ari)
- [PhotoPress](https://play.google.com/store/apps/details?id=net.c306.photopress)
