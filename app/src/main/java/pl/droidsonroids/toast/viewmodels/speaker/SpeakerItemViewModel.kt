package pl.droidsonroids.toast.viewmodels.speaker

import pl.droidsonroids.toast.data.dto.ImageDto

class SpeakerItemViewModel(
        val id: Long,
        val name: String,
        val job: String,
        val avatar: ImageDto,
        private val action: (Long) -> Unit
) {
    fun onClick() {
        action(id)
    }

    override fun equals(other: Any?): Boolean {
        return (other as? SpeakerItemViewModel)?.let {
            id == it.id
                    && name == it.name
                    && job == it.job
                    && avatar == it.avatar
        } ?: false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + job.hashCode()
        result = 31 * result + avatar.hashCode()
        return result
    }


}