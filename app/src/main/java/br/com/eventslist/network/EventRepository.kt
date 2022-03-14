package br.com.eventslist.network

class EventRepository(private val service: EventService) {

    suspend fun getEvents() = service.getEvents()

    suspend fun getEventDetail(id: String) = service.getEventDetail(id)
}
