package com.example.ek;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> userEmail = new MutableLiveData<>();
    private final MutableLiveData<String> userFullName = new MutableLiveData<>();
    private final MutableLiveData<String> userRole = new MutableLiveData<>();
    private final MutableLiveData<Integer> listingID = new MutableLiveData<>();
    private final MutableLiveData<String> agentCell = new MutableLiveData<>();

    private final MutableLiveData<Double> purchasePrice = new MutableLiveData<>();
    private final MutableLiveData<Double> deposit = new MutableLiveData<>();
    private final MutableLiveData<Double> interestRate = new MutableLiveData<>();
    private final MutableLiveData<Integer> loanTerm = new MutableLiveData<>();
    private final MutableLiveData<Double> monthlyRepayment = new MutableLiveData<>();
    private final MutableLiveData<Double> totalOnceOffCost = new MutableLiveData<>();
    private final MutableLiveData<Double> bondRegistrationCost = new MutableLiveData<>();
    private final MutableLiveData<Double> propertyTransferCost = new MutableLiveData<>();
    private final MutableLiveData<Double> grossMonthlyIncome = new MutableLiveData<>();

    public void setUserFullName(String fullName) {
        userFullName.setValue(fullName);
    }

    public LiveData<String> getUserFullName() {
        return userFullName;
    }

    public void setProfileEmail(String email) {
        userEmail.setValue(email);
    }

    public LiveData<String> getProfileEmail() {
        return userEmail;
    }

    public void setUserRole(String role) {
        userRole.setValue(role);
    }

    public LiveData<String> getUserRole() {
        return userRole;
    }

    public LiveData<String> getAgentCell() {
        return agentCell;
    }

    public void setAgentCell(String cell) {
        agentCell.setValue(cell);
    }

    public void setListingID(int id) { listingID.setValue(id); }
    public LiveData<Integer> getListingID() { return listingID; }

    public void setPurchasePrice(double price) { purchasePrice.setValue(price); }
    public LiveData<Double> getPurchasePrice() { return purchasePrice; }

    public void setDeposit(double dep) { deposit.setValue(dep); }
    public LiveData<Double> getDeposit() { return deposit; }

    public void setInterestRate(double rate) { interestRate.setValue(rate); }
    public LiveData<Double> getInterestRate() { return interestRate; }

    public void setLoanTerm(int term) { loanTerm.setValue(term); }
    public LiveData<Integer> getLoanTerm() { return loanTerm; }

    public void setMonthlyRepayment(double repayment) { monthlyRepayment.setValue(repayment); }
    public LiveData<Double> getMonthlyRepayment() { return monthlyRepayment; }

    public void setTotalOnceOffCost(double cost) { totalOnceOffCost.setValue(cost); }
    public LiveData<Double> getTotalOnceOffCost() { return totalOnceOffCost; }

    public void setBondRegistrationCost(double cost) { bondRegistrationCost.setValue(cost); }
    public LiveData<Double> getBondRegistrationCost() { return bondRegistrationCost; }

    public void setPropertyTransferCost(double cost) { propertyTransferCost.setValue(cost); }
    public LiveData<Double> getPropertyTransferCost() { return propertyTransferCost; }

    public void setGrossMonthlyIncome(double income) { grossMonthlyIncome.setValue(income); }
    public LiveData<Double> getGrossMonthlyIncome() { return grossMonthlyIncome; }
}
