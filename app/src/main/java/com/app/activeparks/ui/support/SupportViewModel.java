package com.app.activeparks.ui.support;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.data.model.dictionaries.BaseDictionaries;
import com.app.activeparks.data.model.support.Support;
import com.app.activeparks.data.model.support.SupportItem;
import com.app.activeparks.data.storage.Preferences;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SupportViewModel extends ViewModel {

    private final Preferences sharedPreferences;
    private Repository repository;
    private MutableLiveData<Support> mSupport = new MutableLiveData<>();
    private MutableLiveData<SupportItem> mSupportDetails = new MutableLiveData<>();
    public List<SupportItem> mSupportItem = new ArrayList<>();
    public SupportItem mSupportUpdate = new SupportItem();

    public SupportViewModel(Preferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.repository = new Repository(sharedPreferences);
    }

    public LiveData<Support> getSupportList() {
        return mSupport;
    }

    public LiveData<SupportItem> getSupportItem() {
        mSupportUpdate = mSupportDetails.getValue();
        return mSupportDetails;
    }
    public void getSupport() {
        repository.getSupportList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> supportMapper(result.getItems()),
                        error -> {
                        });
    }

    public void getSupportDetails(String id) {
        repository.getSupportDetails(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result != null) {
                                mSupportDetails.setValue(result);
                                mSupportUpdate = result;
                            }
                        },
                        error -> {
                        });
    }

    public void createSupport() {
        repository.createSupport().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result != null) {
                                mSupportDetails.setValue(result);
                                mSupportUpdate = result;
                            }
                        },
                        error -> {
                        });
    }

    public void updateSupport(String topic, String text) {
        if (mSupportUpdate != null) {
            mSupportUpdate.setTopic(topic != null ? topic : mSupportUpdate.getTopic());
            mSupportUpdate.setText(text != null ? text : mSupportUpdate.getText());
            repository.updateSupport(mSupportUpdate.getId(), mSupportUpdate).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> sendSupport(),
                            error -> {
                            });
        }
    }

    public void sendSupport() {
        repository.sendSupportMessage(mSupportUpdate.getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                        },
                        error -> {
                        });
    }

    public void sendSupportMessage(String msg) {
        if (mSupportUpdate.getId() != null) {
            repository.sendSupportMessage(mSupportUpdate.getId(), msg).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> getSupportDetails(mSupportUpdate.getId()),
                            error -> getSupportDetails(mSupportUpdate.getId()));
        }
    }

    public void supportMapper(List<SupportItem> supportItems) {
        List<BaseDictionaries> eventHoldingStatuses = sharedPreferences.getDictionarie().getSupportTicketStatuses();
        for (SupportItem supportItem : supportItems) {
            for (BaseDictionaries base : eventHoldingStatuses) {
                if (supportItem.getStatusId().equals(base.getId())) {
                    supportItem.setStatusId(base.getTitle());
                }
            }
            mSupportItem.add(supportItem);
        }
        Support sportEvents = new Support().setItems(mSupportItem);
        mSupport.setValue(sportEvents);
    }
}