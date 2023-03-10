package com.app.activeparks.ui.profile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.CursorLoader;

import com.app.activeparks.ui.training.TrainingDialog;
import com.app.activeparks.ui.training.TrainingModelFactory;
import com.app.activeparks.ui.training.TrainingViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.technodreams.activeparks.R;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditProfileActivity extends BottomSheetDialogFragment {

    private ProfileViewModel mViewModel;

    final Calendar mCalendar = Calendar.getInstance();

    private ImageView photo;

    private EditText phone, email, firstName, lastName, secondName, birthday,  weight, height, about, healt, city;

    private Spinner sex, region, district;

    public  static  EditProfileActivity newInstance() {
        return new EditProfileActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);


        mViewModel =
                new ViewModelProvider(this, new ProfileModelFactory(getContext())).get(ProfileViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_edit, container,
                false);

        photo = view.findViewById(R.id.image_avatar);

        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);

        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        secondName = view.findViewById(R.id.secondName);
        sex = view.findViewById(R.id.sex);
        birthday = view.findViewById(R.id.birthday);
        weight = view.findViewById(R.id.weight);
        height = view.findViewById(R.id.height);
        about = view.findViewById(R.id.about);
        healt = view.findViewById(R.id.healt);

        birthday = view.findViewById(R.id.birthday);

        region = view.findViewById(R.id.region);
        district = view.findViewById(R.id.district);
        city = view.findViewById(R.id.city);

        view.findViewById(R.id.closed).setOnClickListener((View v) -> {
            dismiss();
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH,month);
                mCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateDate();
            }
        };

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(getContext(), R.array.sex,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex.setAdapter(adapter);
        ArrayAdapter<String> reg = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, mViewModel.getRegions());
        reg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        region.setAdapter(reg);
        region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int s, long selectedId) {

                ArrayAdapter<String> dis = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, mViewModel.getDictionarie(s));
                dis.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                district.setAdapter(dis);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        view.findViewById(R.id.photo_action).setOnClickListener((View v) -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
        });

        view.findViewById(R.id.save_action).setOnClickListener((View v) -> {
            updateUser();
        });

        mViewModel.user();

        mViewModel.getUser().observe(this, user -> {
            try{
                phone.setText(user.getPhone());
                email.setText(user.getEmail());

                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                secondName.setText(user.getSecondName());

                region.setSelection(mViewModel.getRegionsIndex());
                district.setSelection(mViewModel.getDictionarieIndex());

                sex.setSelection(user.getSex() == "male" ? 1 : 0);
                birthday.setText(user.getBirthday());

                height.setText(user.getHeight() + "");
                weight.setText(user.getWeight() + "");
                about.setText(user.getAboutMe() + "");
                healt.setText(user.getHealthState() + "");

                city.setText(user.getCity());

                Glide.with(this).load(user.getPhoto()).into(photo);
            }catch (Exception e){}
        });

        mViewModel.getDefault().observe(this, def -> {
            dismiss();
        });

        mViewModel.getMessage().observe(this, msg -> {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();

                String[] proj = {MediaStore.Images.Media.DATA};
                CursorLoader loader = new CursorLoader(getActivity(), uri, proj, null, null, null);
                Cursor cursor = loader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String result = cursor.getString(column_index);
                cursor.close();

                File file = new File(result);

                mViewModel.updateFile(file);

                try {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    photo.setImageBitmap(bm);
                }  catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateDate(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        birthday.setText(dateFormat.format(mCalendar.getTime()));
    }

    private void updateUser(){
        if (mViewModel.mProfile != null) {
            mViewModel.mProfile.setPhone(phone.getText().toString());
            mViewModel.mProfile.setEmail(email.getText().toString());

            mViewModel.mProfile.setFirstName(firstName.getText().toString());
            mViewModel.mProfile.setLastName(lastName.getText().toString());
            mViewModel.mProfile.setSecondName(secondName.getText().toString());

            mViewModel.mProfile.setBirthday(birthday.getText().toString());

            mViewModel.mProfile.setHeight(height.getText().toString());
            mViewModel.mProfile.setWeight(weight.getText().toString());

            mViewModel.mProfile.setAboutMe(about.getText().toString());
            mViewModel.mProfile.setHealthState(healt.getText().toString());

            mViewModel.mProfile.setSex(sex.getSelectedItemPosition() == 0 ? "male" : "female");

            mViewModel.mProfile.setCity(city.getText().toString());
            mViewModel.updateUser();
        }
    }
}