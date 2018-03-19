package pl.droidsonroids.toast.utils

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import pl.droidsonroids.toast.BuildConfig

const val BASE_URL_KEY = BuildConfig.FLAVOR + "BaseUrl"
const val IMAGE_URL_KEY = BuildConfig.FLAVOR + "ImageUrl"

val baseUrl: String
    get() = FirebaseRemoteConfig.getInstance()
            .getString(BASE_URL_KEY)

val baseImageUrl: String
    get() = FirebaseRemoteConfig.getInstance()
            .getString(IMAGE_URL_KEY)
