package pl.droidsonroids.toast.services

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import pl.droidsonroids.toast.data.db.DbMessage
import javax.inject.Inject
import javax.inject.Singleton

private const val MESSAGE_KEY = "message"

@Singleton
class LocalContactStorage @Inject constructor(private val sharedPreferences: SharedPreferences) : ContactStorage {
    private val gson by lazy { Gson() }

    override fun saveMessage(dbMessage: DbMessage) {
        sharedPreferences.edit().apply {
            putString(MESSAGE_KEY, gson.toJson(dbMessage))
        }.apply()
    }

    override fun readMessage(): Single<DbMessage> {
        return Maybe.fromCallable {
            val message = sharedPreferences.getString(MESSAGE_KEY, null)
            gson.fromJson(message, DbMessage::class.java)
        }
                .subscribeOn(Schedulers.io())
                .toSingle(DbMessage())
    }
}