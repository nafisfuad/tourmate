package com.nafisfuad.firebasemad16.repos;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nafisfuad.firebasemad16.pojos.EventExpense;
import com.nafisfuad.firebasemad16.pojos.Moments;
import com.nafisfuad.firebasemad16.pojos.TourmateEvent;

import java.util.ArrayList;
import java.util.List;

public class EventDBRepository {
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference eventRef;
    private DatabaseReference momentsRef;
    private FirebaseUser firebaseUser;
    public MutableLiveData<List<TourmateEvent>> eventListLD = new MutableLiveData<>();
    public MutableLiveData<TourmateEvent> eventDetailsLD = new MutableLiveData<>();
    public MutableLiveData<List<Moments>> momentsLD = new MutableLiveData<>();

    public EventDBRepository() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(firebaseUser.getUid());
        momentsRef = userRef.child("Moments");
        eventRef = userRef.child("Events");
        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<TourmateEvent> events = new ArrayList<>();
                for (DataSnapshot d: dataSnapshot.getChildren()) {
                    events.add(d.getValue(TourmateEvent.class));
                }
                eventListLD.postValue(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addNewEventToDB(TourmateEvent event) {
        String eventID = eventRef.push().getKey();
        event.setEventID(eventID);
        eventRef.child(eventID)/*.push()*/.setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public MutableLiveData<TourmateEvent> getEventDetailsByEventId(String eventId) {
        eventRef.child(eventId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TourmateEvent event = dataSnapshot.getValue(TourmateEvent.class);
                eventDetailsLD.postValue(event);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return eventDetailsLD;
    }

    public void addNewMoment(Moments moments) {
        String momentId = momentsRef.push().getKey();
        moments.setMomentId(momentId);
        momentsRef.child(moments.getEventId()).child(momentId).setValue(moments);
    }

    public MutableLiveData<List<Moments>> getAllMoments(String eventId) {
        momentsRef.child(eventId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Moments> moments = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    moments.add(d.getValue(Moments.class));
                }
                momentsLD.postValue(moments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return momentsLD;
    }


}
