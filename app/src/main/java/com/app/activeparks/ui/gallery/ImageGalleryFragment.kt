package com.app.activeparks.ui.gallery

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.app.activeparks.ui.gallery.adapter.GalleryPagerAdapter
import com.app.activeparks.util.extention.removeFragment
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentImageGalleryBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ImageGalleryFragment : Fragment() {

    val adapter = GalleryPagerAdapter{}
    lateinit var binding: FragmentImageGalleryBinding
    private val galleryViewModel: GalleryViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageGalleryBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pagerGallery.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.imageCounter.text = (binding.pagerGallery.currentItem +1).toString() + " із " +binding.pagerGallery.adapter?.itemCount

                galleryViewModel
                    .photoItemList
                    .value?.get(position)?.userPhoto?.let {
                    Glide
                        .with(binding.imageAuthor.context)
                        .load(galleryViewModel
                            .photoItemList
                            .value?.get(position)?.userPhoto)
                        .error(R.drawable.ic_prew)
                        .into(binding.imageAuthor) }

                binding.dateCreated.text = galleryViewModel.photoItemList.value?.get(position)?.id
            }
        })



        binding.close.setOnClickListener{
            parentFragmentManager.removeFragment(this)
        }

        binding.cardDelete.setOnClickListener{
            val currentPosition = arguments?.getInt("CURRENT_POSITION", 0) ?: 0
            binding.pagerGallery.currentItem = currentPosition
        }

        initView()
        obsereve()
    }

    private fun obsereve() {
        galleryViewModel.photoItemList.observe(viewLifecycleOwner){
            adapter.list.submitList(galleryViewModel.photoItemList.value)
            val currentPosition = arguments?.getInt("CURRENT_POSITION", 0) ?: 0
            binding.pagerGallery.setCurrentItem(currentPosition, true)
        }
    }

    private fun initView() {
        binding.pagerGallery.adapter = adapter
    }
}