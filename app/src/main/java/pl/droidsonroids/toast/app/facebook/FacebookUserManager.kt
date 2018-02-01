package pl.droidsonroids.toast.app.facebook

import com.facebook.AccessToken

class FacebookUserManager : UserManager {
    override fun getUserInfo(): UserInfo? {
        return AccessToken.getCurrentAccessToken()?.run {
            UserInfo(token, userId)
        }
    }
}