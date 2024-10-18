package com.example.ek;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ClientCalculationFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private TextView tvMonthlyRepay, tvTotOnceOff, tvDeposit, tvBondReg, tvPropTrf, tvMonthlyIncome;
    private FloatingActionButton btnBackToProfile;
    private Button btnRecalculate;

    public ClientCalculationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_calculation, container, false);

        // Initialize ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Initialize output fields
        tvMonthlyRepay = view.findViewById(R.id.tvMonthlyRepay);
        tvTotOnceOff = view.findViewById(R.id.tvTotOnceOff);
        tvDeposit = view.findViewById(R.id.tvDeposit);
        tvBondReg = view.findViewById(R.id.tvBondReg);
        tvPropTrf = view.findViewById(R.id.tvPropTrf);
        tvMonthlyIncome = view.findViewById(R.id.tvMonthlyIncome);

        btnBackToProfile = view.findViewById(R.id.backToProfile);
        btnRecalculate = view.findViewById(R.id.btnRecalculate);

        // Observe ViewModel and set values
        sharedViewModel.getMonthlyRepayment().observe(getViewLifecycleOwner(), value -> {
            tvMonthlyRepay.setText("R " + String.format("%.2f", value));
        });

        sharedViewModel.getTotalOnceOffCost().observe(getViewLifecycleOwner(), value -> {
            tvTotOnceOff.setText("R " + String.format("%.2f", value));
        });

        sharedViewModel.getDeposit().observe(getViewLifecycleOwner(), value -> {
            tvDeposit.setText("R " + String.format("%.2f", value));
        });

        sharedViewModel.getBondRegistrationCost().observe(getViewLifecycleOwner(), value -> {
            tvBondReg.setText("R " + String.format("%.2f", value));
        });

        sharedViewModel.getPropertyTransferCost().observe(getViewLifecycleOwner(), value -> {
            tvPropTrf.setText("R " + String.format("%.2f", value));
        });

        sharedViewModel.getGrossMonthlyIncome().observe(getViewLifecycleOwner(), value -> {
            tvMonthlyIncome.setText("R " + String.format("%.2f", value));
        });

        // Back button click listener
        btnBackToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                NavHostFragment.findNavController(ClientCalculationFragment.this)
                        .navigate(R.id.action_clientCalculationFragment_to_clientProfileFragment);
            }
        });

        // Back button click listener
        btnRecalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                NavHostFragment.findNavController(ClientCalculationFragment.this)
                        .navigate(R.id.action_clientCalculationFragment_to_clientCalculatorFragment);
            }
        });

        return view;
    }
}
