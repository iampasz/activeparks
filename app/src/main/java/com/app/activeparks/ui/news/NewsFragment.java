package com.app.activeparks.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.ui.home.HomeFragment;
import com.app.activeparks.ui.home.adapter.HorizontalAdaper;
import com.app.activeparks.util.FragmentInteface;
import com.technodreams.activeparks.databinding.FragmentNewsBinding;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    public NewsFragment(){}

    public NewsFragment(String id){
        this.id = id;
    }

    private String id = null;
    private FragmentNewsBinding binding;
    private NewsViewModel mViewModel;
    private HorizontalAdaper adapterNews;;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this, new NewsModelFactory(getContext())).get(NewsViewModel.class);

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final RecyclerView listNews = binding.listNews;

        if (id != null){
            binding.panelTop.setVisibility(View.GONE);
            binding.titleNews.setVisibility(View.VISIBLE);
            mViewModel.getNewsId(id);
        }else {
            mViewModel.getNews();
            binding.panelTop.setVisibility(View.VISIBLE);
            binding.titleNews.setVisibility(View.GONE);
        }

        mViewModel.getNewsList().observe(getViewLifecycleOwner(), news -> {
            if (news.getItems().size() > 0) {
                binding.listNull.setVisibility(View.GONE);
            }

            listNews.setAdapter(new HorizontalAdaper(getActivity(), news).setOnCliclListener(itemNews -> {
                if (id != null){
                    startActivity(new Intent(getActivity(), NewsActivity.class)
                            .putExtra("id", itemNews.getId())
                            .putExtra("clubId", id));
                }else {
                    startActivity(new Intent(getActivity(), NewsActivity.class)
                            .putExtra("id", itemNews.getId()));
                }
            }));
        });


        binding.closed.setOnClickListener(v ->{
            ((FragmentInteface) getActivity()).show(new HomeFragment());
        });

        binding.actionCreate.setOnClickListener(v ->{
            startActivity(new Intent(getActivity(), NewsCreateActivity.class).putExtra("id", id));
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}