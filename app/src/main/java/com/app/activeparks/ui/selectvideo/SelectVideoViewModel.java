package com.app.activeparks.ui.selectvideo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.dictionaries.Dictionaries;
import com.app.activeparks.data.storage.Preferences;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SelectVideoViewModel extends ViewModel {

    public ModelSelectCategory modelSelectCategory;
    private Dictionaries district;
    private MutableLiveData<ModelSelectCategory> idVideo;

    public SelectVideoViewModel(Preferences sharedPreferences)   {
        idVideo = new MutableLiveData<>();
        modelSelectCategory = new ModelSelectCategory();
        district = sharedPreferences.getDictionarie();
        districtUpdate();
    }

    public LiveData<ModelSelectCategory> showVideo() {
        return idVideo;
    }

    void districtUpdate(){
        new Repository().getDictionaries().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            district = result;
                        },
                        error -> {});
    }


    void selectActivePark(int action){
        switch (action){
            case 0: modelSelectCategory.TYPE_CATEGORY_ID = "fb7b81dc-b6dc-4e14-a43d-56839be90c5c";
                break;
            case 1: modelSelectCategory.TYPE_CATEGORY_ID = "76f644fd-04ca-400a-b767-1b4370c9a5ec";
                break;
            case 2: modelSelectCategory.TYPE_CATEGORY_ID = "7e1937d2-3a07-4473-a1bb-a225d77ffd45";
                break;
            case 3: modelSelectCategory.TYPE_CATEGORY_ID = "748b6683-0968-45d0-91ac-893d69525a6e";
                break;
            case 4: modelSelectCategory.TYPE_CATEGORY_ID = "571f4d2c-cfea-476f-966b-c8e27b3cdac9";
                break;
            case 5: modelSelectCategory.TYPE_CATEGORY_ID = "a2f3d609-3775-4fcc-bad0-4d8351830e1d";
                break;
        }
    }


    void type(int action) {
        if (district.getExerciseDifficultyLevels().size() >= action) {
            modelSelectCategory.TYPE_DIFFICULTY_LEVEL_ID = district.getExerciseDifficultyLevels().get(action).getId();
            idVideo.setValue(modelSelectCategory);
        }
    }


    void rozmenka() {
        modelSelectCategory.TYPE_CATEGORY_ID = "4ae46939-5c80-46f9-8441-96082ba197d3";
        modelSelectCategory.TYPE_DIFFICULTY_LEVEL_ID = "43378ef5-e55d-45ee-b610-31a4d25e4193";
        idVideo.setValue(modelSelectCategory);
    }

    void likar() {
        modelSelectCategory.TYPE_CATEGORY_ID = "a0ad6f28-8562-41c9-93dc-b114aa9626c3";
        modelSelectCategory.TYPE_DIFFICULTY_LEVEL_ID = "43378ef5-e55d-45ee-b610-31a4d25e4193";
        idVideo.setValue(modelSelectCategory);
    }
}