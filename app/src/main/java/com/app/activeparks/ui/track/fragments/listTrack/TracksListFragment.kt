package com.app.activeparks.ui.track.fragments.listTrack

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.track.adapter.TrackItemAdapter
import com.app.activeparks.ui.track.fragments.saveTrack.SaveTrackFragment
import com.app.activeparks.ui.userProfile.video.AddVideoUserProfile
import com.technodreams.activeparks.databinding.FragmentTracksListBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class TracksListFragment  : Fragment() {

    lateinit var binding: FragmentTracksListBinding
    private lateinit var adapter: TrackItemAdapter
    private val viewModel: TracksListViewModel by activityViewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTracksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTracksList()
        initView()
        setListener()
        observe()
    }

    private fun setListener() {
        with(binding) {

            ivAddTrack.setOnClickListener {
                openFragment(AddVideoUserProfile())
            }

            rvTracksList.setOnClickListener {
                openFragment(SaveTrackFragment())
            }

            ivBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            etFindTrack.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val city: String = etFindTrack.getText().toString()
                    if (city.length > 1) {
                        viewModel.getFindTracksList(city)
                    }else{
                        viewModel.getTracksList()
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            srUpdate.setOnRefreshListener {
                viewModel.getTracksList()
            }
        }
    }

    private fun initView() {
        with(binding) {
            adapter = TrackItemAdapter { id ->
                openFragment(SaveTrackFragment.newInstance(id))
            }
            rvTracksList.adapter = adapter
        }
    }

    private fun observe() {
        with(viewModel) {
            trackList.observe(viewLifecycleOwner) { response ->
                binding.srUpdate.isRefreshing = false
                response?.takeIf { it.isNotEmpty() }
                    ?.apply {
                        adapter.list.submitList(this)
                }
            }
        }
    }

    private fun openFragment(fragment: Fragment) =
        (requireActivity() as? MainActivity)?.addFragment(fragment)
}