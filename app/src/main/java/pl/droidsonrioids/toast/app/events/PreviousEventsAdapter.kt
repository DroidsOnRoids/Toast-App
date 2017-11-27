package pl.droidsonrioids.toast.app.events

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.droidsonrioids.toast.R

class PreviousEventsAdapter : RecyclerView.Adapter<PreviousEventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PreviousEventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_previous_event, parent, false))

    override fun onBindViewHolder(holder: PreviousEventViewHolder?, position: Int) {
        //TODO fill this with data after repository implemented
    }

    override fun getItemCount(): Int = 5 //TODO fill this with data after repository implemented

}