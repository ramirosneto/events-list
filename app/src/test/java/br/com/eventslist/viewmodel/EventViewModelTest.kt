package br.com.eventslist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.eventslist.utils.TestCoroutineRule
import br.com.eventslist.data.api.EventRepository
import br.com.eventslist.data.api.EventService
import br.com.eventslist.data.model.EventItemVO
import br.com.eventslist.data.model.EventVO
import br.com.eventslist.ui.viewmodel.EventViewModel
import br.com.eventslist.utils.NetworkStatus
import io.mockk.mockk
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class EventViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var service: EventService

    @Mock
    private lateinit var observer: Observer<NetworkStatus>

    private var eventItem: EventItemVO = mockk(relaxed = true)
    private lateinit var repository: EventRepository
    private lateinit var viewModel: EventViewModel

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = EventRepository(service)
        viewModel = EventViewModel(repository)
    }

    @Test
    fun `WHEN fetching events ok THEN return an object successfully`() {
        val response = EventVO()
        testCoroutineRule.runBlockingTest {
            viewModel.progressLiveStatus.observeForever(observer)
            `when`(repository.getEvents()).thenReturn(response)

            viewModel.getEvents()
            assertNotNull(viewModel.getEvents())
        }
    }

    @Test
    fun `WHEN fetching event detail ok THEN return an object successfully`() {
        testCoroutineRule.runBlockingTest {
            val eventId = "99"
            viewModel.progressLiveStatus.observeForever(observer)
            `when`(repository.getEventDetail(eventId)).thenReturn(eventItem)

            viewModel.getEventDetail(eventId)
            assertNotNull(viewModel.getEventDetail(eventId))
        }
    }
}