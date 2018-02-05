package pl.droidsonroids.toast.app.speakers

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.droidsonroids.toast.databinding.ItemSpeakerTalkBinding
import pl.droidsonroids.toast.viewmodels.speaker.SpeakerTalkViewModel

class SpeakerTalksAdapter : RecyclerView.Adapter<SpeakerTalkViewHolder>() {
    private var speakerTalkViewModels: List<SpeakerTalkViewModel> = listOf()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakerTalkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSpeakerTalkBinding.inflate(inflater, parent, false)
        return SpeakerTalkViewHolder(binding)
    }

    override fun getItemId(position: Int): Long {
        return speakerTalkViewModels[position].id
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