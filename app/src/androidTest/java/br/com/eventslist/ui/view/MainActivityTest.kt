package br.com.eventslist.ui.view

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import br.com.eventslist.R
import br.com.eventslist.di.EventModule
import br.com.eventslist.ui.viewmodel.EventViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4ClassRunner::class)
@ExperimentalCoroutinesApi
class MainActivityTest {

    @get:Rule
    var mActivityRule: IntentsTestRule<MainActivity> = IntentsTestRule(
        MainActivity::class.java
    )

    lateinit var scenario: ActivityScenario<MainActivity>

    //private val viewModel: EventViewModel = mockk(relaxed = true)

    @Before
    fun setUp() {
        //
    }

    @Test
    fun selectLanguageAndCheck() {
        scenario = ActivityScenario.launch((MainActivity::class.java))

        scenario.onActivity {
            scenario.moveToState(Lifecycle.State.STARTED)
            //onView(withId(android.R.id.tool))
        }
    }

    @After
    fun finish() {
        stopKoin()
    }
}