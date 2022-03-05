package com.prometrx.questionscresolver.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.prometrx.questionscresolver.Create.CreateMenuActivity;
import com.prometrx.questionscresolver.Fragments.Adapter.CreateLayoutLastModelAdapter;
import com.prometrx.questionscresolver.Fragments.Model.CreateLayoutLastModel;
import com.prometrx.questionscresolver.R;
import com.prometrx.questionscresolver.Settings.SettingsActivity;


import java.util.ArrayList;
import java.util.List;



public class CreatedFragment extends Fragment {

    private ImageView createButton;
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;

    private List<CreateLayoutLastModel> createLayoutLastModelList;
    private CreateLayoutLastModelAdapter adapter;

    public CreatedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_createdk, container, false);

        createButton = view.findViewById(R.id.CreatedFragmentCreateButton);
        recyclerView = view.findViewById(R.id.CreatedFragmentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        progressBar = view.findViewById(R.id.CreatedFragmentProgressBar);

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        createLayoutLastModelList = new ArrayList<>();

        create();
        getData();

        return view;
    }

    private void create() {

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(requireActivity(), CreateMenuActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

    }

    private void getData() {

        firebaseFirestore.collection("Quiz").orderBy("date", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                createLayoutLastModelList.clear();

                for (DocumentSnapshot dc : queryDocumentSnapshots) {

                    if (dc.get("Uid").equals(firebaseUser.getUid()) && dc.get("Pending").toString().equals("1")){

                        String qId = dc.get("Qid").toString();

                        String qTitle = dc.get("Title").toString();

                        firebaseFirestore.collection("Quiz").document(qId).collection("Answers").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                if (value != null) {

                                    String aCount = value.size() + "";

                                    CreateLayoutLastModel createLayoutLastModel = new CreateLayoutLastModel(qId, qTitle, aCount);
                                    createLayoutLastModelList.add(createLayoutLastModel);

                                    adapter = new CreateLayoutLastModelAdapter(createLayoutLastModelList);
                                    recyclerView.setAdapter(adapter);

                                }
                                if (error != null) {
                                    Toast.makeText(requireActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }

                }
                progressBar.setVisibility(View.GONE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}