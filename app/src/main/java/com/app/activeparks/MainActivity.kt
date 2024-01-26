package com.app.activeparks

import android.annotation.SuppressLint
import android.content.IntentSender.SendIntentException
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.app.activeparks.data.repository.Repository
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.ui.userProfile.edit.EditProfileFragment
import com.app.activeparks.ui.userProfile.home.ProfileHomeFragment
import com.app.activeparks.ui.userProfile.info.UserInfoFragment
import com.app.activeparks.ui.userProfile.menu.MenuProfileFragment
import com.app.activeparks.util.Dictionarie
import com.app.activeparks.util.FragmentInteface
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), FragmentInteface {
    private var binding: ActivityMainBinding? = null
    private var appUpdateManager: AppUpdateManager? = null
    private val viewModel: MainViewModel by viewModel()
    var navControllerMain: NavController? = null
        private set
    private var preferences: Preferences? = null
    private var repository: Repository? = null

    @SuppressLint("NonConstantResourceId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(binding?.root)
        appUpdateManager = AppUpdateManagerFactory.create(this)
        navControllerMain = findNavController(this, R.id.nav_host_fragment_activity_main)
        setupWithNavController(binding!!.navView, navControllerMain!!)
        Dictionarie().init(this)
        updatePushToken()
        val locale = Locale("uk-rUA")
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(configuration, null)
        preferences = Preferences(this)
        repository = Repository(preferences)
        binding!!.navView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navControllerMain!!.navigate(R.id.navigation_home)
                }

                R.id.navigation_maps -> {
                    navControllerMain!!.navigate(R.id.navigation_maps)
                }

                R.id.navigation_scaner -> {
                    navControllerMain!!.navigate(R.id.navigation_scaner)
                }

                R.id.navigation_active -> {
                    if (preferences?.token == null || preferences?.token.isNullOrEmpty()) {
                        navControllerMain?.navigate(R.id.registration_user)
                    } else {
                        navControllerMain?.navigate(R.id.navigation_active)
                    }
                }

                R.id.navigation_user -> {
                    if (preferences?.token == null || preferences?.token.isNullOrEmpty()) {
                        navControllerMain?.navigate(R.id.registration_user)
                    } else {
                        navControllerMain?.navigate(R.id.navigation_user)
                    }
                }
            }
            false
        }

        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                try {
                    appUpdateManager?.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        this,
                        1
                    )
                } catch (e: SendIntentException) {
                    throw RuntimeException(e)
                }
            }
        }
    }

    fun openFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()
    }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.navFragment, fragment)
            .commit()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size > 1) {
            val fragment =
                supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1]
            if (fragment is UserInfoFragment ||
                fragment is MenuProfileFragment ||
                fragment is ProfileHomeFragment ||
                fragment is EditProfileFragment
            ) {
                supportFragmentManager
                    .beginTransaction()
                    .remove(
                        supportFragmentManager
                            .fragments[supportFragmentManager.fragments.size - 1]
                    )
                    .commit()

                onResume()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        navigation(intent.getIntExtra("ID", R.id.navigation_home))
        intent.removeExtra("ID")
        viewModel.updateUserInfo()
    }

    override fun show(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, fragment)
            .commit()
    }

    override fun navigation(id: Int) {
        binding?.navView?.selectedItemId = id
    }

    override fun message(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun updatePushToken() {
        val preferences = Preferences(this)
        if (preferences.pushToken != null) {
            Repository(preferences).setPush(preferences.pushToken)
        }
        if (preferences.token != null && preferences.token.isNotEmpty()) {
            reviewManager()
        }
    }

    private fun reviewManager() {
        val manager = ReviewManagerFactory.create(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task: Task<ReviewInfo?> ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                reviewInfo?.apply {
                    manager.launchReviewFlow(this@MainActivity, this)
                        .addOnCompleteListener { _: Task<Void?>? -> }
                }
            }
        }
    }
}