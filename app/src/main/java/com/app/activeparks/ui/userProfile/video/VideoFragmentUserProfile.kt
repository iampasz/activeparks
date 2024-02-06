package com.app.activeparks.ui.userProfile.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
import com.technodreams.activeparks.databinding.FragmentUserProfileVideoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by O.Dziuba on 21.12.2023.
 */
class VideoFragmentUserProfile : Fragment() {

    private lateinit var binding: FragmentUserProfileVideoBinding
    private val adapter = VideoItemAdapter {
        openFragment(AddVideoUserProfile(it))
    }
    private val viewModel: VideoUserProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileVideoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListener()
        observe()

        viewModel.getUserVideos()
    }

    private fun observe() {
        with(viewModel) {
            userVideos.observe(viewLifecycleOwner) {
                it?.items?.let { videos ->
                    if (videos.isNotEmpty()) {
                        adapter.list.submitList(videos)
                        binding.tvNoVideo.gone()
                    } else {
                        binding.tvNoVideo.visible()
                    }
                }
            }
        }
    }

    private fun setListener() {
        with(binding) {
            tvAddVideo.setOnClickListener {
                openFragment(AddVideoUserProfile())
            }
        }
    }

    private fun initView() {
        with(binding) {
            rvVideo.adapter = adapter
        }
    }

    private fun openFragment(fragment: Fragment) =
        (requireActivity() as? MainActivity)?.openFragment(fragment)
}