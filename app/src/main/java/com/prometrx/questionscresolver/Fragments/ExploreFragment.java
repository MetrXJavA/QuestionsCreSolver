package com.prometrx.questionscresolver.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.prometrx.questionscresolver.AfterSearch.AfterSearchActivity;
import com.prometrx.questionscresolver.Fragments.Adapter.ExploreAdapter;
import com.prometrx.questionscresolver.Fragments.Model.ExploreModel;
import com.prometrx.questionscresolver.R;
import com.prometrx.questionscresolver.Settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;


public class ExploreFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private List<ExploreModel> exploreModelList;
    private RecyclerView recyclerView;
    private ExploreAdapter exploreAdapter;
    private ProgressBar progressBar;
    private EditText searchEditText;
    private ImageView searchImageView;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        exploreModelList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.ExploreFragmentRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        progressBar = view.findViewById(R.id.ExploreFragmentProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        searchEditText = view.findViewById(R.id.ExploreFragmentSearchEditText);
        searchImageView = view.findViewById(R.id.ExploreFragmentSearch);

        getData();
        filterData();

        return view;
    }

    private void getData() {

        firebaseFirestore.collection("Quiz").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (value != null) {

                    exploreModelList.clear();

                    for (DocumentSnapshot dc : value){

                        if (!dc.get("Uid").toString().equals(firebaseUser.getUid())){

                            ExploreModel exploreModel = new ExploreModel(dc.get("Qcount").toString(), dc.get("Title").toString(), dc.get("Qid").toString());
                            exploreModelList.add(exploreModel);

                            exploreAdapter = new ExploreAdapter(exploreModelList);

                            recyclerView.setAdapter(exploreAdapter);
                        }


                    }

                    progressBar.setVisibility(View.GONE);

                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(), "Failed 73", Toast.LENGTH_SHORT).show();
                }

                if (error != null) Toast.makeText(requireActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void filterData() {

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!searchEditText.getText().toString().trim().isEmpty()){

                    Intent intent = new Intent(requireActivity(), AfterSearchActivity.class);
                    intent.putExtra("Words", searchEditText.getText().toString().trim());
                    startActivity(intent);
                    requireActivity().finish();

                }
                else Toast.makeText(requireActivity(), "Please Fill the Field!", Toast.LENGTH_SHORT).show();


            }
        });

    }

}