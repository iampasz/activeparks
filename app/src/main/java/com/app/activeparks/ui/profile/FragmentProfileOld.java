package com.app.activeparks.ui.profile;

import android.annotation.SuppressLint;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.uservideo.UserVideoItem;
import com.app.activeparks.data.model.workout.WorkoutItem;
import com.app.activeparks.ui.auth.AuthFragment;
import com.app.activeparks.ui.clubs.ClubActivity;
import com.app.activeparks.ui.clubs.adapter.ClubsAdaper;
import com.app.activeparks.ui.event.activity.EventActivity;
import com.app.activeparks.ui.event.adapter.EventsListAdaper;
import com.app.activeparks.ui.profile.uservideo.UserAddVideoActivity;
import com.app.activeparks.ui.profile.uservideo.adapter.UserVideoAdapter;
import com.app.activeparks.ui.support.SupportActivity;
import com.app.activeparks.ui.training.TrainingDialog;
import com.app.activeparks.ui.workout.adapter.journal.JournalListAdaper;
import com.app.activeparks.ui.workout.adapter.plan.PlanListAdaper;
import com.app.activeparks.util.ButtonSelect;
import com.app.activeparks.util.FragmentInteface;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.technodreams.activeparks.BuildConfig;
import com.technodreams.activeparks.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FragmentProfileOld extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ProfileViewModelOld viewModel;
    private View view;
    private ImageView photo;
    private RecyclerView profileList;
    private ProgressBar profileFilling;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView statusView;
    private ButtonSelect clubs, event, result, video, journal, plan;

    public static FragmentProfileOld newInstance() {
        return new FragmentProfileOld();
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this, new ProfileModelFactory(getContext())).get(ProfileViewModelOld.class);

        view = inflater.inflate(R.layout.fragment_profile_old, container, false);

        ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        photo = view.findViewById(R.id.photo);

        statusView = view.findViewById(R.id.list_status);

        TextView name = view.findViewById(R.id.name);
        TextView role = view.findViewById(R.id.role);
        TextView sex = view.findViewById(R.id.sex);
        TextView birthday = view.findViewById(R.id.birthday);
        TextView address = view.findViewById(R.id.address);
        TextView about = view.findViewById(R.id.about);
        TextView healt = view.findViewById(R.id.healt);
        TextView height = view.findViewById(R.id.height);
        TextView weight = view.findViewById(R.id.weight);
        TextView phone = view.findViewById(R.id.phone);
        TextView email = view.findViewById(R.id.email);
        TextView exit = view.findViewById(R.id.exit_action);

        profileList = view.findViewById(R.id.profile_list);

        profileFilling = view.findViewById(R.id.profileFilling);

        clubs = view.findViewById(R.id.clubs_action);
        event = view.findViewById(R.id.event_action);
        result = view.findViewById(R.id.result_action);
        video = view.findViewById(R.id.video_action);
        journal = view.findViewById(R.id.journal_action);
        plan = view.findViewById(R.id.plan_action);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {

            if (user.getFirstName().length() + user.getLastName().length() > 1) {
                name.setVisibility(View.VISIBLE);
                name.setText(user.getFirstName() + " " + user.getLastName());
            } else {
                name.setText(user.getNickname());
            }

            Glide.with(this).load(user.getPhoto()).error(R.drawable.ic_prew).into(photo);

            role.setText(viewModel.isRole(user.getRoleId()));

            if (user.getSex() != null) {
                view.findViewById(R.id.layout_sex).setVisibility(View.VISIBLE);
                sex.setText(user.getSex().equals("male") ? "Чоловік" : "Жінка");
            }

            if (viewModel.isProfile(user.getRoleId())) {
                view.findViewById(R.id.web_title_action).setVisibility(View.VISIBLE);
                view.findViewById(R.id.web_action).setVisibility(View.VISIBLE);
            }

            if (user.getBirthday() != null && user.getBirthday().length() > 0) {
                view.findViewById(R.id.layout_birthday).setVisibility(View.VISIBLE);
                try {
                    @SuppressLint("SimpleDateFormat") Date date = new SimpleDateFormat("yyyy-MM-dd").parse(user.getBirthday());
                    assert date != null;
                    birthday.setText(new SimpleDateFormat("dd MMMM yyyy", new Locale("uk", "UA")).format(date));
                } catch (ParseException e) {
                    birthday.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            if (user.getCity().length() > 1) {
                view.findViewById(R.id.layout_location).setVisibility(View.VISIBLE);
                address.setText(user.getCity());
            }


            if (user.getAboutMe().length() > 1) {
                view.findViewById(R.id.title_about).setVisibility(View.VISIBLE);
                about.setVisibility(View.VISIBLE);
                about.setText(user.getAboutMe());
            }

            if (user.getHealthState().length() > 1) {
                view.findViewById(R.id.title_healt).setVisibility(View.VISIBLE);
                healt.setVisibility(View.VISIBLE);
                healt.setText(user.getHealthState());
            }

            if (user.getHeight() != null && user.getHeight().length() > 1) {
                view.findViewById(R.id.item_height).setVisibility(View.VISIBLE);
                height.setVisibility(View.VISIBLE);
                height.setText(user.getHeight() + " cм");
            }

            if (user.getWeight() != null && user.getWeight().length() > 1) {
                view.findViewById(R.id.item_weight).setVisibility(View.VISIBLE);
                weight.setVisibility(View.VISIBLE);
                weight.setText(user.getWeight() + " кг");
            }

            if (user.getPhone().length() > 1) {
                view.findViewById(R.id.phone_item).setVisibility(View.VISIBLE);
                phone.setText(user.getPhone());
            }

            email.setText(user.getEmail());
            profileFilling.setProgress(user.getProfileFilling());


        });

        exit.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE -> {
                            viewModel.logout();
                            requireActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container_user, new AuthFragment())
                                    .commit();
                            dialog.cancel();
                        }
                        case DialogInterface.BUTTON_NEGATIVE -> dialog.cancel();
                    }
                };

            builder.setMessage("Ви дійсно бажаєте вийти?").

                setPositiveButton("Так",dialogClickListener)
                    .

                setNegativeButton("Ні",dialogClickListener).

                show();
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

            view.findViewById(R.id.setting_action).setOnClickListener(v -> dialogShow());

            view.findViewById(R.id.phone).setOnClickListener(v -> {
                ClipData clip = ClipData.newPlainText("", phone.getText().toString());
                clipboard.setPrimaryClip(clip);
                ((FragmentInteface) requireActivity()).message("Скопійовано");
            });

            view.findViewById(R.id.email).setOnClickListener(v -> {
                ClipData clip = ClipData.newPlainText("", email.getText().toString());
                clipboard.setPrimaryClip(clip);
                ((FragmentInteface) requireActivity()).message("Скопійовано");
            });


            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://ap.sportforall.gov.ua")));
                }
            };

            TextView webAction = view.findViewById(R.id.web_action);
            int indexText = webAction.getText().toString().indexOf("веб");
            SpannableString spannableString = new SpannableString("Для адміністрування зокрема і таких, як створення заходів та клубів потрібно перейти на веб-версію Активних парків");
            spannableString.setSpan(clickableSpan, indexText, indexText + 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            webAction.setText(spannableString);
            webAction.setMovementMethod(LinkMovementMethod.getInstance());

            observeData();

            viewModel.user();
            viewModel.clubs();
            viewModel.select = 0;

            return view;
        }


        @Override
        public void onResume () {
            super.onResume();
            statusView.setVisibility(View.VISIBLE);
            switch (viewModel.select) {
                case 0:
                    viewModel.clubs();
                    break;
                case 1:
                    viewModel.event();
                    break;
                case 2:
                    viewModel.journal();
                    break;
                case 3:
                    viewModel.userVideoList();
                    break;
            }
        }


        @Override
        public void onViewCreated (@NonNull View view, @NonNull Bundle savedInstanceState){
            super.onViewCreated(view, savedInstanceState);
        }

        private void observeData () {
            viewModel.getClubs().observe(getViewLifecycleOwner(), clubs -> {
                if (clubs.size() > 0) {
                    statusView.setVisibility(View.GONE);
                } else {
                    statusView.setText("Ви ще не вступили \n в жоден клуб");
                }
                profileList.setAdapter(new ClubsAdaper(getContext(), clubs).setOnClubsListener(itemClub -> startActivity(new Intent(getContext(), ClubActivity.class)
                        .putExtra("id", itemClub.getId()))));
            });

            viewModel.getEvents().observe(getViewLifecycleOwner(), result -> {
                if (result.size() > 0) {
                    statusView.setVisibility(View.GONE);
                } else {
                    statusView.setText("Ви ще не вступили \n в жоден захід");
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
                    statusView.setVisibility(View.GONE);
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
                    statusView.setVisibility(View.GONE);
                } else {
                    statusView.setText("Не додали жодного відео");
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
                    statusView.setVisibility(View.GONE);
                } else {
                    statusView.setText("Немає жодних активностей");
                }
                JournalListAdaper adaper = new JournalListAdaper(getActivity(), journal);
                profileList.setAdapter(adaper.setListener(new JournalListAdaper.JournalListener() {
                    @Override
                    public void onInfo(WorkoutItem workoutItem) {
                        startActivity(new Intent(getContext(), EventActivity.class).putExtra("id", workoutItem.getId()));
                    }

                    @Override
                    public void getNotes(String id, int position) {
                        viewModel.notes(id);
                        viewModel.getNotes().observe(getViewLifecycleOwner(), notes -> adaper.updateWorkout(notes, position));
                    }

                    @Override
                    public void sendMessage(String id, String idNotes, String msg, boolean edit) {
                        if (edit && idNotes != null) {
                            viewModel.cangeNotes(id, idNotes, msg);
                        } else {
                            viewModel.sendNotes(id, msg);
                        }
                    }
                }));
            });

            viewModel.getPlan().observe(getViewLifecycleOwner(), plan -> {
                if (plan != null && plan.size() > 0) {
                    statusView.setVisibility(View.GONE);
                } else {
                    statusView.setText("Немая жодних активностей");
                }
                profileList.setAdapter(new PlanListAdaper(getActivity(), plan).setListener(id -> {
                    TrainingDialog dialog = TrainingDialog.newInstance(id, viewModel.mProfile.getId());
                    dialog.show(requireActivity().getSupportFragmentManager(),
                            "training_dialog");
                }));
            });
        }

        void replaceButton () {
            clubs.off();
            event.off();
            result.off();
            video.off();
            journal.off();
            plan.off();
            profileList.removeAllViewsInLayout();
            statusView.setText("Завантаження...");
            statusView.setVisibility(View.VISIBLE);
        }

        void dialogShow () {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme);
            bottomSheetDialog.setContentView(R.layout.dialog_select_setting);

            LinearLayout editAction = bottomSheetDialog.findViewById(R.id.edit_action);
            assert editAction != null;
            editAction.setOnClickListener(view -> {
                //startActivity(new Intent(getActivity(), EditProfileActivity.class));
                EditProfileActivity dialog = EditProfileActivity.newInstance();
                dialog.setOnEditProfile(() -> viewModel.user()).show(requireActivity().getSupportFragmentManager(),
                        "edit_profile");
                bottomSheetDialog.dismiss();
            });
            LinearLayout settingAction = bottomSheetDialog.findViewById(R.id.setting_action);
            assert settingAction != null;
            settingAction.setOnClickListener(view -> {
                dialogSetting();
                bottomSheetDialog.dismiss();
            });

            LinearLayout supportlistAtion = bottomSheetDialog.findViewById(R.id.supportlist_action);
            assert supportlistAtion != null;
            supportlistAtion.setOnClickListener(view -> {
                startActivity(new Intent(getActivity(), SupportActivity.class));
                bottomSheetDialog.dismiss();
            });

            LinearLayout infolistAction = bottomSheetDialog.findViewById(R.id.infolist_action);
            assert infolistAction != null;
            infolistAction.setOnClickListener(view -> {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://ap.sportforall.gov.ua/infolist/start")));
                bottomSheetDialog.dismiss();
            });

            LinearLayout removeUserAction = bottomSheetDialog.findViewById(R.id.remove_user_action);
            assert removeUserAction != null;
            removeUserAction.setOnClickListener(view -> {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE -> {
                            viewModel.removeUser();
                            dialog.cancel();
                        }
                        case DialogInterface.BUTTON_NEGATIVE -> dialog.cancel();
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Ви дійсно бажаєте видалити свій обліковий запис?").setPositiveButton("Так", dialogClickListener)
                        .setNegativeButton("Ні", dialogClickListener).show();
            });

            LinearLayout cancel = bottomSheetDialog.findViewById(R.id.cancel);
            assert cancel != null;
            cancel.setOnClickListener(view -> bottomSheetDialog.dismiss());
            bottomSheetDialog.show();
        }

        @SuppressLint("SetTextI18n")
        void dialogSetting () {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme);
            bottomSheetDialog.setContentView(R.layout.dialog_bottom_setting);

            @SuppressLint("UseSwitchCompatOrMaterialCode") Switch server = bottomSheetDialog.findViewById(R.id.server);
            assert server != null;
            server.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Для активації функції потрібно заново авторизуватися", Toast.LENGTH_LONG).show();
                viewModel.setServer(!viewModel.getServer());
            });

            server.setChecked(viewModel.getServer());

            LinearLayout cacheAction = bottomSheetDialog.findViewById(R.id.cache_action);
            assert cacheAction != null;
            cacheAction.setOnClickListener(view -> {
                Toast.makeText(getContext(), "Кеш очищено", Toast.LENGTH_LONG).show();
                bottomSheetDialog.dismiss();
            });

            LinearLayout googleplayAction = bottomSheetDialog.findViewById(R.id.google_play_action);
            assert googleplayAction != null;
            googleplayAction.setOnClickListener(view -> {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.technodreams.activeparks")));
                bottomSheetDialog.dismiss();
            });

            TextView version = bottomSheetDialog.findViewById(R.id.version);

            assert version != null;
            version.setText("Вироблено під замовлення ВЦФЗН «Спорт для всіх» \n made by «TechnoDreams Group» \n\n Версія: " + BuildConfig.VERSION_NAME);

            LinearLayout cancel = bottomSheetDialog.findViewById(R.id.cancel);
            assert cancel != null;
            cancel.setOnClickListener(view -> bottomSheetDialog.dismiss());
            bottomSheetDialog.show();
        }

        @Override
        public void onRefresh () {
            swipeRefreshLayout.setRefreshing(false);
            viewModel.user();
            switch (viewModel.select) {
                case 0 -> viewModel.clubs();
                case 1 -> viewModel.event();
                case 2 -> viewModel.journal();
                case 3 -> viewModel.userVideoList();
            }
        }
    }