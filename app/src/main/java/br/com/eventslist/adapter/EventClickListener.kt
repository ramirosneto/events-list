package br.com.eventslist.adapter

import br.com.eventslist.model.EventItemVO

interface EventClickListener {

    fun onEventClickListener(event: EventItemVO)
}
