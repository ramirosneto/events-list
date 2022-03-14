package br.com.eventslist.network

import br.com.eventslist.model.EventItemVO
import br.com.eventslist.model.EventVO
import retrofit2.http.GET
import retrofit2.http.Path

interface EventService {

    @GET("events")
    suspend fun getEvents(): EventVO

    @GET("events/{id}")
    suspend fun getEventDetail(@Path("id") id: String): EventItemVO
}
