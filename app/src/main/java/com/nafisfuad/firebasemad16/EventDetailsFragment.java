package com.nafisfuad.firebasemad16;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nafisfuad.firebasemad16.adapters.MomentsAdapter;
import com.nafisfuad.firebasemad16.databinding.FragmentEventDetailsBinding;
import com.nafisfuad.firebasemad16.databinding.FragmentEventListBinding;
import com.nafisfuad.firebasemad16.helper.EventUtils;
import com.nafisfuad.firebasemad16.pojos.EventExpense;
import com.nafisfuad.firebasemad16.pojos.Moments;
import com.nafisfuad.firebasemad16.pojos.TourmateEvent;
import com.nafisfuad.firebasemad16.viewmodels.EventViewModel;
import com.nafisfuad.firebasemad16.viewmodels.ExpenseViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailsFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 456;
    private static final String TAG = EventDetailsFragment.class.getSimpleName();
    private static final int REQUEST_STORAGE_CODE = 999;
    private String eventId = null;
    private int totalBudget = 0;
    private ExpenseViewModel expenseViewModel;
    private EventViewModel eventViewModel;
    private FragmentEventDetailsBinding binding;
    private String currentPhotoPath;
    private List<Moments> momentsList = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventDetailsFragment newInstance(String param1, String param2) {
        EventDetailsFragment fragment = new EventDetailsFragment();
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
        binding = FragmentEventDetailsBinding.inflate(LayoutInflater.from(getActivity()));
        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        Bundle bundle = getArguments();
        if (bundle != null) {
            eventId = bundle.getString("id");
            eventViewModel.getEventDetails(eventId);
            expenseViewModel.getAllExpenses(eventId);
            eventViewModel.getMoments(eventId);
        }

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_event_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventViewModel.eventDetailsLD.observe(this, new Observer<TourmateEvent>() {
            @Override
            public void onChanged(TourmateEvent event) {
                binding.detailsEventName.setText(event.getEventName());
                binding.detailsInitialBudget.setText("Total budget: " + event.getBudget());
                totalBudget = event.getBudget();
            }
        });

        eventViewModel.momentsLD.observe(this, new Observer<List<Moments>>() {
            @Override
            public void onChanged(List<Moments> moments) {
                momentsList = moments;
            }
        });

        expenseViewModel.expenseListLD.observe(this, new Observer<List<EventExpense>>() {
            @Override
            public void onChanged(List<EventExpense> eventExpenses) {
                int totalEx = 0;
                for (EventExpense ex: eventExpenses) {
                    totalEx += ex.getAmount();
                }
                binding.detailsTotalExpense.setText("Total Expense: " + totalEx);
                int remainingBudget = totalBudget - totalEx;
                binding.detailsRemainingBudget.setText("Remaining budget: " + remainingBudget);
            }
        });

        binding.addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddExpenseDialog();
            }
        });

        binding.addMomentBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkStoragePermission()) {
                    dispatchTakePictureIntent();
                }
            }
        });

        binding.viewMomentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMomentGalleryDialog();
            }
        });
    }

    private void showMomentGalleryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("All Moments");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_sheet_gallery, null);
//        builder.setView(view);

        final RecyclerView recyclerView = view.findViewById(R.id.momentGalleryRV);
        final MomentsAdapter adapter = new MomentsAdapter(getActivity(), momentsList);
        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(adapter);
        builder.setView(view);
        builder.setNegativeButton("cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.nafisfuad.firebasemad16",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
            Log.e(TAG, "onActivityResult: " + currentPhotoPath);
            File file = new File(currentPhotoPath);
            eventViewModel.uploadImageToFirebaseStorage(file, eventId);
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkStoragePermission() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, REQUEST_STORAGE_CODE);
            return false;
        }
        return true;
    }

    private void showAddExpenseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add New Expense");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_expense_dialog_box, null);
        builder.setView(view);
        final EditText amountET = view.findViewById(R.id.dialogAddExpenseAmountInput);
        final EditText nameET = view.findViewById(R.id.dialogAddExpenseNameInput);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String amount = amountET.getText().toString();
                String name = nameET.getText().toString();
                if (amount.isEmpty() && name.isEmpty()) {
                    //toast
                    return;
                }
                EventExpense expense = new EventExpense(null, eventId, name, Integer.parseInt(amount), EventUtils.getCurrentDateTime());
                expenseViewModel.saveExpense(expense);
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}