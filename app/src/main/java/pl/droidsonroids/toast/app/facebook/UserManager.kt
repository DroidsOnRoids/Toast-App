package pl.droidsonroids.toast.app.facebook

import pl.droidsonroids.toast.data.UserInfo

interface UserManager {
    fun getUserInfo(): UserInfo?
}