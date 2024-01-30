package com.app.activeparks.ui.userProfile.activityInfo.gallery

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.app.activeparks.ui.userProfile.activityInfo.ActivityInfoViewModel
import com.technodreams.activeparks.databinding.FragmentImageGalleryActivityInfoBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ImageGalleryFragment(
    private val position: Int
) : Fragment() {

    var adapter: GalleryPagerAdapter? = null
    lateinit var binding: FragmentImageGalleryActivityInfoBinding
    private val vmInfo: ActivityInfoViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageGalleryActivityInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPager()
        initView()
        observe()
    }

    private fun initPager() {
        binding.pagerGallery.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.imageCounter.text =
                    (binding.pagerGallery.currentItem + 1).toString() + " із " + binding.pagerGallery.adapter?.itemCount
            }
        })

        binding.close.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun observe() {
        with(vmInfo) {
            activity.observe(viewLifecycleOwner) {
                it?.photos?.let { photos ->

                    adapter?.list?.submitList(photos)
                    binding.pagerGallery.setCurrentItem(position, true)
                }

            }
        }
    }

    private fun initView() {
        adapter = GalleryPagerAdapter(resources)
        binding.pagerGallery.adapter = adapter
    }
}