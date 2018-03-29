package pl.droidsonroids.toast.services

import io.reactivex.Single
import pl.droidsonroids.toast.data.db.DbMessage

interface ContactStorage {
    fun saveMessage(dbMessage: DbMessage)
    fun readMessage(): Single<DbMessage>
}