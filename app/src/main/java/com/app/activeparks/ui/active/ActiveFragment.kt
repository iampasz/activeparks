package com.app.activeparks.ui.active


import androidx.fragment.app.Fragment


class ActiveFragment : Fragment() {
//    private var binding: FragmentActiveBinding? = null
//    private var mapsViewController: MapsViewControler? = null
//    private var locationManager: LocationManager? = null
//    private val line = Polyline()
//    private var viewModel: ActiveViewModel? = null
//    private var isRunning = false
//    private var startTime: Long = 0
//    private var distance = 0.0
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?, savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentActiveBinding.inflate(inflater, container, false)
//        viewModel = ViewModelProvider(this)[ActiveViewModel::class.java]
//        return binding!!.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        locationManager =
//            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        initStartValue()
//        setCurrentLocation()
//        initListeners()
//        observes()
//        resetStopwatch()
//
//        val f = parentFragmentManager.findFragmentByTag("1") ?: MapActivityFragment()
//        parentFragmentManager
//            .beginTransaction()
//            .replace(R.id.navActivity, f, "1")
//            .commit()
//
//        binding?.topPanel?.navActivitySettings?.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.mapActivityFragment -> {
//                    val f = parentFragmentManager.findFragmentByTag("1") ?: MapActivityFragment()
//                    parentFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.navActivity, f, "1")
//                        .commit()
//                }
//
//                R.id.levelActivityFragment -> {
////                    val f = parentFragmentManager.findFragmentByTag("2") ?: LevelActivityFragment.newInstance(viewModel!!.activityState.activityType)
//                    val f = parentFragmentManager.findFragmentByTag("2") ?: SaveActivityFragment()
//                    parentFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.navActivity, f, "2")
//                        .commit()
//                }
//
//                R.id.settingsActivityFragment -> {
//
//                    val f = parentFragmentManager.findFragmentByTag("3") ?: SettingsActivityFragment()
//
//
//
//                    parentFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.navActivity, f, "3")
//                        .commit()
//                }
//
//            }
//            true
//        }
//
//
////        val navView: BottomNavigationView = binding?.topPanel?.navActivitySettings!!
////        val loginDialogContainer = childFragmentManager.findFragmentById(R.id.navActivity) as NavHostFragment
////        val loginNavController = loginDialogContainer.navController
////
////        val appBarConfiguration = AppBarConfiguration(
////            setOf(
////                R.id.mapActivityFragment, R.id.levelActivityFragment, R.id.settingsActivityFragment
////            )
////        )
////        NavigationUI.setupActionBarWithNavController(requireActivity() as AppCompatActivity, loginNavController, appBarConfiguration)
////        navView.setupWithNavController(loginNavController)
//    }
//
//    private fun initStartValue() {
//        binding?.let {
//            binding?.topPanel?.navActivitySettings?.menu?.findItem(R.id.mapview)?.isVisible = false
//
//            with(it) {
//                topPanel.aivFirst.apply {
//                    setNumber(context.getString(R.string.tv_0))
//                    setActivityInfoItem(ActivityInfoItem.getActivityInfoItem()[0])
//                }
//                topPanel.aivSecond.apply {
//                    setNumber(context.getString(R.string.tv_0))
//                    setActivityInfoItem(ActivityInfoItem.getActivityInfoItem()[1])
//                }
//                topPanel.aivThird.apply {
//                    setNumber(context.getString(R.string.tv_0))
//                    setActivityInfoItem(ActivityInfoItem.getActivityInfoItem()[2])
//                }
//            }
//        }
//
//    }
//
//    override fun onDestroy() {
//        locationManager!!.removeUpdates(this)
//        super.onDestroy()
//    }
//
//    private fun startCheckLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10f, this)
//    }
//
//    private fun startStopwatch() {
//        if (!isRunning) {
//            isRunning = true
//            startTime = SystemClock.elapsedRealtime()
//            handler.postDelayed(updateTime, 0)
//        }
//    }
//
//    private fun stopStopwatch() {
//        if (isRunning) {
//            isRunning = false
//            handler.removeCallbacks(updateTime)
//        }
//    }
//
//    private fun resetStopwatch() {
//        isRunning = false
//        handler.removeCallbacks(updateTime)
//        binding!!.topPanel.time.text = getString(R.string.tv_reset_time)
//    }
//
//    private val handler = Handler(Looper.getMainLooper())
//    private val updateTime: Runnable = object : Runnable {
//        override fun run() {
//            val timeInMilliseconds = SystemClock.elapsedRealtime() - startTime
//            var seconds = (timeInMilliseconds / 1000).toInt()
//            var minutes = seconds / 60
//            val hours = minutes / 60
//            seconds %= 60
//            minutes %= 60
//            binding!!.topPanel.time.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
//            handler.postDelayed(this, 1000)
//        }
//    }
//
//    private fun initListeners() {
//        binding?.let {
//            with(it) {
//                btnStart.setOnClickListener {
//                    startCheckLocation()
//                    startStopwatch()
//                    btnStart.visibility = View.GONE
//                    actionClose.visibility = View.VISIBLE
//                }
//                actionClose.setOnClickListener {
//                    btnStart.visibility = View.VISIBLE
//                    actionClose.visibility = View.GONE
//                    stopStopwatch()
//                    locationManager!!.removeUpdates(this@ActiveFragment)
//                    viewModel!!.insertGeoPointList(line.actualPoints)
//                    resetStopwatch()
//                }
//
//                topPanel.aivFirst.setOnClickListener {
//                    showDialogActivityInfoItem(
//                        topPanel.aivFirst,
//                        topPanel.aivSecond.getActivityInfoItem()?.id ?: 0,
//                        topPanel.aivThird.getActivityInfoItem()?.id ?: 0
//                    )
//                }
//                topPanel.aivSecond.setOnClickListener {
//                    showDialogActivityInfoItem(
//                        topPanel.aivSecond,
//                        topPanel.aivFirst.getActivityInfoItem()?.id ?: 0,
//                        topPanel.aivThird.getActivityInfoItem()?.id ?: 0
//                    )
//                }
//                topPanel.aivThird.setOnClickListener {
//                    showDialogActivityInfoItem(
//                        topPanel.aivThird,
//                        topPanel.aivFirst.getActivityInfoItem()?.id ?: 0,
//                        topPanel.aivSecond.getActivityInfoItem()?.id ?: 0
//                    )
//                }
//            }
//        }
//    }
//
//    private fun showDialogActivityInfoItem(view: ActivityInfoView, firstId: Int, secondId: Int) {
//        DialogViewBuilder(requireContext()).build().apply {
//            setContentView(R.layout.dialog_activity_info)
//            findViewById<TextView>(R.id.tvTitle).setOnClickListener {
//                dismiss()
//            }
//
//            val adapter = ActivityInfoItemAdapter(
//                view.getActivityInfoItem()?.id ?: 0
//            ) { item ->
//                view.apply {
//                    setNumber("0")
//                    setTitle(item.title)
//                    setActivityInfoItem(item)
//                }
//                dismiss()
//            }
//            findViewById<RecyclerView>(R.id.rvActivity).adapter = adapter
//            adapter.list.submitList(
//                ActivityInfoItem.getActivityInfoItem()
//                    .filter { it.id != firstId && it.id != secondId })
//
//            show()
//        }
//    }
//
//    private fun observes() {
////        viewModel!!.onSuccess.observe(viewLifecycleOwner) { aBoolean: Boolean ->
////            if (aBoolean) {
////                binding!!.mapview.overlayManager.removeIf { overlay: Overlay? -> overlay is Polyline }
////                line.actualPoints.clear()
////            }
////        }
////        viewModel!!.listsGeoPoint.observe(viewLifecycleOwner) { lists: List<List<GeoPoint?>?> ->
////            if (lists.isNotEmpty()) {
////                line.setPoints(lists.stream().findFirst().get())
////                binding!!.mapview.overlayManager.add(line)
////                binding!!.mapview.invalidate()
////            }
////        }
//    }
//
//    private fun setCurrentLocation() {
////        mapsViewController = MapsViewControler(binding!!.mapview, context)
////        mapsViewController!!.homeView = true
//    }
//
//    private var previousLocation: Location? = null
//    override fun onLocationChanged(location: Location) {
////        val geoPoint = GeoPoint(location)
////        if (previousLocation != null) {
////            val distance = previousLocation!!.distanceTo(location)
////            val timeElapsed = (location.time - previousLocation!!.time) / 1000 // Convert to seconds
////            val speedMetersPerSecond = distance / timeElapsed
////            val speedKilometersPerHour = speedMetersPerSecond * 3.6f
////            val speedMinutesPerKilometer = 60.0f / speedKilometersPerHour
////            binding!!.topPanel.aivThird.setNumber(((speedMinutesPerKilometer * 10).roundToInt() / 10.0).toString())
////        } else {
////            previousLocation = location
////        }
////        line.addPoint(geoPoint)
////        binding!!.mapview.overlayManager.add(line)
////        binding!!.mapview.invalidate()
////        distance += 0.1
////        binding!!.topPanel.aivFirst.setNumber(((distance * 10).roundToInt() / 10.0).toString())
//    }
}