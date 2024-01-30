package com.app.activeparks.ui.routeActive.fragments.detailsRouteActive

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.data.model.track.PointsTrack
import com.app.activeparks.ui.active.ActivityForActivity
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.TypeActivity
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentRouteActiveBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline

class DetailsRouteActiveFragment : Fragment() {

    private lateinit var binding: FragmentRouteActiveBinding

    private val viewModel: DetailsRouteActiveViewModel by viewModel()
    private var mapsViewController: MapsViewController? = null
    private var polyLine = Polyline()

    private var trackId: String = ""

    companion object {
        fun newInstance(trackId: String): DetailsRouteActiveFragment {
            val fragment = DetailsRouteActiveFragment()
            val args = Bundle()
            args.putString("track_id", trackId)
            fragment.arguments = args
            fragment.trackId = trackId
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRouteActiveBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var trackId = arguments?.getString("track_id") ?: ""

        with(binding) {
            viewModel.getRouteActive(trackId)
            setListener()
            observe()
        }
    }

    private fun FragmentRouteActiveBinding.setListener() {
        ivBack.setOnClickListener { requireActivity().onBackPressed() }


        bRouteActive.setOnClickListener {
            startActivity(Intent(requireContext(), ActivityForActivity::class.java))
        }
    }


    private fun observe() {
        with(viewModel) {
            routeActive.observe(viewLifecycleOwner) { response ->

                response.name.let {
                    binding.tvName.setText(it)
                }

                response.description.let {
                    binding.tvDescription.setText(it)
                }

                response.address.let {
                    binding.tvAddress.setText(it)
                }

                response.recommendedTime.let {
                    binding.tvTime.setText(it)
                }

                response.routeLength.let {
                    binding.tvDistance.setText(it + " км")
                }

                response.calories.let {
                    binding.tvCalories.setText(it + " ккал")
                }

                response.complexityId.let {
                    binding.tvComplexity.setText(it + " км")
                }

                response.coverImage.let {
                    Glide.with(requireContext()).load(it).error(R.drawable.ic_prew)
                        .into(binding.ivMainPhoton)
                }

                response.type.let {
                    it?.let {
                        binding.tvTypeRouteActive.text = TypeActivity.getTypeOfActivityName(requireContext(), it)
                        binding.ivTypeRouteActive.setImageResource(TypeActivity.getTypeOfActivityIcon(it))
                    }
                }

                response.integrity.let {
                    if (it == true){
                        binding.lvIntegrity.visibility = View.VISIBLE
                    }else{
                        binding.lvIntegrity.visibility = View.GONE
                    }
                }

                response.pointsTrack?.let {
                    setCurrentLocation(it)
                }
            }
        }
    }

    private fun setCurrentLocation(points: List<PointsTrack>) {
        points.takeIf { it.isNotEmpty() }?.apply {
            points[0].let {
                mapsViewController?.setPositionMap(it.latitude, it.longitude)
                mapsViewController = MapsViewController(
                    binding.mapview,
                    context,
                    GeoPoint(it.latitude, it.longitude)
                )
                mapsViewController?.homeView = true
                points.forEach {
                    polyLine.addPoint(GeoPoint(it.latitude, it.longitude))
                }
                binding.mapview.overlayManager.add(polyLine)
                binding.mapview.invalidate()
            }
        }
    }
}