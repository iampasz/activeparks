package com.app.activeparks.ui.routeActive.fragments.saveRouteActive

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.activeparks.MainActivity
import com.app.activeparks.data.model.track.PointsTrack
import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.track.fragments.changeMapTrack.TrackChangeMapFragment
import com.app.activeparks.util.AddressOfDoubleUtil
import com.app.activeparks.util.EasyTextWatcher
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.extention.FileHelper
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentSaveTrackBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline

class SaveRouteActiveFragment : Fragment() {

    private lateinit var binding: FragmentSaveTrackBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveTrackBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    private fun openFragment(fragment: Fragment) =
        (requireActivity() as? MainActivity)?.addFragment(fragment)
}