package com.app.activeparks.ui.active.fragments.saveActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.db.entity.Active
import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCase
import com.app.activeparks.ui.active.model.CurrentActivity
import kotlinx.coroutines.launch

/**
 * Created by O.Dziuba on 08.11.2023.
 */
class SaveActivityViewModel(
    private val saveActivityUseCase: SaveActivityUseCase
) : ViewModel() {

    var currentActivity = CurrentActivity()

    fun saveActivity() {
        viewModelScope.launch {
            kotlin.runCatching {
                saveActivityUseCase.insert(Active(
                    currentActivity.location!!,
                    currentActivity.weather!!,
                    currentActivity.dateTime!!,
                    currentActivity.titleActivity!!,
                    currentActivity.descriptionActivity!!,
                    currentActivity.feeling!!
                ))
                saveActivityUseCase.getActives()
            }.onSuccess {
                val s = it

            }.onFailure {
                val s = it
            }
        }
    }
}