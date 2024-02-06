package com.app.activeparks.ui.track.fragments.changeMapTrack

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.app.activeparks.data.model.track.PointsTrack
import com.app.activeparks.ui.active.model.Direction
import com.app.activeparks.ui.track.adapter.RouteChangeAdapter
import com.app.activeparks.util.MapsViewController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentEditTrackBinding
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline

class TrackChangeMapFragment : Fragment() {

    lateinit var binding: FragmentEditTrackBinding
    private lateinit var adapter: RouteChangeAdapter
    private var dataCallback: DataCallback? = null

    private var pointsTrack: List<PointsTrack>? = null

    private var mapsViewController: MapsViewController? = null
    private var polyLine = Polyline()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListener()
    }

    private fun initView() {
        with(binding) {
            adapter = RouteChangeAdapter(
                onItemClick = { index ->
                    setChangeMapDialog(index)
                })
            rvListRouteActive.adapter = adapter
            rvListRouteActive.layoutManager =
                GridLayoutManager(requireContext(), calculateSpanCount(requireContext()))

            arguments?.getParcelableArrayList<PointsTrack>("pointsList")?.let {
                setCurrentLocation(it)
                adapter.list.submitList(it)
            }

        }
    }

    private fun setListener() {
        with(binding) {
            ivBack.setOnClickListener {
                pointsTrack?.let { dataCallback?.onDataReceived(it) }
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setCurrentLocation(points: List<PointsTrack>) {
        points.takeIf { it.isNotEmpty() }?.apply {
            pointsTrack = points
            points[0].let {
                mapsViewController?.setPositionMap(it.latitude, it.longitude)
                mapsViewController = MapsViewController(
                    binding.mapview,
                    context,
                    GeoPoint(it.latitude, it.longitude)
                )
                mapsViewController?.homeView = true
                points.forEach {
                    polyLine.addPoint(GeoPoint(it.latitude, it.longitude))
                }
                binding.mapview.overlayManager.add(polyLine)
                binding.mapview.invalidate()
            }
        }
    }

    private fun setChangeMapDialog(index: Int) {
        val dialog = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
        dialog.setContentView(R.layout.dialog_change_map)
        val rightAction = dialog.findViewById<LinearLayout>(R.id.llRightAction)
        rightAction?.setOnClickListener {
            pointsTrack = editTurnInList(index, Direction.RIGHT.direction)
            sendDataToFragment()
            dialog.dismiss()
        }

        val leftAction = dialog.findViewById<LinearLayout>(R.id.llLeftAction)!!
        leftAction.setOnClickListener {
            pointsTrack = editTurnInList(index, Direction.RIGHT.direction)
            sendDataToFragment()
            dialog.dismiss()
        }

        val cancel = dialog.findViewById<LinearLayout>(R.id.cancel)!!
        cancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun editTurnInList(index: Int, newTurn: String): List<PointsTrack>? {
        return pointsTrack?.toMutableList()?.apply {
            if (index in 0 until size) {
                this[index] = this[index].copy(turn = newTurn)
            }
        }
    }

    fun setDataCallback(callback: DataCallback) {
        this.dataCallback = callback
    }

    private fun calculateSpanCount(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        val columns =
            (dpWidth / 64).toInt()  // Замените YOUR_ITEM_WIDTH_DP на ширину вашего элемента в dp
        return if (columns > 0) columns else 1
    }

    private fun sendDataToFragment() {
        pointsTrack?.let { adapter.submitData(it) }
        pointsTrack?.let { dataCallback?.onDataReceived(it) }
    }

}