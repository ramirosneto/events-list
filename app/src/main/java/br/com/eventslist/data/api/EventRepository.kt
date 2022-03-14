package br.com.eventslist.data.api

import br.com.eventslist.data.model.EventCheckInRequest

class EventRepository(private val service: EventService) {

    suspend fun getEvents() = service.getEvents()

    suspend fun getEventDetail(id: String) = service.getEventDetail(id)

    suspend fun checkIn(request: EventCheckInRequest) = service.checkIn(request)
}
