package br.com.eventslist.ui.adapter

import br.com.eventslist.data.model.EventItemVO

interface EventClickListener {

    fun onEventClickListener(event: EventItemVO)
}
