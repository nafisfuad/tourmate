package com.nafisfuad.firebasemad16.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nafisfuad.firebasemad16.pojos.EventExpense;
import com.nafisfuad.firebasemad16.repos.ExpenseRepository;

import java.util.List;

public class ExpenseViewModel extends ViewModel {

    private ExpenseRepository repository;
    public MutableLiveData<List<EventExpense>> expenseListLD = new MutableLiveData<>();

    public ExpenseViewModel() {
        repository = new ExpenseRepository();
    }

    public void saveExpense(EventExpense expense) {
        repository.addNewExpenseToRTDB(expense);
    }

    public void getAllExpenses(String eventId) {
        expenseListLD = repository.getAllExpensesByEventId(eventId);
    }
}
