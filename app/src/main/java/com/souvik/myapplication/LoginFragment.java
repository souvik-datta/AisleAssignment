package com.souvik.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.souvik.myapplication.databinding.FragmentLoginBinding;
import com.souvik.myapplication.network.ApiClient;
import com.souvik.myapplication.utils.Utility;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private NavController navController;
    private String TAG = LoginFragment.class.getSimpleName();
    private MainViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        navController = Navigation.findNavController(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isNetworkConnected(requireContext())) {
                    if (isValidInput()) {
                        binding.loadingLayout.setVisibility(View.VISIBLE);
                        String phoneNumber = binding.etExtension.getText().toString().trim() + binding.etPhoneNumber.getText().toString().trim();
                        viewModel.getLoginStatus(phoneNumber).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                binding.loadingLayout.setVisibility(View.GONE);
                                Utility.hideKeyboardFrom(requireContext(), binding.getRoot());
                                if (aBoolean) {
                                    try {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("phoneNumber", phoneNumber);
                                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_otpVerificationFragment, bundle);
                                    } catch (Exception e) {
                                        Log.d(TAG, "onChanged: " + e.getMessage());
                                    }
                                } else {
                                    Snackbar.make(binding.getRoot(), "Something went wrong!", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Utility.hideKeyboardFrom(requireContext(), binding.getRoot());
                        Snackbar.make(binding.getRoot(), "Make sure to enter data correctly", Snackbar.LENGTH_LONG).show();
                    }
                }else {
                    Utility.hideKeyboardFrom(requireContext(), binding.getRoot());
                    Snackbar.make(binding.getRoot(), "No Internet!", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isValidInput() {
        Log.d(TAG, "isValidInput: " + binding.etExtension.getText().toString().length());
        if (binding.etExtension.getText().toString().length() < 3) {
            binding.etExtension.setError("Please enter the extension in proper format!");
            return false;
        }
        if (binding.etPhoneNumber.getText().toString().length() < 10) {
            binding.etPhoneNumber.setError("Please enter the phone number in proper format!");
            return false;
        }
        return true;
    }
}