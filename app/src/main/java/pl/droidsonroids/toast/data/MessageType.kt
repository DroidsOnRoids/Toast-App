package pl.droidsonroids.toast.data

enum class MessageType {
    I_WANT_TO, TALK, REWARD, SPONSOR;

    companion object {
        operator fun get(ordinal: Int): MessageType {
            return values()[ordinal]
        }
    }
}