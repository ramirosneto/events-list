package br.com.eventslist.ui.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import br.com.eventslist.R
import br.com.eventslist.databinding.ActivityEventDetailBinding
import br.com.eventslist.data.model.EventCheckInRequest
import br.com.eventslist.data.model.EventItemVO
import br.com.eventslist.utils.NetworkStatus
import br.com.eventslist.ui.viewmodel.EventViewModel
import br.com.eventslist.utils.CurrencyUtils
import br.com.eventslist.utils.DateUtils
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
                is NetworkStatus.Loading -> showLoading()
                is NetworkStatus.Success<*> -> displayData(it.data as EventItemVO)
                is NetworkStatus.Error -> displayError(it.errorMessage)
            }
        }
    }

    private fun showLoading() {
        binding.shimmer.showShimmer(true)
        binding.shimmer.visibility = View.VISIBLE
        binding.coordinatorLayout.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.shimmer.hideShimmer()
        binding.shimmer.stopShimmer()
        binding.shimmer.visibility = View.GONE
        binding.coordinatorLayout.visibility = View.VISIBLE
    }

    private fun displayData(event: EventItemVO) {
        eventViewModel.progressLiveStatus.removeObservers(this)
        hideLoading()

        event.date?.let {
            binding.date.text = DateUtils.formatTimestamp(it)
            binding.date.visibility = View.VISIBLE
        } ?: run {
            binding.date.visibility = View.GONE
        }

        event.price?.let {
            binding.price.text = CurrencyUtils.formatPrice(it)
            binding.price.visibility = View.VISIBLE
        } ?: run {
            binding.price.visibility = View.GONE
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

        binding.icShare.setOnClickListener {
            shareContent(event)
        }

        binding.btnCheckIn.setOnClickListener {
            if (binding.checkInContent.visibility == View.GONE) {
                binding.checkInContent.visibility = View.VISIBLE
                binding.nestedScrollView.post {
                    binding.nestedScrollView.fullScroll(View.FOCUS_DOWN)
                }
            } else {
                if (validateFields()) {
                    val request = EventCheckInRequest(
                        event.id,
                        binding.etName.text.toString(),
                        binding.etEmail.text.toString()
                    )
                    doCheckIn(request)
                }
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

    private fun validateFields(): Boolean {
        if (binding.etName.text.length < 5) {
            binding.etName.error = getString(R.string.msg_validate_name)
            return false
        }

        if (Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text).matches().not()) {
            binding.etEmail.error = getString(R.string.msg_validate_email)
            return false
        }

        return true
    }

    private fun doCheckIn(request: EventCheckInRequest) {
        eventViewModel.checkIn(request)
        eventViewModel.progressLiveStatus.observe(this) {
            when (it) {
                is NetworkStatus.Loading -> showLoading()
                is NetworkStatus.Success<*> -> checkInSuccess()
                is NetworkStatus.Error -> checkInError(it.errorMessage)
            }
        }
    }

    private fun checkInSuccess() {
        hideLoading()
        binding.checkInContent.visibility = View.GONE
        binding.btnCheckIn.isEnabled = false
        binding.btnCheckIn.text = getString(R.string.txt_check_in_successfull)
    }

    private fun checkInError(errorMessage: String) {
        Toast.makeText(this@EventDetailActivity, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun shareContent(event: EventItemVO) {
        val formattedDate = event.date?.let { DateUtils.formatTimestamp(event.date) } ?: ""
        val price = event.price?.let { CurrencyUtils.formatPrice(event.price) } ?: ""
        val content = "${event.title}\n\n" +
                "Data: $formattedDate\n" +
                "Valor: $price\n\n" +
                "${event.description}"
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, content)
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "Share To:"))
    }

    private fun displayError(errorMessage: String) {
        Toast.makeText(this@EventDetailActivity, errorMessage, Toast.LENGTH_LONG).show()
        hideLoading()
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