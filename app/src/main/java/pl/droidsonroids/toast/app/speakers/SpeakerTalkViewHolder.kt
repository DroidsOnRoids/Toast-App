package pl.droidsonroids.toast.app.speakers

import android.support.v7.widget.RecyclerView
import pl.droidsonroids.toast.databinding.ItemSpeakerTalkBinding
import pl.droidsonroids.toast.viewmodels.speaker.SpeakerTalkViewModel

class SpeakerTalkViewHolder(private val binding: ItemSpeakerTalkBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(speakerTalkViewModel: SpeakerTalkViewModel) {
        binding.speakerTalkViewModel = speakerTalkViewModel
        binding.executePendingBindings()
    }
}