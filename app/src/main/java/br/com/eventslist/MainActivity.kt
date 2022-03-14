package br.com.eventslist

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.eventslist.adapter.EventAdapter
import br.com.eventslist.adapter.EventClickListener
import br.com.eventslist.databinding.ActivityMainBinding
import br.com.eventslist.model.EventItemVO
import br.com.eventslist.model.EventVO
import br.com.eventslist.network.NetworkStatus
import br.com.eventslist.view.EventViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), EventClickListener {

    private lateinit var binding: ActivityMainBinding
    private val eventViewModel: EventViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupData()
    }

    override fun onEventClickListener(event: EventItemVO) {
        startActivity(EventDetailActivity.newIntent(this@MainActivity, event.id))
    }

    private fun setupData() {
        eventViewModel.getEvents()
        eventViewModel.progressLiveStatus.observe(this) {
            when (it) {
                is NetworkStatus.Loading -> displayLoading()
                is NetworkStatus.Success<*> -> displayData(it.data as EventVO)
                is NetworkStatus.Error -> displayError(it.errorMessage)
            }
        }
    }

    private fun displayLoading() {
        //
    }

    private fun displayData(data: EventVO) {
        binding.recyclerViewEvents.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = EventAdapter(data, this@MainActivity)
        }
    }

    private fun displayError(errorMessage: String) {
        Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
    }
}