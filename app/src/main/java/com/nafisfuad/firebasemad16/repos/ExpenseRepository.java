package com.nafisfuad.firebasemad16.repos;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nafisfuad.firebasemad16.pojos.EventExpense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository {
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference expenseRef;
    private FirebaseUser firebaseUser;
    public MutableLiveData<List<EventExpense>> expenseListLD = new MutableLiveData<>();

    public ExpenseRepository() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(firebaseUser.getUid());
        expenseRef = userRef.child("Expenses");
    }

    public void addNewExpenseToRTDB(EventExpense expense) {
        String expenseId = expenseRef.push().getKey();
        expense.setExpenseId(expenseId);
        expenseRef.child(expense.getEventId()).child(expenseId).setValue(expense);
    }

    public MutableLiveData<List<EventExpense>> getAllExpensesByEventId(String eventId) {
        expenseRef.child(eventId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<EventExpense> eventExpenses = new ArrayList<>();
                for (DataSnapshot d: dataSnapshot.getChildren()) {
                    eventExpenses.add(d.getValue(EventExpense.class));
                }

                expenseListLD.postValue(eventExpenses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return expenseListLD;
    }


}
