package com.app.activeparks.ui.routeActive.fragments.listRouteActive

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.active.ActivityForActivity
import com.app.activeparks.ui.routeActive.adapter.RouteActiveItemAdapter
import com.app.activeparks.ui.track.fragments.saveTrack.SaveTrackFragment
import com.technodreams.activeparks.databinding.FragmentRouteActiveListBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class RouteActiveListFragment : Fragment() {

    lateinit var binding: FragmentRouteActiveListBinding
    private lateinit var adapter: RouteActiveItemAdapter
    private val viewModel: RouteActiveListViewModel by activityViewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRouteActiveListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRouteActivesList()
        initView()
        setListener()
        observe()
    }

    private fun setListener() {
        with(binding) {

            rvTracksList.setOnClickListener {
                //TODO Переслати користувача на ActivityForActivity після закінчення на SaveTrackFragment
                //openFragment(SaveTrackFragment())
                startActivity(Intent(requireContext(), ActivityForActivity::class.java))
            }

            ivBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            etFindTrack.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val city: String = etFindTrack.getText().toString()
                    if (city.length > 1) {
                        viewModel.getFindRouteActivesList(city)
                    }else{
                        viewModel.getRouteActivesList()
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
    }

    private fun initView() {
        with(binding) {
            adapter = RouteActiveItemAdapter (
                onItemClick = { id ->
                    openFragment(SaveTrackFragment.newInstance(id))
                },
                onRoutePoint = { pointsTrack ->
                    val uri =
                        "https://www.google.com/maps/search/?api=1&query=${pointsTrack.latitude},${pointsTrack.longitude}"
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
                })
            rvTracksList.adapter = adapter
        }
    }

    private fun observe() {
        with(viewModel) {
            routeActiveList.observe(viewLifecycleOwner) { response ->
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