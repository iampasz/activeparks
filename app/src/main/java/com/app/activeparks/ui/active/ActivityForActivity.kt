package com.app.activeparks.ui.active

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.active.fragments.infoItem.ActivityInfoItemFragment
import com.app.activeparks.ui.active.fragments.map.MapActivityFragment
import com.app.activeparks.ui.active.fragments.saveActivity.SaveActivityFragment
import com.app.activeparks.ui.active.fragments.saveActivity.SaveActivityViewModel
import com.app.activeparks.ui.active.fragments.type.ActivityTypeFragment
import com.app.activeparks.ui.active.model.ActivityInfoItem
import com.app.activeparks.ui.active.model.TypeOfActivity
import com.app.activeparks.util.extention.enableIf
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentActiveBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.views.MapView

class ActivityForActivity : AppCompatActivity() {

    private lateinit var binding: FragmentActiveBinding
    private var isRunning = false
    private val activeViewModel: ActiveViewModel by viewModel()
    private val saveViewModel: SaveActivityViewModel by viewModel()
    private var startTime: Long = 0
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentActiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.topPanel.navActivitySettings

        navController = findNavController(R.id.navActivity)
        navView.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()

        initStartValue()
        initListeners()
        observes()
        resetStopwatch()
        updateUI()

        activeViewModel.test()
    }

    override fun onDestroy() {
        super.onDestroy()
        activeViewModel.checkLocation.value = false
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size > 1) {
            supportFragmentManager.beginTransaction()
                .remove(supportFragmentManager.fragments.last()).commit()
            activeViewModel.updateUI.value = true
        } else {
            super.onBackPressed()
        }
    }

    private fun initStartValue() {
        with(binding) {
            topPanel.aivFirst.apply {
                setNumber(context.getString(R.string.tv_0))
                setActivityInfoItem(ActivityInfoItem.getActivityInfoItem()[0])
            }
            topPanel.aivSecond.apply {
                setNumber(context.getString(R.string.tv_0))
                setActivityInfoItem(ActivityInfoItem.getActivityInfoItem()[1])
            }
            topPanel.aivThird.apply {
                setNumber(context.getString(R.string.tv_0))
                setActivityInfoItem(ActivityInfoItem.getActivityInfoItem()[2])
            }

            navMain.selectedItemId = R.id.navigation_active
        }
    }

    fun takeScreenshot(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache(true)

        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false

        return bitmap
    }

    private fun initListeners() {
        with(binding) {
            btnStart.setOnClickListener {
                startStopwatch()
                visible(btnPause, vBottomView)
                gone(btnStart, navMain)
                activeViewModel.activityState.isTrainingStart = true
                activeViewModel.updateUI.value = true
            }
            btnPause.setOnClickListener {
                visible(btnEnd, btnContinue, gPause)
                stopStopwatch()
                btnPause.gone()
            }
            btnEnd.setOnClickListener {

                if (activeViewModel.activityState.activityType.id != 4) {
                    val navHostFragment =
                        supportFragmentManager.findFragmentById(R.id.navActivity) as NavHostFragment
                    val fragment =
                        navHostFragment.childFragmentManager.fragments[0] as? MapActivityFragment
                    fragment?.view?.findViewById<MapView>(R.id.mapview)?.apply {
                        activeViewModel.bitmap = takeScreenshot(this)
                    }
                }

                activeViewModel.save.value = true
                gone(btnEnd, btnContinue, gPause, vBottomView)
                visible(navMain, btnStart)
                resetStopwatch()
                activeViewModel.activityState.isTrainingStart = false
                openFragment(SaveActivityFragment())
            }
            btnContinue.setOnClickListener {
                gone(btnEnd, btnContinue, gPause)
                visible(btnPause)
                startStopwatch()
            }

            topPanel.aivFirst.setOnClickListener {
                openFragment(ActivityInfoItemFragment(
                    topPanel.aivFirst.getActivityInfoItem()?.id ?: 0,
                    topPanel.aivSecond.getActivityInfoItem()?.id ?: 0,
                    topPanel.aivThird.getActivityInfoItem()?.id ?: 0
                ) { item ->
                    topPanel.aivFirst.apply {
                        setNumber("0")
                        setTitle(item.title)
                        setActivityInfoItem(item)
                    }
                    activeViewModel.activityState.aiFirst = item
                })
            }
            topPanel.aivSecond.setOnClickListener {
                openFragment(ActivityInfoItemFragment(
                    topPanel.aivSecond.getActivityInfoItem()?.id ?: 0,
                    topPanel.aivFirst.getActivityInfoItem()?.id ?: 0,
                    topPanel.aivThird.getActivityInfoItem()?.id ?: 0
                ) { item ->
                    topPanel.aivSecond.apply {
                        setNumber("0")
                        setTitle(item.title)
                        setActivityInfoItem(item)
                    }
                    activeViewModel.activityState.aiSecond = item
                })
            }
            topPanel.aivThird.setOnClickListener {
                openFragment(ActivityInfoItemFragment(
                    topPanel.aivThird.getActivityInfoItem()?.id ?: 0,
                    topPanel.aivFirst.getActivityInfoItem()?.id ?: 0,
                    topPanel.aivSecond.getActivityInfoItem()?.id ?: 0
                ) { item ->
                    topPanel.aivThird.apply {
                        setNumber("0")
                        setTitle(item.title)
                        setActivityInfoItem(item)
                    }
                    activeViewModel.activityState.aiThird = item
                })
            }

            topPanel.icActivityType.setOnClickListener {
                openFragment(ActivityTypeFragment())
            }

            navMain.setOnItemSelectedListener { item ->
                navigateToMain(item)
            }
        }
    }

    private fun navigateToMain(item: MenuItem): Boolean {
        val id = when (item.itemId) {
            R.id.navigation_home -> {
                R.id.navigation_home
            }

            R.id.navigation_maps -> {
                R.id.navigation_maps
            }

            R.id.navigation_scaner -> {
                R.id.navigation_scaner
            }

            R.id.navigation_user -> {
                R.id.navigation_user
            }

            else -> {
//                activeViewModel.activityState = activeViewModel.activityState.copy(
//                    activityType = TypeOfActivity.getTypeOfActivity().last()
//                )
                0
            }
        }

        if (id != 0) {
            startMainActivity(id)
        }
        return true
    }

    private fun startMainActivity(id: Int? = R.id.navigation_home) {
        startActivity(
            Intent(this@ActivityForActivity, MainActivity::class.java).apply {
                putExtra("ID", id)
            })
        finish()
    }

    private fun startStopwatch() {
        if (!isRunning) {
            isRunning = true
            startTime = SystemClock.elapsedRealtime()
            handler.postDelayed(updateTime, 0)
        }
    }

    private fun stopStopwatch() {
        if (isRunning) {
            isRunning = false
            handler.removeCallbacks(updateTime)
        }
    }

    private fun resetStopwatch() {
        isRunning = false
        handler.removeCallbacks(updateTime)
        binding.topPanel.time.text = getString(R.string.tv_reset_time)
    }

    private val handler = Handler(Looper.getMainLooper())
    private val updateTime: Runnable = object : Runnable {
        override fun run() {
            val timeInMilliseconds = SystemClock.elapsedRealtime() - startTime
            var seconds = (timeInMilliseconds / 1000).toInt()
            var minutes = seconds / 60
            val hours = minutes / 60
            seconds %= 60
            minutes %= 60
            binding.topPanel.time.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            handler.postDelayed(this, 1000)
        }
    }

    private fun observes() {
        activeViewModel.apply {
            navigate.observe(this@ActivityForActivity) {
                it?.let { openFragment(it) }
            }
            saved.observe(this@ActivityForActivity) {
                if (it) openFragment(SaveActivityFragment())
            }
            save.observe(this@ActivityForActivity) {
                val s = it
            }
        }
    }

    private fun updateUI() {
        activeViewModel.updateUI.observe(this@ActivityForActivity) {
            activeViewModel.activityState.let { activityState ->
                with(binding.topPanel) {
                    tvTitleActivityType.text = activityState.activityType.title
                    icActivityType.setImageResource(activityState.activityType.img)

                    if (activityState.activityType.id == 4) {
                        navActivitySettings.apply {
                            menu.removeItem(R.id.mapActivityFragment)
                            if (!activityState.onSelectedTypeFromSetting) {
                                navActivitySettings.selectedItemId = R.id.levelActivityFragment
                            }
                        }
                    } else {
                        if (navActivitySettings.menu.size != 3) {
                            navActivitySettings.apply {
                                menu.clear()
                                navActivitySettings.inflateMenu(R.menu.menu_activity_settings)

                                if (!activityState.onSelectedTypeFromSetting) {
                                    navActivitySettings.selectedItemId = R.id.mapActivityFragment
                                }
                            }
                        }
                    }
                    activeViewModel.activityState.onSelectedTypeFromSetting = false

                    enableIf(
                        !activityState.isTrainingStart,
                        icActivityType,
                        aivFirst,
                        aivSecond,
                        aivThird
                    )
                }
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        fragment.let {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.navFragment, it)
                .commit()

        }
        activeViewModel.navigate.value = null
    }
}