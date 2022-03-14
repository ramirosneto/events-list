package br.com.eventslist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.eventslist.R
import br.com.eventslist.databinding.EventItemBinding
import br.com.eventslist.model.EventItemVO
import br.com.eventslist.utils.DateUtils
import com.squareup.picasso.Picasso

class EventAdapter(
    private val dataSet: List<EventItemVO>,
    private val onClickListener: EventClickListener
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: EventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: EventItemVO) {
            event.date?.let {
                binding.date.text = DateUtils.formatTimestamp(it)
                binding.date.visibility = View.VISIBLE
            } ?: run {
                binding.date.visibility = View.GONE
            }

            event.title?.let {
                binding.title.text = it
                binding.title.visibility = View.VISIBLE
            } ?: run {
                binding.title.visibility = View.GONE
            }

            event.description?.let {
                binding.description.text = it
                binding.description.visibility = View.VISIBLE
            } ?: run {
                binding.description.visibility = View.GONE
            }

            Picasso.get()
                .load(event.image)
                .placeholder(R.drawable.ic_image_not_found)
                .error(R.drawable.ic_image_not_found)
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            EventItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val event = dataSet[position]
        viewHolder.bind(event)

        viewHolder.itemView.setOnClickListener {
            onClickListener.onEventClickListener(event)
        }
    }

    override fun getItemCount() = dataSet.size
}
