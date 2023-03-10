package com.app.activeparks.ui.profile;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.uservideo.UserVideoItem;
import com.app.activeparks.data.model.workout.WorkoutItem;
import com.app.activeparks.ui.clubs.ClubActivity;
import com.app.activeparks.ui.clubs.adapter.ClubsAdaper;
import com.app.activeparks.ui.event.EventActivity;
import com.app.activeparks.ui.event.adapter.EventsListAdaper;
import com.app.activeparks.ui.profile.uservideo.UserAddVideoActivity;
import com.app.activeparks.ui.profile.uservideo.adapter.UserVideoAdapter;
import com.app.activeparks.ui.support.SupportActivity;
import com.app.activeparks.ui.training.TrainingDialog;
import com.app.activeparks.ui.auth.AuthFragment;
import com.app.activeparks.ui.workout.adapter.journal.JournalListAdaper;
import com.app.activeparks.ui.workout.adapter.plan.PlanListAdaper;
import com.app.activeparks.util.ButtonSelect;
import com.app.activeparks.util.FragmentInteface;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;

    private View binding;
    private ImageView photo;

    private RecyclerView profileList;
    private ProgressBar profileFilling;
    private ButtonSelect clubs, event, result, video, journal, plan;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this, new ProfileModelFactory(getContext())).get(ProfileViewModel.class);

        binding = inflater.inflate(R.layout.fragment_profile, container, false);

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        photo = binding.findViewById(R.id.photo);

        TextView name = binding.findViewById(R.id.name);
        TextView login = binding.findViewById(R.id.text_login);
        TextView sex = binding.findViewById(R.id.sex);
        TextView birthday = binding.findViewById(R.id.birthday);
        TextView time = binding.findViewById(R.id.time);
        TextView adress = binding.findViewById(R.id.adress);
        TextView create = binding.findViewById(R.id.create);
        TextView about = binding.findViewById(R.id.about);
        TextView height = binding.findViewById(R.id.height);
        TextView weight = binding.findViewById(R.id.weight);
        TextView phone = binding.findViewById(R.id.phone);
        TextView email = binding.findViewById(R.id.email);
        TextView exit = binding.findViewById(R.id.exit_action);

        profileList = binding.findViewById(R.id.profile_list);

        profileFilling = binding.findViewById(R.id.profileFilling);

        clubs = binding.findViewById(R.id.clubs_action);
        event = binding.findViewById(R.id.event_action);
        result = binding.findViewById(R.id.result_action);
        video = binding.findViewById(R.id.video_action);
        journal = binding.findViewById(R.id.journal_action);
        plan = binding.findViewById(R.id.plan_action);

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            try {
                name.setText(user.getFirstName() + " " + user.getLastName());
                login.setText(user.getNickname());
                if (user.getSex() != null) {
                    sex.setText(user.getSex().equals("male") ? "Чоловік" : user.getSex() == "female" ? "Жінка" : "Невідомо");
                }

                if (viewModel.isProfile(user.getRoleId())){
                    binding.findViewById(R.id.web_title_action).setVisibility(View.GONE);
                    binding.findViewById(R.id.web_action).setVisibility(View.GONE);
                }

                adress.setText(user.getCity());
                time.setText(user.getUpdatedAt().replace("-", "."));
                birthday.setText(user.getBirthday().replace("-", "."));
                create.setText(user.getCreatedAt().replace("-", "."));
                about.setText(user.getAboutMe());

                if (user.getHeight() != null){
                    binding.findViewById(R.id.icon_weight).setVisibility(View.VISIBLE);
                    binding.findViewById(R.id.title_weight).setVisibility(View.VISIBLE);
                    height.setVisibility(View.VISIBLE);
                    height.setText(user.getHeight() + " cм");
                }

                if (user.getWeight() != null){
                    binding.findViewById(R.id.icon_height).setVisibility(View.VISIBLE);
                    binding.findViewById(R.id.title_height).setVisibility(View.VISIBLE);
                    weight.setVisibility(View.VISIBLE);
                    weight.setText(user.getWeight() + " кг");
                }

                phone.setText(user.getPhone());
                email.setText(user.getEmail());
                profileFilling.setProgress(user.getProfileFilling());
                Glide.with(this).load(user.getPhoto()).into(photo);
            } catch (Exception e) {
            }
        });

        exit.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            viewModel.logout();
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container_user, new AuthFragment())
                                    .commit();
                            dialog.cancel();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.cancel();
                            break;
                    }
                }
            };
            builder.setMessage("Ви дійсно бажаєте вийти?").setPositiveButton("Так", dialogClickListener)
                    .setNegativeButton("Ні", dialogClickListener).show();
        });

        clubs.setOnClickListener(v -> {
            viewModel.clubs();
            replaceButton();
            clubs.on();
        });

        event.setOnClickListener(v -> {
            viewModel.event();
            replaceButton();
            event.on();
        });

        result.setOnClickListener(v -> {
            viewModel.result();
            replaceButton();
            result.on();
        });

        video.setOnClickListener(v -> {
            viewModel.userVideoList();
            replaceButton();
            video.on();
        });

        journal.setOnClickListener(v -> {
            viewModel.journal();
            replaceButton();
            journal.on();
        });

        plan.setOnClickListener(v -> {
            viewModel.plan();
            replaceButton();
            plan.on();
        });

        binding.findViewById(R.id.setting_action).setOnClickListener(v -> {
            dialogShow();
        });

        binding.findViewById(R.id.phone).setOnClickListener(v -> {
            ClipData clip = ClipData.newPlainText("", phone.getText().toString());
            clipboard.setPrimaryClip(clip);
            ((FragmentInteface) getActivity()).message("Скопійовано");
        });

        binding.findViewById(R.id.email).setOnClickListener(v -> {
            ClipData clip = ClipData.newPlainText("", email.getText().toString());
            clipboard.setPrimaryClip(clip);
            ((FragmentInteface) getActivity()).message("Скопійовано");
        });


        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://web.sportforall.gov.ua")));
            }
        };

        TextView webAction = binding.findViewById(R.id.web_action);
        int indexText = webAction.getText().toString().indexOf("веб");
        SpannableString spannableString = new SpannableString("Адміністрування доступне у веб-версії Активних парків");
        spannableString.setSpan(clickableSpan, indexText, indexText + 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        webAction.setText(spannableString);
        webAction.setMovementMethod(LinkMovementMethod.getInstance());

        observeData();

        viewModel.user();
        viewModel.clubs();

        return binding;
    }


    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void observeData() {
        viewModel.getClubs().observe(getViewLifecycleOwner(), clubs -> {
            if (clubs.size() > 0) {
                binding.findViewById(R.id.list_status).setVisibility(View.GONE);
            }
            profileList.setAdapter(new ClubsAdaper(getContext(), clubs).setOnClubsListener(new ClubsAdaper.ClubsListener() {
                @Override
                public void onInfo(ItemClub itemClub) {
                    startActivity(new Intent(getContext(), ClubActivity.class)
                            .putExtra("id", itemClub.getId()));
                }
            }));
        });

        viewModel.getEvents().observe(getViewLifecycleOwner(), result -> {
            if (result.size() > 0) {
                binding.findViewById(R.id.list_status).setVisibility(View.GONE);
            }
            profileList.setAdapter(new EventsListAdaper(getContext(), result).setOnEventListener(new EventsListAdaper.EventsListener() {
                @Override
                public void onInfo(ItemEvent itemClub) {
                    startActivity(new Intent(getContext(), EventActivity.class).putExtra("id", itemClub.getId()));
                }

                @Override
                public void onOpenMaps(double lat, double lon) {

                }
            }));
        });

        viewModel.getResult().observe(getViewLifecycleOwner(), result -> {
            if (result.getItems().size() > 0) {
                binding.findViewById(R.id.list_status).setVisibility(View.GONE);
            }
            profileList.setAdapter(new EventsListAdaper(getContext(), result.getItems()).setOnEventListener(new EventsListAdaper.EventsListener() {
                @Override
                public void onInfo(ItemEvent itemClub) {
                    startActivity(new Intent(getContext(), EventActivity.class).putExtra("id", itemClub.getId()));
                }

                @Override
                public void onOpenMaps(double lat, double lon) {

                }
            }));
        });

        viewModel.getUserVideo().observe(getViewLifecycleOwner(), video -> {
            if (video.getItems().size() > 0) {
                binding.findViewById(R.id.list_status).setVisibility(View.GONE);
            }
            profileList.setAdapter(new UserVideoAdapter(getActivity(), video.getItems()).setOnUserVideoListener(new UserVideoAdapter.UserVideoListener() {
                @Override
                public void onInfo(UserVideoItem itemClub) {
                    startActivity(new Intent(getActivity(), UserAddVideoActivity.class).putExtra("id", itemClub.getId()));
                }

                @Override
                public void onCreate() {
                    startActivity(new Intent(getActivity(), UserAddVideoActivity.class));
                }
            }));
        });

        viewModel.getJournal().observe(getViewLifecycleOwner(), journal -> {
            if (journal.size() > 0) {
                binding.findViewById(R.id.list_status).setVisibility(View.GONE);
            }
            JournalListAdaper adaper = new JournalListAdaper(getActivity(), journal);
            profileList.setAdapter(adaper.setListener(new JournalListAdaper.JournalListener() {
                @Override
                public void onInfo(WorkoutItem workoutItem) {

                }

                @Override
                public void getNotes(String id, int position) {
                    viewModel.notes(id);
                    viewModel.getNotes().observe(getViewLifecycleOwner(), notes -> {
                        adaper.updateWorkout(notes, position);
                    });
                }

                @Override
                public void sendMessage(String id, String idNotes, String msg, boolean edit) {
                    if (edit == true){
                        viewModel.cangeNotes(id, idNotes, msg);
                    }else {
                        viewModel.sendNotes(id, msg);
                    }
                }
            }));
        });

        viewModel.getPlan().observe(getViewLifecycleOwner(), plan -> {
            if (plan.size() > 0) {
                binding.findViewById(R.id.list_status).setVisibility(View.GONE);
            }
            profileList.setAdapter(new PlanListAdaper(getActivity(), plan).setListener(new PlanListAdaper.PlanListener() {
                @Override
                public void onInfo(String id) {
                    TrainingDialog dialog = TrainingDialog.newInstance(id);
                    dialog.show(getActivity().getSupportFragmentManager(),
                            "training_dialog");
                }
            }));
        });
    }

    void replaceButton() {
        clubs.off();
        event.off();
        result.off();
        video.off();
        journal.off();
        plan.off();
        profileList.removeAllViewsInLayout();
        binding.findViewById(R.id.list_status).setVisibility(View.VISIBLE);
    }

    void dialogShow() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.CustomBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.dialog_select_setting);

        LinearLayout editAction = bottomSheetDialog.findViewById(R.id.edit_action);
        editAction.setOnClickListener(view -> {
            //startActivity(new Intent(getActivity(), EditProfileActivity.class));
            EditProfileActivity dialog = EditProfileActivity.newInstance();
            dialog.show(getActivity().getSupportFragmentManager(),
                    "edit_profile");
            bottomSheetDialog.dismiss();
        });
        LinearLayout settingAction = bottomSheetDialog.findViewById(R.id.setting_action);
        settingAction.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Кеш очищено", Toast.LENGTH_LONG).show();
            bottomSheetDialog.dismiss();
        });

        LinearLayout supportlistAtion = bottomSheetDialog.findViewById(R.id.supportlist_action);
        supportlistAtion.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), SupportActivity.class));
            bottomSheetDialog.dismiss();
        });

        LinearLayout infolistAction = bottomSheetDialog.findViewById(R.id.infolist_action);
        infolistAction.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://web.sportforall.gov.ua/infolist/start")));
            bottomSheetDialog.dismiss();
        });

        LinearLayout removeUserAction = bottomSheetDialog.findViewById(R.id.remove_user_action);
        removeUserAction.setOnClickListener(view -> {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            viewModel.removeUser();
                            dialog.cancel();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.cancel();
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Ви дійсно бажаєте видалити свій обліковий запис?").setPositiveButton("Так", dialogClickListener)
                    .setNegativeButton("Ні", dialogClickListener).show();
        });

        LinearLayout cancel = bottomSheetDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }
}