package com.app.activeparks.ui.profile;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.ui.dialog.BottomEmailDialog;
import com.app.activeparks.ui.dialog.BottomPhoneDialog;
import com.app.activeparks.util.cropper.CropImage;
import com.app.activeparks.util.cropper.CropImageView;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.technodreams.activeparks.R;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class EditProfileActivity extends BottomSheetDialogFragment {

    private EditProfileInterfece editProfileInterfece;

    private ProfileViewModelOld viewModel;
    final Calendar mCalendar = Calendar.getInstance();
    private ImageView photo;
    private EditText phone, email, firstName, lastName, secondName, birthday, weight, height, about, healt, city;
    private Spinner sex, region, district;


    public static EditProfileActivity newInstance() {
        return new EditProfileActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        viewModel =
                new ViewModelProvider(this, new ProfileModelFactory(getContext())).get(ProfileViewModelOld.class);
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
            if (editProfileInterfece != null) {
                editProfileInterfece.onUpdate();
            }
            dismiss();
        });

        DatePickerDialog.OnDateSetListener date = (view1, year, month, day) -> {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateDate();
        };

        birthday.setOnClickListener(view12 -> new DatePickerDialog(requireContext(), date, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show());

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(requireContext(), R.array.sex,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex.setAdapter(adapter);

        phone.setOnClickListener((View v) -> {
            BottomPhoneDialog dialog = BottomPhoneDialog.newInstance();
            dialog.onListener(() -> viewModel.user());
            dialog.show(requireActivity().getSupportFragmentManager(),
                    "fragment_phone");
        });

        email.setOnClickListener((View v) -> {
            BottomEmailDialog dialog = BottomEmailDialog.newInstance();
            dialog.show(requireActivity().getSupportFragmentManager(),
                    "fragment_email");

        });

        view.findViewById(R.id.photo_action).setOnClickListener((View v) -> galleryDialog());

        view.findViewById(R.id.save_action).setOnClickListener((View v) -> updateUser());

        viewModel.user();

        viewModel.getUser().observe(this, user -> {

                phone.setText(user.getPhone());
                email.setText(user.getEmail());

                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                secondName.setText(user.getSecondName());

                region.setSelection(viewModel.getRegionsIndex());
                district.setSelection(viewModel.getDictionarieIndex());

                sex.setSelection(user.getSex().contains("male") ? 0 : 1);

                try {
                    @SuppressLint("SimpleDateFormat") Date dateBirthday = new SimpleDateFormat("yyyy-MM-dd").parse(user.getBirthday());
                    assert dateBirthday != null;
                    birthday.setText(new SimpleDateFormat("dd.MM.yyyy", new Locale("uk", "UA")).format(dateBirthday));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                height.setText(user.getHeight() == null ? "" : user.getHeight() + "");
                weight.setText(user.getWeight() == null ? "" : user.getWeight() + "");
                about.setText(user.getAboutMe() == null ? "" : user.getAboutMe() + "");
                healt.setText(user.getHealthState() == null ? "" : user.getHealthState() + "");

                city.setText(user.getCity() == null ? "" : user.getCity() + "");
                if (user.getPhoto() != null) {
                    Glide.with(this).load(user.getPhoto()).error(R.drawable.ic_prew).into(photo);
                }

        });

        viewModel.getDefault().observe(this, def -> {
            editProfileInterfece.onUpdate();
            dismiss();
        });

        viewModel.getMessage().observe(this, msg -> Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show());


        return view;
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setFixAspectRatio(true)
                .setCropMenuCropButtonTitle("Зберегти")
                .start(requireActivity(), this);
    }

    private void updateDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        birthday.setText(dateFormat.format(mCalendar.getTime()));
    }

    private void updateUser() {
        if (viewModel.userUpdate != null) {
            viewModel.mProfile.setPhone(phone.getText().toString());
            viewModel.mProfile.setEmail(email.getText().toString());

            if (!firstName.getText().toString().isEmpty()) {
                viewModel.userUpdate.setFirstName(firstName.getText().toString());
            }
            if (!lastName.getText().toString().isEmpty()) {
                viewModel.userUpdate.setLastName(lastName.getText().toString());
            }
            if (!secondName.getText().toString().isEmpty()) {
                viewModel.userUpdate.setSecondName(secondName.getText().toString());
            }

            if (!birthday.getText().toString().isEmpty()) {
                try {
                    @SuppressLint("SimpleDateFormat") Date dateBirthday = new SimpleDateFormat("dd.MM.yyyy").parse(birthday.getText().toString());
                    assert dateBirthday != null;
                    viewModel.userUpdate.setBirthday(new SimpleDateFormat("yyyy-MM-dd", new Locale("uk", "UA")).format(dateBirthday));
                } catch (ParseException e) {
                    e.printStackTrace();
                    viewModel.userUpdate.setBirthday(null);
                }
            }

            viewModel.userUpdate.setHeight(height.getText().toString());
            viewModel.userUpdate.setWeight(weight.getText().toString());

            if (about.getText().toString().length() > 0) {
                viewModel.userUpdate.setAboutMe(about.getText().toString());
            }
            if (healt.getText().toString().length() > 0) {
                viewModel.userUpdate.setHealthState(healt.getText().toString());
            }

            viewModel.userUpdate.setSex(sex.getSelectedItemPosition() == 0 ? "male" : "female");

            city.getText();
            viewModel.userUpdate.setCity(city.getText().toString());

            viewModel.updateUser();
            if (editProfileInterfece != null) {
                editProfileInterfece.onUpdate();
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    void galleryDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme);
        dialog.setContentView(R.layout.dialog_gallery);


        LinearLayout galleryAction = dialog.findViewById(R.id.gallery_action);
        assert galleryAction != null;
        galleryAction.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
            } else {
                Toast.makeText(getActivity(), "Додайте доступ до фото", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
            dialog.dismiss();
        });

        LinearLayout cameraAction = dialog.findViewById(R.id.camera_action);
        assert cameraAction != null;
        cameraAction.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 4);
                }
            } else {
                Toast.makeText(getActivity(), "Додайте доступ до камери", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        5);
            }
            dialog.dismiss();
        });

        LinearLayout cancel = dialog.findViewById(R.id.cancel);
        assert cancel != null;
        cancel.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();

                startCropImageActivity(uri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri croppedImageUri = result.getUri();

                Bitmap bm;
                try {
                    File file = saveImageToFile(croppedImageUri);

                    viewModel.updateFile(file);

                    bm = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), croppedImageUri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                photo.setImageBitmap(bm);
            }  // Handle the error

        }
        if (requestCode == 4 && resultCode == RESULT_OK) {
            // Get the photo as a Bitmap
            Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");

            try {
                // Get the directory to save the file in
                File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());

                // Create a new file with a unique name in the directory
                File camera = File.createTempFile("image_", ".jpg", directory);

                // Create a file output stream to write the Bitmap to the new file
                FileOutputStream fileOutputStream = new FileOutputStream(camera);

                // Write the Bitmap to the file as JPEG with 100% quality
                assert bitmap != null;
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

                // Flush and close the file output stream
                fileOutputStream.flush();
                fileOutputStream.close();
                viewModel.updateFile(camera);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private File saveImageToFile(Uri imageUri) {
        ContentResolver resolver = requireActivity().getContentResolver();
        File file = null;

        try {
            InputStream inputStream = resolver.openInputStream(imageUri);

            file = new File(requireActivity().getFilesDir(), "image.jpg");

            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while (true) {
                assert inputStream != null;
                if ((bytesRead = inputStream.read(buffer)) == -1) break;
                outputStream.write(buffer, 0, bytesRead);
            }

            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (editProfileInterfece != null) {
            editProfileInterfece.onUpdate();
        }
    }



    public interface EditProfileInterfece {
        void onUpdate();
    }

    public EditProfileActivity setOnEditProfile(EditProfileInterfece editProfileInterfece) {
        this.editProfileInterfece = editProfileInterfece;
        return this;
    }

}