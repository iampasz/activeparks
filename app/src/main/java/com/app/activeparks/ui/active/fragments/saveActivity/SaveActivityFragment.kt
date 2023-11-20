package com.app.activeparks.ui.active.fragments.saveActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.active.fragments.level.ActivityInfoTrainingAdapter
import com.app.activeparks.ui.active.model.Feeling
import com.app.activeparks.util.EmptyTextWatcher
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.extention.disable
import com.app.activeparks.util.extention.enableIf
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.replaceNull
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentSaveActivityBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.views.overlay.Polyline
import java.text.SimpleDateFormat
import java.util.Date

class SaveActivityFragment : Fragment() {

    private lateinit var binding: FragmentSaveActivityBinding
    private val imageActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImage: Intent? = result.data
            handleSelectedImage(selectedImage)
        }
    }

    private val viewModel: SaveActivityViewModel by viewModel()
    private val activityViewModel: ActiveViewModel by activityViewModels()
    private var count = 0
    private val maxCount = 255
    private var mapsViewController: MapsViewController? = null
    private var polyLine = Polyline()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveActivityBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            setStartValue()
            setListener()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun FragmentSaveActivityBinding.setStartValue() {
        viewModel.currentActivity.dateTime = SimpleDateFormat("dd MMM yyyy, HH:mm").format(Date())
        Feeling.getFiling().first().apply {
            viewModel.currentActivity.feeling = this
            tvFellTitle.text = this.title
            ivFeelIcon.setImageResource(this.img)
        }

        tvData.text = viewModel.currentActivity.dateTime

        viewModel.startInfo.startPoint.unit =
            activityViewModel.activityState.startPoint.replaceNull()

        ivLocation.setActivityInfoItem(viewModel.startInfo.startPoint)
        calculateWidthLocation()
        ivWeather.setActivityInfoItem(viewModel.startInfo.weather)

        viewModel.currentActivity.location = ivLocation.getItem()
        viewModel.currentActivity.weather = ivWeather.getItem()


        rvInfoView.apply {
            val adapterInfoItem = ActivityInfoTrainingAdapter {}
            adapter = adapterInfoItem
            layoutManager = GridLayoutManager(requireContext(), 2)

            val list = activityViewModel.activityInfoItems.filter {
                (!activityViewModel.activityState.activityType.isInclude || !it.isOutside) &&
                        (activityViewModel.activityState.isPulseGadgetConnected || !it.isPulseGadget)
            }

            adapterInfoItem.list.submitList(list)
        }

        if (activityViewModel.activityState.activityType.isInclude) {
            cardView.gone()

            val layoutParams = rvInfoView.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.topToBottom = R.id.tvFeelDescription
            rvInfoView.layoutParams = layoutParams
        } else {
            setCurrentLocation()
        }
        initFeelingAdapter()
    }

    private fun initFeelingAdapter() {
        val feelings = Feeling.getFiling()
        val popupMenu = PopupMenu(requireContext(), binding.vFeel)
        binding.tvFellTitle.setOnClickListener {
            popupMenu.show()
        }

        feelings.forEach { feeling ->
            popupMenu.menu.add(feeling.title).setIcon(feeling.img).setOnMenuItemClickListener {
                onFeelingSelected(feeling)
                true
            }
        }
    }

    private fun onFeelingSelected(feeling: Feeling) {
        with(binding) {
            tvFellTitle.text = feeling.title
            ivFeelIcon.setImageResource(feeling.img)
        }
    }

    @Suppress("DEPRECATION")
    private fun calculateWidthLocation() {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels


        if (binding.ivLocation.calculateDesiredWidth() > 0.5 * screenWidth) {
            val point = viewModel.startInfo.startPoint
            point.unit = point.unit.replace(", ", ",\n")
            binding.ivLocation.setActivityInfoItem(point)
        }
    }

    private fun FragmentSaveActivityBinding.setListener() {
        etDescriptionActivity.addTextChangedListener(object : EmptyTextWatcher() {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                count = s?.toString()?.length ?: 0
                binding.tvCount.text = "$count/$maxCount"
                viewModel.currentActivity.descriptionActivity = s?.toString() ?: ""
            }
        })
        etNameTraining.addTextChangedListener(object : EmptyTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.currentActivity.titleActivity = s?.toString() ?: ""
                btnSave.enableIf(isValid())
            }
        })

        ivClose.setOnClickListener { requireActivity().onBackPressed() }
        tvDeleteTraining.setOnClickListener { requireActivity().onBackPressed() }

        btnSave.setOnClickListener {
            viewModel.saveActivity(activityViewModel.activityState.activeRoad, activityViewModel.activityInfoItems)

            binding.mapview.overlayManager.clear()
            binding.mapview.invalidate()

            btnSave.disable()
            requireActivity().onBackPressed()
        }

        addImgActivity.setOnClickListener {
            openGalleryForImage()
        }
    }

    private fun setCurrentLocation() {
        activityViewModel.activityState.activeRoad.takeIf { it.isNotEmpty() }?.apply {
            mapsViewController = MapsViewController(
                binding.mapview,
                context,
                activityViewModel.activityState.activeRoad.first()
            )
            mapsViewController?.homeView = true

            activityViewModel.apply {
                activityState.activeRoad.forEach {
                    polyLine.addPoint(it)
                }

                binding.mapview.overlayManager.add(polyLine)
                binding.mapview.invalidate()
            }
        } ?: kotlin.run {
            mapsViewController = MapsViewController(
                binding.mapview,
                context
            )
            mapsViewController?.homeView = true
        }
    }

    private fun isValid(): Boolean {
        with(binding) {
            val title = etNameTraining.text.toString()

            return (title.isNotEmpty() && title.length > 5)
        }
    }

    private fun handleSelectedImage(data: Intent?) {
        val selectedImage: Uri? = data?.data
        binding.icActivity.setImageURI(selectedImage)
        viewModel.currentActivity.uri = selectedImage
    }

    private fun openGalleryForImage() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imageActivityResultLauncher.launch(galleryIntent)
    }
}