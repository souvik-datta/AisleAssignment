package com.souvik.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.souvik.myapplication.adapter.LikesAdapter;
import com.souvik.myapplication.databinding.FragmentDashboardBinding;
import com.souvik.myapplication.model.Likes;
import com.souvik.myapplication.utils.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private String TAG = DashboardFragment.class.getSimpleName();
    private FragmentDashboardBinding binding;
    private NavController navController;
    private MainViewModel viewModel;
    private String token;
    private ArrayList<Likes> likeList = new ArrayList<>();
    private LikesAdapter likesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            token = getArguments().getString("token");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        if (Utility.isNetworkConnected(requireContext())) {
            viewModel.getProfileData(token).observe(getViewLifecycleOwner(), new Observer<JsonObject>() {
                @Override
                public void onChanged(JsonObject jsonObject) {
                    Log.d(TAG, "onChanged: " + jsonObject);
                    if (jsonObject.has("likes") && jsonObject.get("likes").getAsJsonObject().get("profiles").getAsJsonArray() != null) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<Likes>>() {
                        }.getType();
                        likeList.addAll(gson.fromJson(jsonObject.get("likes").getAsJsonObject().get("profiles").getAsJsonArray(), type));
                        likesAdapter.notifyDataSetChanged();
                    }
                }
            });
        } else {
            Snackbar.make(binding.getRoot(), "No Internet!", Snackbar.LENGTH_LONG).show();
        }
        likesAdapter = new LikesAdapter(likeList, new LikesAdapter.onClickListener() {
            @Override
            public void onClick(int position) {
                Log.d(TAG, "onClick: " + likeList.get(position));
            }
        });
        binding.rvLikes.setAdapter(likesAdapter);
    }
}