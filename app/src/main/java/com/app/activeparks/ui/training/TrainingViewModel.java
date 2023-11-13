package com.app.activeparks.ui.training;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.model.workout.WorkoutItem;
import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.data.repository.Repository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class TrainingViewModel extends ViewModel {

    private final Preferences sharedPreferences;
    private Repository repository;
    private MutableLiveData<WorkoutItem> workoutItem = new MutableLiveData<>();

    private String  planId, userId;

    private List<String> exercises = new ArrayList<>();

    public TrainingViewModel(Preferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.repository = new Repository(sharedPreferences);
    }

    public LiveData<WorkoutItem> getWorkout() {
        return workoutItem;
    }

    public List<String> getExercises() {
        return exercises;
    }

    public void addExercises(String title) {
        exercises.add(title);
    }

    public void remove(int index) {
        exercises.remove(index);
    }

    public void workout(String id) {
        planId = id;
        repository.workout(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            workoutItem.setValue(result);
                            exercises = result.getExercises();
                        },
                        error -> Log.e("ProfileViewModel", "getPokemons: " + error.getMessage())
                );
    }

    public void workoutAdd(String title, boolean isOnce, String weekDays, String startTime, String finishTime) {
        repository.workoutAdd(title, isOnce, weekDays, startTime, finishTime, exercises, userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> { Log.d("workoutAdd", " msg: " + result.string());
                        },
                        error -> { Log.d("workoutAdd", " msg: " + error.getMessage());
                        });
    }

    public void workoutUpdate(String title, boolean isOnce, String weekDays, String startTime, String finishTime) {
        repository.workoutUpdate(planId, title, isOnce, weekDays, startTime, finishTime, exercises, userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                        },
                        error -> {
                        });
    }

    public void delete() {
        repository.workoutRemove(workoutItem.getValue().getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                        },
                        error -> {
                        });
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}