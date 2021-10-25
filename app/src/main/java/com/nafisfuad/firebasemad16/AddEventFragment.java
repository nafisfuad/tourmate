package com.nafisfuad.firebasemad16;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.nafisfuad.firebasemad16.databinding.FragmentAddEventBinding;
import com.nafisfuad.firebasemad16.pojos.TourmateEvent;
import com.nafisfuad.firebasemad16.viewmodels.EventViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventFragment extends Fragment {
    private FragmentAddEventBinding binding;
    private String departureDate = null;
    private EventViewModel eventViewModel;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEventFragment newInstance(String param1, String param2) {
        AddEventFragment fragment = new AddEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_event, container, false);
        binding = FragmentAddEventBinding.inflate(LayoutInflater.from(getActivity()));
        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.createNewEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventName = binding.eventNameInput.getText().toString();
                String departurePlace = binding.departureInput.getText().toString();
                String destination = binding.destinationInput.getText().toString();
                String budget = binding.budgetInput.getText().toString();
                if (eventName.isEmpty() && departurePlace.isEmpty() && destination.isEmpty() && budget.isEmpty() && departureDate.isEmpty()) {
                    Toast.makeText(getActivity(), "provide all information", Toast.LENGTH_SHORT).show();
                }
                TourmateEvent event = new TourmateEvent(null, eventName, departurePlace, destination, Integer.parseInt(budget), departureDate);
                eventViewModel.saveEvent(event);
                Navigation.findNavController(view).navigate(R.id.eventListFragment);
            }
        });
        
        binding.dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
        dpd.show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(i, i1, i2);
            departureDate = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
            binding.dateBtn.setText(departureDate);
        }
    };
}