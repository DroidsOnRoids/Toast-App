package pl.droidsonroids.toast.app.facebook

interface UserManager {
    fun getUserInfo(): UserInfo?
}

data class UserInfo(
        val token: String,
        val userId: String
)