package com.app.activeparks.ui.active.fragments.saveActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.db.mapper.ActivityStateToActiveEntityMapper
import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCase
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.ui.active.model.CurrentActivity
import com.app.activeparks.ui.active.model.StartInfo
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

/**
 * Created by O.Dziuba on 08.11.2023.
 */
class SaveActivityViewModel(
    private val saveActivityUseCase: SaveActivityUseCase
) : ViewModel() {

    var currentActivity = CurrentActivity()
    var startInfo = StartInfo()

    fun saveActivity(
        activeRoad: List<GeoPoint>,
        activityInfoItems: List<ActivityInfoTrainingItem>
    ) {
        viewModelScope.launch {
            kotlin.runCatching {
                saveActivityUseCase.insert(
                    ActivityStateToActiveEntityMapper.map(
                        startInfo,
                        currentActivity,
                        activeRoad,
                        activityInfoItems
                    )
                )
            }
        }
    }
}