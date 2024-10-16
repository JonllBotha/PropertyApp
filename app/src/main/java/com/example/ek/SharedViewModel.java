package com.example.ek;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> userEmail = new MutableLiveData<>();
    private final MutableLiveData<String> userFullName = new MutableLiveData<>();

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
}
