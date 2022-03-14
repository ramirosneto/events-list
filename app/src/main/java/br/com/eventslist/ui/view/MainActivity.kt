package br.com.eventslist.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.eventslist.R
import br.com.eventslist.ui.adapter.EventAdapter
import br.com.eventslist.ui.adapter.EventClickListener
import br.com.eventslist.databinding.ActivityMainBinding
import br.com.eventslist.data.model.EventItemVO
import br.com.eventslist.data.model.EventVO
import br.com.eventslist.utils.NetworkStatus
import br.com.eventslist.ui.viewmodel.EventViewModel
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
                is NetworkStatus.Loading -> showLoading()
                is NetworkStatus.Success<*> -> displayData(it.data as EventVO)
                is NetworkStatus.Error -> displayError(it.errorMessage)
            }
        }
    }

    private fun showLoading() {
        binding.shimmer.startShimmer()
    }

    private fun hideLoading() {
        binding.shimmer.stopShimmer()
        binding.shimmer.hideShimmer()
    }

    private fun displayData(data: EventVO) {
        hideLoading()

        binding.recyclerViewEvents.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = EventAdapter(data, this@MainActivity)
        }
    }

    private fun displayError(errorMessage: String) {
        Toast.makeText(this@MainActivity, R.string.requisition_error, Toast.LENGTH_LONG).show()
        hideLoading()
    }
}