package pl.droidsonroids.toast.app.speakers

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.droidsonroids.toast.databinding.ItemSpeakerTalkBinding
import pl.droidsonroids.toast.viewmodels.speaker.SpeakerTalkViewModel

class SpeakerTalksAdapter : RecyclerView.Adapter<SpeakerTalkViewHolder>() {
    private var speakerTalkViewModels: List<SpeakerTalkViewModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakerTalkViewHolder {
        val binding = ItemSpeakerTalkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpeakerTalkViewHolder(binding)
    }


    override fun getItemCount() = speakerTalkViewModels.size

    override fun onBindViewHolder(holder: SpeakerTalkViewHolder, position: Int) {
        holder.bind(speakerTalkViewModels[position])
    }

    fun setData(newSpeakerTalkViewModels: List<SpeakerTalkViewModel>) {
        speakerTalkViewModels = newSpeakerTalkViewModels
        notifyDataSetChanged()
    }
}