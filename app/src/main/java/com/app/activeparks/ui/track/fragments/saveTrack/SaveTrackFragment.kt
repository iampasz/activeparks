package com.app.activeparks.ui.track.fragments.saveTrack

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

class SaveTrackFragment : Fragment() {

    private lateinit var binding: FragmentSaveTrackBinding
    private val imageActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImage: Intent? = result.data
            handleSelectedImage(selectedImage)
        }
    }

    private val viewModel: SaveTrackViewModel by viewModel()
    private var count = 0
    private val maxCount = 255
    private var mapsViewController: MapsViewController? = null
    private var polyLine = Polyline()

    private var trackId: String = ""

    companion object {
        fun newInstance(trackId: String): SaveTrackFragment {
            val fragment = SaveTrackFragment()
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
        binding = FragmentSaveTrackBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var trackId = arguments?.getString("track_id") ?: ""

        with(binding) {
            viewModel.getTrack(trackId)

            setStartValue()
            setListener()
            observe()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun FragmentSaveTrackBinding.setStartValue() {

    }

    private fun FragmentSaveTrackBinding.setListener() {

        ivBack.setOnClickListener { requireActivity().onBackPressed() }

        etDescriptionTrack.addTextChangedListener(object : EasyTextWatcher() {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                count = s?.toString()?.length ?: 0
                binding.tvCount.text = "$count/$maxCount"
                viewModel.trackDate?.value?.description = s?.toString() ?: ""
            }
        })

        etNameTrack.addTextChangedListener(object : EasyTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.trackDate?.value?.name = s?.toString() ?: ""
            }
        })


        etAdress.addTextChangedListener(object : EasyTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.trackDate?.value?.address = s?.toString() ?: ""
            }
        })

        tvDeleteTrack.setOnClickListener {
            viewModel.removeTrack()
        }

        bSaveTrack.setOnClickListener {
            viewModel.saveTrack()

            binding.mapview.overlayManager.clear()
            binding.mapview.invalidate()

            requireActivity().onBackPressed()
        }

        bIntegrity.setOnClickListener {
            openGalleryForImage()
        }

        bMainPhoto.setOnClickListener {
            openGalleryForImage()
        }

        tvEditMap.setOnClickListener {
            openFragment(TrackChangeMapFragment())
        }
    }

    private fun observe() {
        with(viewModel) {
            trackDate.observe(viewLifecycleOwner) { response ->
                response.dateStartRecord.let {
                    binding.tvStratAdress.setText(it)
                }

                response.pointsTrack?.last()?.let {
                        val address =
                            AddressOfDoubleUtil.getAddressFromLocation(requireContext(), it.latitude,
                                it.longitude
                            )
                        binding.tvFinishAdress.text = address
                }

                response.name.let {
                    binding.etNameTrack.setText(it)
                }

                response.description.let {
                    binding.etDescriptionTrack.setText(it)
                }

                response.address.let {
                    binding.tvStratAdress.setText(it)
                }

                response.recommendedTime.let {
                    binding.tvTime.setText(it)
                }

                response.routeLength.let {
                    binding.tvDistance.setText(it + " км")
                }

                response.coverImage.let {
                    Glide.with(requireContext()).load(it).error(R.drawable.ic_prew)
                        .into(binding.ivMainPhoto)
                }

                response.integrity.let {
                    if (it == true){
                        binding.bIntegrity.resources.getDrawable(R.drawable.background_green,null)
                        binding.bIntegrity.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    }else{
                        binding.bIntegrity.resources.getDrawable(R.drawable.button_gray_video,null)
                        binding.bIntegrity.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                    }
                }

                response.pointsTrack?.let {
                    setCurrentLocation(it)
                }
            }
            back.observe(viewLifecycleOwner) {
                requireActivity().onBackPressed()
            }
        }
    }
    private fun handleSelectedImage(data: Intent?) {
        val selectedImage: Uri? = data?.data
        viewModel.trackDate.value?.coverImage = selectedImage?.toString()
        if (selectedImage != null) {
            binding.ivMainPhoto.setImageURI(selectedImage)
            FileHelper.uriToFile(selectedImage, requireContext())?.let { viewModel.saveImage(it) }
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

    private fun openGalleryForImage() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imageActivityResultLauncher.launch(galleryIntent)
    }

    private fun openFragment(fragment: Fragment) =
        (requireActivity() as? MainActivity)?.addFragment(fragment)
}