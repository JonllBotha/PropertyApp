package com.example.ek;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ClientCalculatorFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private TextView tvReset;
    private EditText etPurchasePrice, etDeposit, etInterestRate, etLoanTerm;
    private Button btnCalculate;
    private FloatingActionButton btnBackToProfile;
    private ImageView btnInfo;

    public ClientCalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_calculator, container, false);

        // Initialize ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Initialize input fields
        etPurchasePrice = view.findViewById(R.id.etPurchasePrice);
        etDeposit = view.findViewById(R.id.etDeposit);
        etInterestRate = view.findViewById(R.id.etInterestRate);
        etLoanTerm = view.findViewById(R.id.etLoanTerm);
        btnCalculate = view.findViewById(R.id.btnCalculate);

        tvReset = view.findViewById(R.id.tvReset);
        btnBackToProfile = view.findViewById(R.id.backToProfile);
        btnInfo = view.findViewById(R.id.btnInfo);

        // Set default values
        etPurchasePrice.setText("1000000"); // Reset Purchase Price to 1,000,000
        etDeposit.setText("0");             // Reset Deposit to 0
        etInterestRate.setText("11.5");     // Reset Interest Rate to 11.5%
        etLoanTerm.setText("20");           // Reset Loan Term to 20 years

        btnCalculate.setOnClickListener(v -> {
            try {
                double purchasePrice = Double.parseDouble(etPurchasePrice.getText().toString());
                double deposit = etDeposit.getText().toString().isEmpty() ? 0 : Double.parseDouble(etDeposit.getText().toString());
                double interestRate = Double.parseDouble(etInterestRate.getText().toString());
                int loanTerm = Integer.parseInt(etLoanTerm.getText().toString());

                // Input validation
                if (purchasePrice <= 0 || interestRate <= 0 || loanTerm <= 0) {
                    // Show error message for invalid inputs
                    Toast.makeText(getContext(), "Please enter valid, positive values.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Perform calculation as before
                double monthlyRepayment = calculateMonthlyRepayment(purchasePrice, deposit, interestRate, loanTerm);
                double bondRegistrationCost = calculateBondRegistrationCost(purchasePrice);
                double propertyTransferCost = calculatePropertyTransferCost(purchasePrice);
                double totalOnceOffCost = bondRegistrationCost + propertyTransferCost;
                double grossMonthlyIncome = monthlyRepayment * 3.333;  // Example multiplier

                // Store calculation results in ViewModel
                sharedViewModel.setMonthlyRepayment(monthlyRepayment);
                sharedViewModel.setTotalOnceOffCost(totalOnceOffCost);
                sharedViewModel.setBondRegistrationCost(bondRegistrationCost);
                sharedViewModel.setPropertyTransferCost(propertyTransferCost);
                sharedViewModel.setGrossMonthlyIncome(grossMonthlyIncome);

                // Navigate to the results fragment
                NavHostFragment.findNavController(ClientCalculatorFragment.this)
                        .navigate(R.id.action_clientCalculatorFragment_to_clientCalculationFragment);

            } catch (NumberFormatException e) {
                // Show error if the inputs are not valid numbers
                Toast.makeText(getContext(), "Please enter valid numbers.", Toast.LENGTH_LONG).show();
            }
        });

        // Back button click listener
        btnBackToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                NavHostFragment.findNavController(ClientCalculatorFragment.this)
                        .navigate(R.id.action_clientCalculatorFragment_to_clientProfileFragment);
            }
        });

        // Set the onClickListener for the Reset TextView
        tvReset.setOnClickListener(v -> {
            // Reset the values to the specified defaults
            etPurchasePrice.setText("1000000"); // Reset Purchase Price to 1,000,000
            etDeposit.setText("0");             // Reset Deposit to 0
            etInterestRate.setText("11.5");     // Reset Interest Rate to 11.5%
            etLoanTerm.setText("20");           // Reset Loan Term to 20 years
        });

        // Set the onClickListener for the ImageView
        btnInfo.setOnClickListener(v -> {
            // Create an AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Disclaimer");
            builder.setMessage("Please note that by default this calculator uses the prime interest " +
                    "rate for bond payment calculations. This is purely for convenience and not an " +
                    "indication of the interest rate that might be offered to you by a bank. /n This " +
                    "calculator is intended to provide estimates based on the indicated amounts, " +
                    "rates and fees. Whilst we make every effort to ensure the accuracy of these calculations, " +
                    "we cannot be held liable for inaccuracies. Horizon Home Finders does not accept " +
                    "liability for any damages arising from the use of this calculator.");
            builder.setPositiveButton("OK", (dialog, which) -> {
                // Dismiss the dialog when OK is clicked
                dialog.dismiss();
            });

            // Show the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        return view;
    }

    // Method to calculate monthly repayment
    private double calculateMonthlyRepayment(double price, double deposit, double rate, int term) {
        double loanAmount = price - deposit;
        double monthlyRate = rate / 12 / 100;
        int termInMonths = term * 12;
        return (loanAmount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -termInMonths));
    }

    // Example cost calculations
    private double calculateBondRegistrationCost(double price) { return price * 0.0375; }
    private double calculatePropertyTransferCost(double price) { return price * 0.0325; }
}


