package com.app.activeparks.ui.park

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.app.activeparks.data.model.parks.ParksItem
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.model.sportsgrounds.ItemSportsground
import com.app.activeparks.ui.adapter.PhotosAdaper
import com.app.activeparks.ui.event.activity.EventFragment
import com.app.activeparks.ui.event.adapter.EventsListAdaper
import com.app.activeparks.ui.park.adapter.ParkListAdaper
import com.app.activeparks.util.MapsViewController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ActivityParkBinding

class ParkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParkBinding
    private var viewModel: ParkViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ParkModelFactory(this)).get(ParkViewModel::class.java)

        viewModel?.getPark(intent.getStringExtra("id"))
        setListener()
        observe()

    }

    private fun setListener() {
        binding.ivBack.setOnClickListener { v: View? ->
            finish()
        }
    }
    private fun observe() {
        viewModel?.parkDetails?.observe(this) { park: ItemSportsground ->
                binding.tvTitle.text = park.title

                Glide.with(this).load(park.photo).error(R.drawable.ic_prew).into(binding.ivMainPhoto)
                binding.vpGallery.adapter = PhotosAdaper(this, park.photos)

                Html.fromHtml("<u>" + park.location[0] + " " + park.location[1] + "</u>").let {
                    binding.tvDescription.text = it
                }

                park.distanceToPoint?.let {
                    binding.tvDistance.visibility = View.VISIBLE
                    binding.tvDistance.text =  "${park.distanceToPoint} км" }

                binding.tvAddress.text = park.city + " " + park.street
                listPark(park)

                TabLayoutMediator(binding.tlGalleryTab, binding.vpGallery,
                    TabConfigurationStrategy { tab: TabLayout.Tab?,
                                               position: Int -> }).attach()

                MapsViewController(binding.mapView, this).setMarker(park.location[0], park.location[1])

                if (park.coordinators != null) {
                    Glide.with(this).load(park.coordinators[0].photo).error(R.drawable.ic_prew)
                        .into(binding.ivPhotoCordenator)

                    binding.lvItemCordenator.visibility = View.VISIBLE
                    binding.tvNameCordenator.text = park.coordinators[0].firstName + " " + park.coordinators[0].lastName
                    binding.tvEmailCordenator.text = park.coordinators[0].email
                    binding.tvPhoneCordenator.text = park.coordinators[0].phone
                }

                if (park.sportEvents.size > 0) {

                    binding.tvParkEvent.visibility = View.GONE
                    binding.rvParkEvent.visibility = View.VISIBLE

                    binding.rvParkEvent.setAdapter(
                        EventsListAdaper(
                            this,
                            park.sportEvents
                        ).setOnEventListener(object : EventsListAdaper.EventsListener {
                            override fun onInfo(itemClub: ItemEvent) {
                                startActivity(
                                    Intent(
                                        baseContext,
                                        EventFragment::class.java
                                    ).putExtra("id", itemClub.id)
                                )
                            }

                            override fun onOpenMaps(lat: Double, lon: Double) {
                                val uri =
                                    "https://www.google.com/maps/search/?api=1&query=$lat,$lon"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                                startActivity(intent)
                            }
                        })
                    )
                }

                binding.tvDescription.setOnClickListener(View.OnClickListener { v: View? ->
                    if (park.location != null) {
                        val uri =
                            "https://google.com/maps/search/?api=1&query=" + park.location[0] + "," + park.location[1]
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        startActivity(intent)
                    }
                })
        }
    }

    fun listPark(park: ItemSportsground) {
        val item: MutableList<ParksItem> = ArrayList()

        if (park.capacityId != null && !park.capacityId.isEmpty()) {
            item.add(ParksItem("Місткість", viewModel!!.capacity(park.capacityId)))
        }

        if (park.hasLighting != null) {
            item.add(ParksItem("Освітлення", if (park.hasLighting > 0) "Присутнє" else "Відсутнє"))
        }

        park.onReconstruction?.let {
            item.add(ParksItem("Технічний стан",  if (it) "Відміний" else "Реконсту"))
        }

        park.accessTypeId?.let {
            item.add(ParksItem("Тип доступу", viewModel!!.accessTypeId(it)))
        }

        park.facebookUrl?.let {
            if (it.length > 1) item.add(ParksItem("Посилання на facebook", it))
        }

        park.typeId?.let {
            item.add(ParksItem("Вид майданчику", viewModel!!.sportsgroundType(park.typeId)))
        }

        park.ownershipTypeId?.let {
            item.add(ParksItem("Форма власності", viewModel!!.ownershipType(park.ownershipTypeId)))
        }

        park.workHours?.let {
            item.add(ParksItem("Режим роботи", park.workHours))
        }

        binding.rvList.layoutManager = GridLayoutManager(this, 2)
        binding.rvList.adapter = ParkListAdaper(
            this,
            item
        ).setListener { url: String? -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
    }
}