package pl.droidsonrioids.toast.app.events

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_events.*
import pl.droidsonrioids.toast.R

class EventsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_events, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(previousEventsList) {
            adapter = PreviousEventsAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

}

class PreviousEventsAdapter : RecyclerView.Adapter<PreviousEventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PreviousEventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_previous_event, parent, false))

    override fun onBindViewHolder(holder: PreviousEventViewHolder?, position: Int) {
        //TODO fill this with data after repository implemented
    }

    override fun getItemCount(): Int = 5 //TODO fill this with data after repository implemented

}

class PreviousEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}
