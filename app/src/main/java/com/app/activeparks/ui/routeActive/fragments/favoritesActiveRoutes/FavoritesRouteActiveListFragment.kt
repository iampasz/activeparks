package com.app.activeparks.ui.routeActive.fragments.favoritesActiveRoutes

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
import com.app.activeparks.ui.routeActive.adapter.FavoritesRouteActiveItemAdapter
import com.app.activeparks.ui.routeActive.adapter.RouteActiveItemAdapter
import com.app.activeparks.ui.routeActive.fragments.detailsRouteActive.DetailsRouteActiveFragment
import com.app.activeparks.ui.routeActive.fragments.saveRouteActive.SaveRouteActiveFragment
import com.app.activeparks.ui.track.fragments.saveTrack.SaveTrackFragment
import com.app.activeparks.util.extention.gone
import com.technodreams.activeparks.databinding.FragmentRouteActiveListBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavoritesRouteActiveListFragment : Fragment() {

    lateinit var binding: FragmentRouteActiveListBinding
    private lateinit var adapter: FavoritesRouteActiveItemAdapter
    private val viewModel: FavoritesRouteActiveListViewModel by activityViewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRouteActiveListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoritesRouteActivesList()
        initView()
        setListener()
        observe()
    }

    private fun setListener() {
        with(binding) {

            ivBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            srUpdate.setOnRefreshListener {
                viewModel.getFavoritesRouteActivesList()
            }
        }
    }

    private fun initView() {
        with(binding) {
            tvTitle.text = "ОБРАНІ МАРШРУТИ"
            tvDescriptionText.gone()
            etFindTrack.gone()

            adapter = FavoritesRouteActiveItemAdapter (
                onItemClick = { id ->
                    openFragment(DetailsRouteActiveFragment.newInstance(id))
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