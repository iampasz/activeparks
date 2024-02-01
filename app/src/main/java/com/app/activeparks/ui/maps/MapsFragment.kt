package com.app.activeparks.ui.maps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.activeparks.MainActivity
import com.app.activeparks.data.model.sportsgrounds.ItemSportsground
import com.app.activeparks.data.model.sportsgrounds.Sportsgrounds
import com.app.activeparks.ui.dialog.BottomDialogActiveParkFragment
import com.app.activeparks.ui.dialog.BottomSearchDialog
import com.app.activeparks.ui.maps.adapter.ParksAdaper
import com.app.activeparks.ui.maps.adapter.RouteActiveAdapter
import com.app.activeparks.ui.maps.dialog.BottomDialogFilterOfMaps
import com.app.activeparks.ui.park.ParkActivity
import com.app.activeparks.ui.routeActive.adapter.RouteActiveItemAdapter
import com.app.activeparks.ui.routeActive.fragments.favoritesActiveRoutes.FavoritesRouteActiveListFragment
import com.app.activeparks.ui.routeActive.fragments.saveRouteActive.SaveRouteActiveFragment
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.extention.buttonDeselectLeft
import com.app.activeparks.util.extention.buttonDeselectRight
import com.app.activeparks.util.extention.buttonSelectLeft
import com.app.activeparks.util.extention.buttonSelectRight
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayout
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentMapsBinding


class MapsFragment : Fragment() {

    lateinit var binding: FragmentMapsBinding

    private lateinit var routeActive: RouteActiveAdapter
    private lateinit var parksAdaper: ParksAdaper

    private var viewModel: MapsViewModel? = null
    private var locationClient: FusedLocationProviderClient? = null
    var mapsViewController: MapsViewController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListener()
        observe()
    }

    private fun initView() {
        locationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        mapsViewController = MapsViewController(binding.mapview, context)
        viewModel!!.getSportsgroundList(90)
        mapsViewController!!.homeView = true
        setUpMapIfNeeded(context)
    }

    private fun setListener() {
        binding.bFind.setOnClickListener { view ->
            viewModel!!.getSportsgroundList(90)
            binding.bFind.visibility = View.INVISIBLE
        }

        binding.ivBookmark.setOnClickListener { view ->
            openFragment(FavoritesRouteActiveListFragment())
        }

        binding.ivFilter.setOnClickListener { view ->
            val addPhotoBottomDialogFragment =
                BottomDialogFilterOfMaps.newInstance()
            addPhotoBottomDialogFragment.show(
                requireActivity().supportFragmentManager,
                "add_photo_dialog_fragment"
            )
        }

        mapsViewController!!.setOnCliclListener(object : MapsViewController.MapsViewListener {
            override fun onClick(sportsground: ItemSportsground) {
                val addPhotoBottomDialogFragment =
                    BottomDialogActiveParkFragment.newInstance().setSportsground(sportsground)
                addPhotoBottomDialogFragment.show(
                    activity!!.supportFragmentManager,
                    "add_photo_dialog_fragment"
                )
            }

            override fun onPositionStatus(status: Boolean) {
                binding.bFind.visibility =  if (status) View.VISIBLE else View.INVISIBLE
                if (status) binding.bFind.startAnimation(
                    AnimationUtils.loadAnimation(
                        context, R.anim.fadein
                    )
                )
            }

            override fun onPosition(latitude: Double, longitude: Double) {
                viewModel!!.setGeolocation(latitude, longitude)
            }
        })

        binding.bLocationUser.setOnClickListener { view: View? ->
            requestPermissions()
        }

        binding.ivFindAddress.setOnClickListener { view ->
            val addPhotoBottomDialogFragment = BottomSearchDialog.newInstance().setOnCliclListener(
                BottomSearchDialog.SearchDialogListener { lat, lon ->
                    mapsViewController!!.setPositionMap(lat, lon)
                    viewModel!!.setUpdateSportsgroundList(50, lat, lon)
                })
            addPhotoBottomDialogFragment.show(
                requireActivity().supportFragmentManager,
                "fragment_search"
            )
        }

        binding!!.tlSelectMenu.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        binding.llSelectList.visibility = View.GONE
                        binding.flSelectMaps.visibility = View.VISIBLE
                        viewModel?.isFilterList = false
                    }

                    1 -> {
                        binding.llSelectList.visibility = View.VISIBLE
                        binding.flSelectMaps.visibility = View.GONE
                        viewModel?.isFilterList = true
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.bRouteActive.setOnClickListener { view ->
            if (viewModel?.isFilterList == false) {
                if (viewModel?.isFilterActiveRoute == true) {
                    binding.bRouteActive.buttonDeselectLeft()
                } else {
                    binding.bRouteActive.buttonSelectLeft()
                }
                viewModel?.isFilterActiveRoute = (viewModel?.isFilterActiveRoute != true)
            } else {
                binding.bRouteActive.buttonSelectLeft()
                binding.bActivePark.buttonDeselectRight()
                viewModel?.isFilterActivePark = false
                binding.etFind.hint = "Пошук за маршрутом"
                setRouteActiveAdaper()
            }
        }

        binding.bActivePark.setOnClickListener { view ->
            if (viewModel?.isFilterList == false) {
                if (viewModel?.isFilterActivePark == true){
                    binding.bActivePark.buttonDeselectRight()
                }else{
                    binding.bActivePark.buttonSelectRight()
                }
                viewModel?.isFilterActivePark = (viewModel?.isFilterActivePark != true)
            } else {
                binding.bActivePark.buttonSelectRight()
                binding.bRouteActive.buttonDeselectLeft()
                viewModel?.isFilterActiveRoute = false
                binding.etFind.hint = "Пошук за парком"
                setParksAdaper()
            }
        }
    }

    private fun observe() {
        with(viewModel) {
            viewModel?.sportsground?.observe(viewLifecycleOwner) { sportsgrounds: Sportsgrounds ->
                parksAdaper = ParksAdaper(getActivity(), sportsgrounds.getSportsground())
                parksAdaper.setOnCliclListener(object : ParksAdaper.ParksAdaperListener {
                    override fun onClick(lat: Double?, lon: Double?) {
                        val uri: String =
                            "https://www.google.com/maps/search/?api=1&query=" + lat + "," + lon
                        val intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        startActivity(intent)
                    }

                    override fun onInfoPark(sportsground: ItemSportsground?) {
                        startActivity(
                            Intent(getActivity(), ParkActivity::class.java).putExtra(
                                "id",
                                sportsground?.getId()
                            )
                        )
                    }
                })
                mapsViewController!!.setSportsgroundList(sportsgrounds)
                binding.rvList.adapter = parksAdaper
                binding.rvList.invalidate()
            }
            viewModel?.sportsgroundsList?.observe(viewLifecycleOwner) { sportsgrounds ->
                routeActive = RouteActiveAdapter(
                    onItemClick = { id ->
                        openFragment(SaveRouteActiveFragment.show(id))
                    },
                    onRoutePoint = { pointsTrack ->
                        val uri =
                            "https://www.google.com/maps/search/?api=1&query=${pointsTrack.latitude},${pointsTrack.longitude}"
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
                    })
                mapsViewController!!.setSportsgroundList(sportsgrounds)
            }
        }
   }


    private fun setUpMapIfNeeded(ctx: Context?) {
        if (ActivityCompat.checkSelfPermission(
                ctx!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                ctx, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            return
        }
    }

    fun requestPermissions() {
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            && PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        } else {
//            lastLocation
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
//            lastLocation
        }
    }

    fun setParksAdaper() {
        binding.rvList.adapter = parksAdaper
        binding.rvList.invalidate()
        parksAdaper.notifyDataSetChanged()
    }

    fun setRouteActiveAdaper() {
        binding.rvList.adapter = routeActive
        binding.rvList.invalidate()
        routeActive.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        mapsViewController?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapsViewController!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapsViewController!!.onDestroy()
    }
    private fun openFragment(fragment: Fragment) {
        (requireActivity() as? MainActivity)?.openFragment(fragment)
    }

}