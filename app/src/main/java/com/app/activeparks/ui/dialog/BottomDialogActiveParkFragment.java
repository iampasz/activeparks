package com.app.activeparks.ui.dialog;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.activeparks.data.model.sportsgrounds.ItemSportsground;
import com.app.activeparks.ui.park.ParkActivity;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.Nullable;

public class BottomDialogActiveParkFragment extends BottomSheetDialogFragment{

    public static BottomDialogActiveParkFragment newInstance() {
        return new BottomDialogActiveParkFragment();
    }

    public String  lat, lon;
    public ImageView image;
    public Button infoAction, mapAction;
    public TextView title;
    public ItemSportsground sportsground;


    public BottomDialogActiveParkFragment setSportsground(ItemSportsground sportsground){
        this.sportsground = sportsground;
        return this;
    }

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


        View view = inflater.inflate(R.layout.dialog_bottom_sheet, container,
                false);

        image = view.findViewById(R.id.image_park);

        title = view.findViewById(R.id.title_park);

        infoAction = view.findViewById(R.id.info_action);
        mapAction = view.findViewById(R.id.map_action);

        if (sportsground != null && sportsground.getTitle() != null) {
            title.setText(sportsground.getTitle());
        }

        if (sportsground.getPhoto() != null) {
            Glide.with(this).load(sportsground.getPhoto()).error(R.drawable.ic_prew).into(image);
        }else{
            Glide.with(this).load(sportsground.getPhotos().get(0)).error(R.drawable.ic_prew).into(image);
        }

        infoAction.setOnClickListener(v-> {
            startActivity(new Intent(getActivity(), ParkActivity.class).putExtra("id", sportsground.getId()));
        });

        mapAction.setOnClickListener(v-> {
            if (sportsground.getLocation() != null) {
                String uri = "https://google.com/maps/search/?api=1&query=" + sportsground.getLocation().get(0) + "," + sportsground.getLocation().get(1);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
        return view;

    }
}