package com.nafisfuad.firebasemad16.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nafisfuad.firebasemad16.nearby.NearbyResponseBody;
import com.nafisfuad.firebasemad16.repos.NearbyRepository;

public class NearbyViewModel extends ViewModel {
    private NearbyRepository repository;
     public NearbyViewModel() {
         repository = new NearbyRepository();
     }

     public MutableLiveData<NearbyResponseBody> getResponseFromRepo(String endUrl) {
         return repository.getResponse(endUrl);
     }
}
