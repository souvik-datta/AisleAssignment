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

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.souvik.myapplication.databinding.FragmentOtpVerificationBinding;
import com.souvik.myapplication.network.ApiClient;
import com.souvik.myapplication.utils.Utility;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerificationFragment extends Fragment {

    private FragmentOtpVerificationBinding binding;
    private NavController navController;
    private String TAG = OtpVerificationFragment.class.getSimpleName();
    private MainViewModel viewModel;
    private String phoneNumber;
    private CountDownTimer timer;
    private int timerCount = 60;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            phoneNumber = getArguments().getString("phoneNumber");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otp_verification, container, false);
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
        binding.tvPhoneNumber.setText(phoneNumber.substring(0, 3) + " " + phoneNumber.substring(3));
        startTimer();
        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isNetworkConnected(requireContext())) {
                    if (isValidInput()) {
                        viewModel.getOTPData(phoneNumber, binding.etOtp.getText().toString().trim()).observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                Utility.hideKeyboardFrom(requireContext(), binding.getRoot());
                                if (s != null) {
                                    try {
                                        stopTimer();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("token", s);
                                        NavHostFragment.findNavController(OtpVerificationFragment.this)
                                                .navigate(R.id.action_otpVerificationFragment_to_dashboardFragment, bundle);
                                    } catch (Exception e) {
                                        Log.d(TAG, "onChanged: " + e.getMessage());
                                    }
                                } else {
                                    Snackbar.make(binding.getRoot(), "OTP does not match!", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
                    }else{
                        Utility.hideKeyboardFrom(requireContext(), binding.getRoot());
                        Snackbar.make(binding.getRoot(), "OTP does not match!", Snackbar.LENGTH_LONG).show();
                    }
                }else{
                    Utility.hideKeyboardFrom(requireContext(), binding.getRoot());
                    Snackbar.make(binding.getRoot(), "No Internet!", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startTimer() {
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                --timerCount;
                binding.tvTimer.setText("00:" + (timerCount));
            }

            @Override
            public void onFinish() {
                binding.tvTimer.setText("00:00");
            }
        };
        timer.start();
    }

    private void stopTimer() {
        timer.cancel();
        binding.tvTimer.setText("00:00");
    }

    private boolean isValidInput() {
        if (binding.etOtp.getText().toString().length() < 4) {
            binding.etOtp.setError("Please enter the extension in proper format!");
            return false;
        }
        return true;
    }
}