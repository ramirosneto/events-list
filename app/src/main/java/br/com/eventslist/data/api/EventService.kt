package br.com.eventslist.data.api

import br.com.eventslist.data.model.EventCheckInRequest
import br.com.eventslist.data.model.EventItemVO
import br.com.eventslist.data.model.EventVO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventService {

    @GET("events")
    suspend fun getEvents(): EventVO

    @GET("events/{id}")
    suspend fun getEventDetail(@Path("id") id: String): EventItemVO

    @POST("checkin")
    suspend fun checkIn(@Body request: EventCheckInRequest)
}
