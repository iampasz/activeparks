package com.app.activeparks.ui.clubs.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.app.activeparks.data.model.sportevents.EventList
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.ui.event.activity.EventFragment
import com.app.activeparks.ui.event.interfaces.OnItemClickListener
import com.app.activeparks.ui.event.viewmodel.EventViewModel
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentCalendarClubBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class ClubEventsCalendarFragment : Fragment(), LocationListener, OnItemClickListener {

    lateinit var binding: FragmentCalendarClubBinding
    private val viewModel: EventViewModel by activityViewModel()
    private var locationManager: LocationManager? = null
    private var nameList: MutableList<ItemEvent> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarClubBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    @SuppressLint("NotifyDataSetChanged")
    fun setAdapter(events: EventList) {
        nameList = events.items as MutableList<ItemEvent>

    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }
        locationManager?.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 50f, this)
    }

    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        viewModel.filterCordinate(latitude, longitude)
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}



    @SuppressLint("CommitTransaction")
    override fun onItemClick(position: Int) {


        parentFragmentManager.beginTransaction()
            .add(R.id.constrain_events_container, EventFragment()).commit()
    }
}