package com.app.activeparks.ui.dialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.city.Body;
import com.app.activeparks.data.model.city.City;
import com.app.activeparks.ui.adapter.SearchAdaper;
import com.technodreams.activeparks.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.Nullable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BottomSearchDialog extends BottomSheetDialogFragment{

    public static BottomSearchDialog newInstance() {
        return new BottomSearchDialog();
    }

    public EditText searchCity;
    private Repository repository;
    public RecyclerView listCity;
    public SearchDialogListener searchDialogListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bottom_search, container,
                false);

        repository = new Repository();

        searchCity = view.findViewById(R.id.search_city);
        listCity = view.findViewById(R.id.search_list);

        searchCity("Київ");
        searchCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String city = searchCity.getText().toString();
                if (city.length() > 2) {
                    searchCity(city);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    public void searchCity(String search){
        repository.searchCity(search).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            listCity.setAdapter(new SearchAdaper(getContext(), result.getBody()).setOnCliclListener(new SearchAdaper.SearchAdaperListener() {
                                @Override
                                public void onInfo(Body item) {
                                    double lat = Double.parseDouble(item.getLatitude());
                                    double lon = Double.parseDouble(item.getLongitude());
                                    if (searchDialogListener != null) {
                                        searchDialogListener.onLong(lat, lon);
                                        dismiss();
                                    }
                                }
                            }));
                        },
                        error -> {
                        });
    }

    public interface SearchDialogListener{
        void onLong(double lat, double lon);
    }

    public BottomSearchDialog setOnCliclListener(SearchDialogListener searchDialogListener){
        this.searchDialogListener = searchDialogListener;
        return this;
    }
}