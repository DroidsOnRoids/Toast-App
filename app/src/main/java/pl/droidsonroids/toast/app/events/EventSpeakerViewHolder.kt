package pl.droidsonroids.toast.app.events

import android.support.v7.widget.RecyclerView
import pl.droidsonroids.toast.databinding.ItemEventSpeakerBinding
import pl.droidsonroids.toast.viewmodels.event.EventSpeakerItemViewModel

class EventSpeakerViewHolder(private val binding: ItemEventSpeakerBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(eventSpeakerItemViewModel: EventSpeakerItemViewModel) {
        binding.eventSpeakerItemViewModel = eventSpeakerItemViewModel
    }
}