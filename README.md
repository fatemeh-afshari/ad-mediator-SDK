# Ad Mediator SDK
Android Ad Mediator SDK to manage two ad networks (Tapsell and UnityAds), using Kotlin, Hilt, Coroutines, Retrofit, Moshi, Room and Clean Architecture.

[![](https://jitpack.io/v/fatemeh-afshari/ad-mediator-SDK.svg)](https://jitpack.io/#fatemeh-afshari/ad-mediator-SDK)
![License MIT](https://img.shields.io/badge/MIT-9E9F9F?style=flat-square&label=License)
![Android minimuml version](https://img.shields.io/badge/21+-9E9F9F?style=flat-square&label=Minimum&logo=android)

Download
--------
Add in project build.gradle:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

// App build.gradle
dependencies {
    implementation 'com.github.fatemeh-afshari:ad-mediator-SDK:latestVersion'

}
```
## Usage
--------
This library consists of three functions: `initializeAdNetworks`, `requestAd` and `showAd`. First of all you should use Hilt librsry and inject it into your Application class, then initialze the ad networks:


```kotlin
@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        MainScope().launch(Dispatchers.IO){
            repository.initializeAdNetworks(this@App).collect()
        }
    }

}
```
After initialzing library you should request ad into your view model. Once an ad become ready you can show it:

```kotlin
    fun requestAd() {
        viewModelScope.launch {
            repository.requestAd().collect { result ->
                when (result) {
                    is AdRequestResultState.Loading -> TODO()
                    is AdRequestResultState.adFailed -> TODO()
                    is AdRequestResultState.adIsReady -> {
                        result.data?.let { showAd(it) }
                    }
                }

            }
        }
    }

     fun showAd(ad: AdRequestDto) {
        viewModelScope.launch {
            repository.showAd(ad).collect { result ->
                when (result) {
                    AdShowState.Completed -> TODO()
                    AdShowState.Failed -> TODO()
                    AdShowState.Loading -> TODO()
                    AdShowState.Started -> TODO()
                }
            }
        }
    }
```
**Note:** see [Example](app/src/main/java/com/test/admediator) for more detail.
