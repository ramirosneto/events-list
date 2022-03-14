package br.com.eventslist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import br.com.eventslist.databinding.ActivityEventDetailBinding
import br.com.eventslist.model.EventItemVO
import br.com.eventslist.network.NetworkStatus
import br.com.eventslist.utils.DateUtils
import br.com.eventslist.view.EventViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailBinding
    private val eventViewModel: EventViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_detail)

        intent.getStringExtra(EVENT_ID_EXTRA)?.let {
            setupData(it)
        } ?: run {
            finish()
        }
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
    }

    private fun setupData(eventId: String) {
        eventViewModel.getEventDetail(eventId)
        eventViewModel.progressLiveStatus.observe(this) {
            when (it) {
                is NetworkStatus.Loading -> displayLoading()
                is NetworkStatus.Success<*> -> displayData(it.data as EventItemVO)
                is NetworkStatus.Error -> displayError(it.errorMessage)
            }
        }
    }

    private fun displayLoading() {
        //
    }

    private fun displayData(event: EventItemVO) {
        event.date?.let {
            binding.date.text = DateUtils.formatTimestamp(it)
            binding.date.visibility = View.VISIBLE
        } ?: run {
            binding.date.visibility = View.GONE
        }

        event.title?.let {
            binding.title.text = it
            binding.collapsingToolbar.title = it
            binding.title.visibility = View.VISIBLE
        } ?: run {
            binding.title.visibility = View.GONE
        }

        event.description?.let {
            binding.description.text = it
            binding.description.visibility = View.VISIBLE
        } ?: run {
            binding.description.visibility = View.GONE
        }

        Picasso.get().load(event.image).into(binding.image)

        event.latitude?.let {
            event.longitude?.let {
                setupMapLocation(LatLng(event.latitude, event.longitude))
            }
        }
    }

    private fun setupMapLocation(latLng: LatLng) {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
            googleMap.addMarker(MarkerOptions().position(latLng))
        }
    }

    private fun displayError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        finish()
    }

    companion object {
        private const val EVENT_ID_EXTRA = "EVENT_ID_EXTRA"

        fun newIntent(context: Context, eventId: String): Intent {
            val intent = Intent(context, EventDetailActivity::class.java)
            intent.putExtra(EVENT_ID_EXTRA, eventId)
            return intent
        }
    }
}