package com.nafisfuad.firebasemad16.repos;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nafisfuad.firebasemad16.viewmodels.LoginViewModel;

public class FirebaseLoginRepository {
    private static final String TAG = FirebaseLoginRepository.class.getSimpleName();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private MutableLiveData<LoginViewModel.AuthenticationState> stateLiveData;
    private MutableLiveData<String> errMsg = new MutableLiveData<>();

    public FirebaseLoginRepository(MutableLiveData<LoginViewModel.AuthenticationState> stateLiveData) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        this.stateLiveData = stateLiveData;


    }

    public MutableLiveData<LoginViewModel.AuthenticationState> loginFirebaseUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseUser = firebaseAuth.getCurrentUser();
                    stateLiveData.postValue(LoginViewModel.AuthenticationState.AUTHENTICATED);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                stateLiveData.postValue(LoginViewModel.AuthenticationState.UNAUTHENTICATED);
                Log.e(TAG, e.getLocalizedMessage());
                errMsg.postValue(e.getLocalizedMessage());
            }
        });

        return stateLiveData;
    }

    public MutableLiveData<LoginViewModel.AuthenticationState> registerFirebaseUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseUser = firebaseAuth.getCurrentUser();
                    stateLiveData.postValue(LoginViewModel.AuthenticationState.AUTHENTICATED);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                if (e instanceof FirebaseAuthWeakPasswordException)
                stateLiveData.postValue(LoginViewModel.AuthenticationState.UNAUTHENTICATED);
                Log.e(TAG, e.getLocalizedMessage());
                errMsg.postValue(e.getLocalizedMessage());
            }
        });

        return stateLiveData;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public MutableLiveData<String> getErrMsg() {
        return errMsg;
    }
}
