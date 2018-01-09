package pl.droidsonroids.toast.app.events

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.droidsonroids.toast.databinding.ItemEventSpeakerBinding
import pl.droidsonroids.toast.viewmodels.event.EventSpeakerItemViewModel

class EventSpeakersAdapter : RecyclerView.Adapter<EventSpeakerViewHolder>() {
    private var eventSpeakerViewModels: List<EventSpeakerItemViewModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventSpeakerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventSpeakerBinding.inflate(inflater, parent, false)
        return EventSpeakerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventSpeakerViewHolder, position: Int) {
        holder.bind(eventSpeakerViewModels[position])
    }

    override fun getItemCount() = eventSpeakerViewModels.size

    fun setData(newEventSpeakerViewModels: List<EventSpeakerItemViewModel>) {
        val diffCallback = EventSpeakerItemDiffCallback(eventSpeakerViewModels, newEventSpeakerViewModels)
        val diff = DiffUtil.calculateDiff(diffCallback)
        eventSpeakerViewModels = newEventSpeakerViewModels
        diff.dispatchUpdatesTo(this)
    }

    class EventSpeakerItemDiffCallback(
            private val oldList: List<EventSpeakerItemViewModel>,
            private val newList: List<EventSpeakerItemViewModel>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }
}

