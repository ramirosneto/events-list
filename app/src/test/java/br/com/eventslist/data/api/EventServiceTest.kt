package br.com.eventslist.data.api

import br.com.eventslist.BuildConfig
import com.google.gson.Gson
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EventServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: EventService
    private lateinit var gson: Gson

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        gson = Gson()
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(EventService::class.java)
    }

    @Test
    fun `get events api test`() {
        runBlocking {
            val mockResponse = MockResponse()
            mockWebServer.enqueue(mockResponse.setBody("[]"))
            val response = apiService.getEvents()
            assertNotNull(response.isNotEmpty())
        }
    }

    @Test
    fun `get event detail api test`() {
        runBlocking {
            val mockResponse = MockResponse()
            mockWebServer.enqueue(mockResponse.setBody("[]"))
            val response = apiService.getEventDetail("1")
            assertNotNull(response)
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
