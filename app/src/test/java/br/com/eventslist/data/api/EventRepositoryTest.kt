package br.com.eventslist.data.api

import br.com.eventslist.data.model.EventCheckInRequest
import br.com.eventslist.data.model.EventItemVO
import br.com.eventslist.data.model.EventVO
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class EventRepositoryTest {

    lateinit var repository: EventRepository

    @Mock
    lateinit var service: EventService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = EventRepository(service)
    }

    @Test
    fun `get events test`() {
        runBlocking {
            Mockito.`when`(service.getEvents()).thenReturn(EventVO())
            val response = repository.getEvents()
            assertEquals(EventVO(), response)
        }
    }

    @Test
    fun `get event detail test`() {
        runBlocking {
            val eventId = "1"
            val event: EventItemVO = mockk(relaxed = true)
            Mockito.`when`(service.getEventDetail(eventId)).thenReturn(event)
            val response = repository.getEventDetail(eventId)
            assertEquals(event, response)
        }
    }

    @Test
    fun `get event check in test`() {
        runBlocking {
            val request : EventCheckInRequest = mockk(relaxed = true)
            Mockito.`when`(service.checkIn(request)).thenReturn(Unit)
            val response = repository.checkIn(request)
            assertEquals(Unit, response)
        }
    }
}