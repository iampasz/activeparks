package com.app.activeparks.ui.selectvideo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
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
            val fragment = ContentTypeVideoFragment()
            fragment.callback = this
            seletcFrament(fragment)
        }

        mViewModel.showVideo()?.observe(viewLifecycleOwner) { modelSelectCategory: ModelSelectCategory ->
            val radioButtonID = binding.radioGroup.checkedRadioButtonId
            val radioButton = binding.radioGroup.findViewById<RadioButton>(radioButtonID)
            val idx = binding.radioGroup.indexOfChild(radioButton)
            startActivity(
                Intent(requireContext(), VideoActivity::class.java)
                    .putExtra("id", idx)
                    .putExtra("categoryId", modelSelectCategory.TYPE_CATEGORY_ID)
                    .putExtra(
                        "exerciseDifficultyLevelId",
                        modelSelectCategory.TYPE_DIFFICULTY_LEVEL_ID
                    )
            )
        }

        initClickListener()

    }

    fun initClickListener() {
        binding.closed.setOnClickListener { onBackPressed() }
        binding.icBottomOne.setOnClickListener { mViewModel.rozmenka() }
        binding.icBottomTwo.setOnClickListener { mViewModel.rozmenka() }
        binding.icBottomThree.setOnClickListener { mViewModel.likar() }
    }

    override fun onSelectType(result: Int) {
        if (result == -1) {
            val fragment = ContentTypeVideoFragment()
            fragment.callback = this
            seletcFrament(fragment)
        } else {
            val type: Boolean
            type = result == 3
            val fragment = LevelVideoFragment(type)
            fragment.callback = this
            seletcFrament(fragment)
            mViewModel.selectActivePark(result)
        }
    }

    override fun onSelectLevel(result: Int) {
        mViewModel.type(result)
    }


    private fun onBackPressed(){
       // requireActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
        (requireActivity() as MainActivity).navControllerMain.navigate(R.id.navigation_scaner)
    }

    fun seletcFrament(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.frame_events, fragment)
            .commit()
    }
}