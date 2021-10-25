package com.nafisfuad.firebasemad16.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nafisfuad.firebasemad16.repos.FirebaseLoginRepository;

public class LoginViewModel extends ViewModel {
    public enum AuthenticationState {
        AUTHENTICATED,
        UNAUTHENTICATED
    }
    private FirebaseLoginRepository repository;
    public MutableLiveData<AuthenticationState> stateLiveData;

    public MutableLiveData<String> errMsg = new MutableLiveData<>();
    public LoginViewModel() {
        stateLiveData = new MutableLiveData<>();
        repository = new FirebaseLoginRepository(stateLiveData);
        errMsg = repository.getErrMsg();
        if (repository.getFirebaseUser() != null) {
            stateLiveData.postValue(AuthenticationState.AUTHENTICATED);
        } else {
            stateLiveData.postValue(AuthenticationState.UNAUTHENTICATED);
        }
    }

    public void login(String email, String password) {
//        repository.loginFirebaseUser(email, password);
        stateLiveData = repository.loginFirebaseUser(email, password);
    }

    public void register(String email, String password) {
//        repository.registerFirebaseUser(email, password);
        stateLiveData = repository.registerFirebaseUser(email, password);
    }
}
