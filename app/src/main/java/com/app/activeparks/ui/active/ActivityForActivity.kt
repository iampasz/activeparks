package com.app.activeparks.ui.active

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.active.fragments.infoItem.ActivityInfoItemFragment
import com.app.activeparks.ui.active.fragments.map.MapActivityFragment
import com.app.activeparks.ui.active.fragments.saveActivity.SaveActivityFragment
import com.app.activeparks.ui.active.fragments.type.ActivityTypeFragment
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.ui.active.model.ActivityState
import com.app.activeparks.ui.active.util.AddressUtil
import com.app.activeparks.util.extention.disable
import com.app.activeparks.util.extention.enable
import com.app.activeparks.util.extention.enableIf
import com.app.activeparks.util.extention.getStingForSpeak
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentActiveBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class ActivityForActivity : AppCompatActivity() {

    private lateinit var binding: FragmentActiveBinding
    private val activeViewModel: ActiveViewModel by viewModel()
    private lateinit var navController: NavController

    private var isTimerRunning = false

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentActiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.topPanel.navActivitySettings

        navController = findNavController(R.id.navActivity)
        navView.setupWithNavController(navController)


        textToSpeech = TextToSpeech(this) { status ->
            textToSpeech.language = Locale("ukr", "UKR")
            if (status != TextToSpeech.SUCCESS) {
                Toast.makeText(this, "TextToSpeech initialization failed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        initStartValue()
        initListeners()
        observes()
        updateTimerText()
        updateUI()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size > 1) {
            (supportFragmentManager.fragments.last() as? SaveActivityFragment)?.let {
                activeViewModel.apply {
                    updateMap.value = true
                    updateUI.value = true
                    loadActiveState()
                    activityState.activityInfoItems = ActivityInfoTrainingItem.getActivityInfoItem()
                }
            }
            supportFragmentManager.beginTransaction()
                .remove(supportFragmentManager.fragments.last()).commit()
            activeViewModel.updateUI.value = true
        } else {
            if (activeViewModel.activityState.isTrainingStart &&
                supportFragmentManager.fragments.first().childFragmentManager.fragments.first() is MapActivityFragment
            ) {
                showPauseDialog { super.onBackPressed() }
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun showPauseDialog(back: () -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.tv_dialog_on_pause_activity))
        builder.setPositiveButton(getString(R.string.tv_yes)) { dialog, _ ->
            activeViewModel.pauseActivity()
            back()
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.tv_no)) { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun initStartValue() {
        activeViewModel.isActivityPause()
        with(binding) {
            setInfoItem()

            navMain.selectedItemId = R.id.navigation_active
        }
    }

    private fun FragmentActiveBinding.setInfoItem() {
        topPanel.aivFirst.apply {
            if (activeViewModel.activityState.activityType.isOutside) {
                setActivityInfoItem(activeViewModel.activityState.aiFirst)
            } else {
                setActivityInfoItem(activeViewModel.activityState.aiFirstOutside)
            }
        }
        topPanel.aivSecond.apply {
            if (activeViewModel.activityState.activityType.isOutside) {
                setActivityInfoItem(activeViewModel.activityState.aiSecond)
            } else {
                setActivityInfoItem(activeViewModel.activityState.aiSecondOutside)
            }
        }
        topPanel.aivThird.apply {
            if (activeViewModel.activityState.activityType.isOutside) {
                setActivityInfoItem(activeViewModel.activityState.aiThird)
            } else {
                setActivityInfoItem(activeViewModel.activityState.aiThirdOutside)
            }
        }
    }

    private fun initListeners() {
        with(binding) {
            btnStart.setOnClickListener {
                btnStart.disable()
                if (activeViewModel.activityState.isAudioHelper) {
                    if (activeViewModel.activityState.isLazyStart) {
                        startDelayedStartTimer()
                    } else {
                        showToastAndSpeak(getString(R.string.tv_start_activity_speak))
                        startActivity()
                    }
                } else if (activeViewModel.activityState.isLazyStart) {
                    startDelayedStartTimer()
                } else {
                    startActivity()
                }

                activeViewModel.saveActiveState()
            }
            btnPause.setOnClickListener {
                onPause()
            }
            btnEnd.setOnClickListener {
                btnStart.enable()
                gone(btnEnd, btnContinue, gPause, vBottomView)
                visible(navMain, btnStart)
                restartTimer()
                with(activeViewModel) {
                    activityState.isTrainingStart = false
                    activityState.isPause = false
                    checkLocation.value = false
                    getWeather()
                    activityState.activityTime.finishesAt = System.currentTimeMillis()
                    activityState.activityTime.time =
                        activityState.activityTime.finishesAt - activityState.activityTime.startsAt

                    location?.let {
                        if (activityState.startPoint.isEmpty()) {
                            activityState.startPoint =
                                AddressUtil.getAddressFromLocation(this@ActivityForActivity, it)
                        }
                    }
                }
                setEnableActivityInfo()
                activeViewModel.deletePauseActivity()
            }
            btnContinue.setOnClickListener {
                gone(btnEnd, btnContinue, gPause)
                visible(btnPause)
                resumeTimer()
                activeViewModel.activityState.isPause = false
                setEnableActivityInfo()
            }

            topPanel.aivFirst.setOnClickListener {
                openFragment(ActivityInfoItemFragment(
                    topPanel.aivFirst.getActivityInfoItem()?.id ?: 0,
                    topPanel.aivSecond.getActivityInfoItem()?.id ?: 0,
                    topPanel.aivThird.getActivityInfoItem()?.id ?: 0
                ) { item ->
                    topPanel.aivFirst.apply {
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

    private fun FragmentActiveBinding.onPause() {
        visible(btnEnd, btnContinue, gPause)
        pauseTimer()
        btnPause.gone()
        activeViewModel.activityState.isPause = true

        setEnableActivityInfo()
    }

    private fun FragmentActiveBinding.setEnableActivityInfo() {
        enableIf(
            !activeViewModel.activityState.isPause,
            topPanel.aivFirst,
            topPanel.aivSecond,
            topPanel.aivThird
        )
    }

    private fun startActivity() {
        with(binding) {
            startTimer()
            visible(btnPause, vBottomView)
            gone(btnStart, navMain)
            with(activeViewModel) {
                activityState.isTrainingStart = true
                updateUI.value = true
                checkLocation.value = true
                activityState.activityTime.startsAt = System.currentTimeMillis()
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

    private fun startTimer() {
        isTimerRunning = true
        runnable = object : Runnable {
            override fun run() {
                activeViewModel.activityState.activityDuration++
                updateTimerText()
                handler.postDelayed(this, 1000)
            }
        }
        runnable?.let { handler.post(it) }
    }

    private fun pauseTimer() {
        isTimerRunning = false
        runnable?.let { handler.removeCallbacks(it) }

    }

    private fun resumeTimer() {
        isTimerRunning = true
        runnable?.let { handler.post(it) }
    }

    private fun restartTimer() {
        isTimerRunning = false
        activeViewModel.activityState.activityDuration = 0
        updateTimerText()
    }

    private fun updateTimerText() {
        val hours = activeViewModel.activityState.activityDuration / 3600
        val minutes = (activeViewModel.activityState.activityDuration % 3600) / 60
        val secs = activeViewModel.activityState.activityDuration % 60

        binding.topPanel.time.text =
            String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs)
    }

    private fun observes() {
        activeViewModel.apply {
            navigate.observe(this@ActivityForActivity) {
                it?.let { openFragment(it) }
            }
            saved.observe(this@ActivityForActivity) {
                if (it) openFragment(SaveActivityFragment())
            }
            updateActivityInfoTrainingItem.observe(this@ActivityForActivity) {
                if (it) {
                    updateInfoItem(activityState)

                    if (activityState.pulseOnPause >= activityState.currentPulse && activityState.isAutoPause) {
                        binding.onPause()
                    }
                }
            }

            updateWeather.observe(this@ActivityForActivity) {
                if (it) {
                    openFragment(SaveActivityFragment())
                    updateWeather.value = false
                }
            }
        }
    }

    private fun updateUI() {
        activeViewModel.updateUI.observe(this@ActivityForActivity) {
            activeViewModel.activityState.let { activityState ->
                with(binding.topPanel) {
                    tvTitleActivityType.text = activityState.activityType.title
                    icActivityType.setImageResource(activityState.activityType.img)

                    if (activityState.activityType.isInclude) {
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

                    icActivityType.enableIf(!activityState.isTrainingStart)

                    binding.setInfoItem()

                    updateInfoItem(activityState)

                    if (activityState.isPause) {
                        if (runnable == null) {
                            startTimer()
                            updateTimerText()
                        }
                        with(binding) {
                            onPause()
                            visible(btnEnd, btnContinue, gPause, vBottomView)
                            gone(btnStart, navMain)
                        }
                    }
                }
            }
        }
    }

    private fun updateInfoItem(activityState: ActivityState) {
        with(binding) {
            topPanel.aivFirst.getActivityInfoItem()?.id?.let { id ->
                topPanel.aivFirst.setNumber(activityState.activityInfoItems[id].number)
            }
            topPanel.aivSecond.getActivityInfoItem()?.id?.let { id ->
                topPanel.aivSecond.setNumber(activityState.activityInfoItems[id].number)
            }
            topPanel.aivThird.getActivityInfoItem()?.id?.let { id ->
                topPanel.aivThird.setNumber(activityState.activityInfoItems[id].number)
            }
        }
    }

    private fun startDelayedStartTimer() {
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                showToastAndSpeak(millisUntilFinished.getStingForSpeak())
            }

            override fun onFinish() {
                showToastAndSpeak(getString(R.string.tv_start_activity_speak))
                startActivity()
            }
        }.start()
    }

    private fun showToastAndSpeak(message: String) {
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
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