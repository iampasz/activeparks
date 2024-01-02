package com.app.activeparks.ui.selectvideo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.selectvideo.util.VIDEO_ACTIVITY_ID
import com.app.activeparks.ui.selectvideo.util.VIDEO_CATEGORY_ID
import com.app.activeparks.ui.selectvideo.util.VIDEO_LEVEL_ID
import com.app.activeparks.ui.video.VideoActivity
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentSelectVideoBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SelectVideoFragment : Fragment(), ContentVideoCallback {

    lateinit var binding: FragmentSelectVideoBinding

    private val mViewModel: SelectVideoViewModel by activityViewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val code: String? = requireActivity().intent.getStringExtra("code")
        if (code != null) {
            try {
                onSelectType(code.toInt())
            } catch (e: Exception) {
                onSelectType(0)
            }
        } else {
            openVideoFragment()
        }

        mViewModel.showVideo()
            ?.observe(viewLifecycleOwner) { modelSelectCategory: ModelSelectCategory ->

                val radioButtonID = binding.radioGroup.checkedRadioButtonId
                val radioButton = binding.radioGroup.findViewById<RadioButton>(radioButtonID)
                val idx = binding.radioGroup.indexOfChild(radioButton)

                startActivity(
                    Intent(requireContext(), VideoActivity::class.java)
                        .putExtra(VIDEO_ACTIVITY_ID, idx)
                        .putExtra(VIDEO_CATEGORY_ID, modelSelectCategory.TYPE_CATEGORY_ID)
                        .putExtra(
                            VIDEO_LEVEL_ID,
                            modelSelectCategory.TYPE_DIFFICULTY_LEVEL_ID
                        )
                )
            }

        initClickListener()

    }

    private fun initClickListener() {
        with(binding) {
            closed.setOnClickListener { onBackPressed() }
            icBottomOne.setOnClickListener { mViewModel.rozmenka() }
            icBottomTwo.setOnClickListener { mViewModel.rozmenka() }
            icBottomThree.setOnClickListener { mViewModel.likar() }
        }

    }

    override fun onSelectType(result: Int) {
        if (result == -1) {
            openVideoFragment()

        } else {
            val type: Boolean = result == 3
            val fragment = LevelVideoFragment(type)
            fragment.callback = this
            selectFragment(fragment)
            mViewModel.selectActivePark(result)
        }
    }

    override fun onSelectLevel(result: Int) {
        mViewModel.type(result)
    }


    private fun onBackPressed() {
        // requireActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
        (requireActivity() as? MainActivity)?.navControllerMain?.navigate(R.id.navigation_scaner)
    }

    private fun selectFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.frame_events, fragment)
            .commit()
    }

    private fun openVideoFragment(){
        val fragment = ContentTypeVideoFragment()
        fragment.callback = this
        selectFragment(fragment)
    }
}