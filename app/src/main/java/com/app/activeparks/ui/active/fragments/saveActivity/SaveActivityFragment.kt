package com.app.activeparks.ui.active.fragments.saveActivity

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
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.active.fragments.level.ActivityInfoTrainingAdapter
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.ui.active.model.Feeling
import com.app.activeparks.ui.active.model.InfoItem
import com.app.activeparks.util.EmptyTextWatcher
import com.app.activeparks.util.extention.enableIf
import com.app.activeparks.util.extention.gone
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentSaveActivityBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date

class SaveActivityFragment : Fragment() {

    private lateinit var binding: FragmentSaveActivityBinding
    private val viewModel: SaveActivityViewModel by viewModel()
    private val activityViewModel: ActiveViewModel by activityViewModels()
    private var count = 0
    private val maxCount = 255
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveActivityBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentActivity.dateTime = SimpleDateFormat("dd MMM yyyy, HH:mm").format(Date())

        with(binding) {
            tvData.text = viewModel.currentActivity.dateTime

            ivLocation.setActivityInfoItem(
                InfoItem(
                    0,
                    "м. Київ, вул. Авіфційна 24",
                    "Точка старту",
                    R.drawable.ic_location
                )
            )
            ivWeather.setActivityInfoItem(
                InfoItem(
                    1,
                    "Сонячна, 18*С",
                    "Погода",
                    R.drawable.ic_calories
                )
            )

            viewModel.currentActivity.location = ivLocation.getItem()
            viewModel.currentActivity.weather = ivWeather.getItem()

            tvFellTitle.setOnClickListener {
                viewModel.currentActivity.feeling = Feeling("Good")
                Toast.makeText(requireContext(), "I feel GOOD", Toast.LENGTH_LONG).show()
            }

            rvInfoView.apply {
                val adapterInfoItem = ActivityInfoTrainingAdapter {}
                adapter = adapterInfoItem
                layoutManager = GridLayoutManager(requireContext(), 2)

                val list = if (activityViewModel.activityState.activityType.isInclude) {
                    ActivityInfoTrainingItem.getActivityInfoItem().filter { !it.isOutside }
                } else {
                    ActivityInfoTrainingItem.getActivityInfoItem()
                }

                adapterInfoItem.list.submitList(list)
            }

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
                viewModel.saveActivity()
                requireActivity().onBackPressed()
            }

            activityViewModel.bitmap?.let {
                ivMapTraining.setImageBitmap(it)
            } ?: kotlin.run {
                cardView.gone()

                val layoutParams = rvInfoView.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.topToBottom = R.id.tvFeelDescription
                rvInfoView.layoutParams = layoutParams
            }

            addImgActivity.setOnClickListener {
                openGalleryForImage()
            }
        }
    }

    private fun isValid(): Boolean {
        with(binding) {
            val title = etNameTraining.text.toString()
            val description = etDescriptionActivity.text.toString()

            return (title.isNotEmpty() && title.length > 5)
        }
    }

    private val PICK_IMAGE_REQUEST = 1

    fun openGalleryForImage() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage: Uri? = data.data
            binding.icActivity.setImageURI(selectedImage)
        }
    }
}