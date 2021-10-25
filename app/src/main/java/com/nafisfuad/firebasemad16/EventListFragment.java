package com.nafisfuad.firebasemad16;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nafisfuad.firebasemad16.adapters.EventAdapter;
import com.nafisfuad.firebasemad16.databinding.FragmentEventListBinding;
import com.nafisfuad.firebasemad16.pojos.TourmateEvent;
import com.nafisfuad.firebasemad16.viewmodels.EventViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventListFragment extends Fragment {
    private static final String TAG = EventListFragment.class.getSimpleName();
    private EventAdapter adapter;
    private EventViewModel eventViewModel;
    private FragmentEventListBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventListFragment newInstance(String param1, String param2) {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add_event:
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_eventListFragment_to_addEventFragment);
                break;
            case R.id.item_weather:
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_eventListFragment_to_weatherFragment);
                break;
            case R.id.item_nearby:
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_eventListFragment_to_nearbyPlaceFragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_event_list, container, false);
        binding = FragmentEventListBinding.inflate(LayoutInflater.from(getActivity()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventViewModel.eventListLD.observe(this, new Observer<List<TourmateEvent>>() {
            @Override
            public void onChanged(List<TourmateEvent> tourmateEvents) {
                Log.e(TAG, "onChanged: " + tourmateEvents.size());
                adapter = new EventAdapter(getActivity(), tourmateEvents);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                binding.eventRV.setLayoutManager(llm);
                binding.eventRV.setAdapter(adapter);
            }
        });
    }
}