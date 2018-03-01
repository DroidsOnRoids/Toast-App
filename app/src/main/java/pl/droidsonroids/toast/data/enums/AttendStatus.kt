package pl.droidsonroids.toast.data.enums

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.util.*

enum class AttendStatus {
    ATTENDING, UNSURE, DECLINED, ERROR;
}

class AttendStatusAdapter : TypeAdapter<AttendStatus>() {
    override fun read(reader: JsonReader): AttendStatus {
        return if (reader.hasNext()) {
            val nextString = reader.nextString()
            AttendStatus.valueOf(nextString.toUpperCase(Locale.ROOT))
        } else {
            AttendStatus.DECLINED
        }
    }

    override fun write(writer: JsonWriter, value: AttendStatus) {
        writer.value(value.name.toLowerCase(Locale.ROOT))
    }

}