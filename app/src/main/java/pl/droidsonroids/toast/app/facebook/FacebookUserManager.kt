package pl.droidsonroids.toast.app.facebook

import com.facebook.AccessToken
import pl.droidsonroids.toast.data.UserInfo

class FacebookUserManager : UserManager {
    override fun getUserInfo(): UserInfo? {
        return AccessToken.getCurrentAccessToken()?.run {
            UserInfo("Bearer $token", userId)
        }
    }
}