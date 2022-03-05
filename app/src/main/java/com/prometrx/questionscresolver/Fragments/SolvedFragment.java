package com.prometrx.questionscresolver.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.prometrx.questionscresolver.Fragments.Adapter.SolvedLayoutLastAdapter;
import com.prometrx.questionscresolver.Fragments.Model.SolvedLayoutLastModel;
import com.prometrx.questionscresolver.MainActivity;
import com.prometrx.questionscresolver.R;
import com.prometrx.questionscresolver.Search.FindOutQuizActivity;
import com.prometrx.questionscresolver.Settings.SettingsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SolvedFragment extends Fragment {

    private ImageView solveButton;
    private RecyclerView recyclerView;
    private EditText idEditText;
    private String id = "";
    private ProgressBar progressBar;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    private List<SolvedLayoutLastModel> solvedLayoutLastModelList;
    private SolvedLayoutLastAdapter adapter;

    //Var

    public SolvedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_solved, container, false);

        solveButton = view.findViewById(R.id.SolvedFragmentSolveButton);
        recyclerView = view.findViewById(R.id.SolvedFragmentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        idEditText = view.findViewById(R.id.SolvedFragmentIdEditText);
        progressBar = view.findViewById(R.id.SolvedFragmentProgressBar);

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        solvedLayoutLastModelList = new ArrayList<>();

        solve();
        getData();

        return view;
    }

    private void solve() {

        solveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!idEditText.getText().toString().isEmpty()) {
                    checkFormat();
                }
                else Toast.makeText(requireActivity(), "Please Write Id!", Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void getData() {

        firebaseFirestore.collection("User").document(firebaseUser.getUid()).collection("Answers").orderBy("date", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                solvedLayoutLastModelList.clear();

                for (DocumentSnapshot dc : queryDocumentSnapshots) {

                    String qTitle = dc.get("Title").toString();
                    String qId = dc.get("Qid").toString();
                    String qCount = dc.get("Qcount").toString();

                    SolvedLayoutLastModel solvedLayoutLastModel = new SolvedLayoutLastModel(qId, qTitle, qCount);

                    solvedLayoutLastModelList.add(solvedLayoutLastModel);

                    adapter = new SolvedLayoutLastAdapter(solvedLayoutLastModelList);

                    recyclerView.setAdapter(adapter);


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

    private void checkFormat() {

        id = idEditText.getText().toString();

        try {
            // 565477    5 + 6 + 5 + 4 + 7 = 27
            //int a = Integer.parseInt(String.valueOf(qId.charAt(0)));
            int fivePlus = Integer.parseInt(String.valueOf(id.charAt(0))) + Integer.parseInt(String.valueOf(id.charAt(1))) + Integer.parseInt(String.valueOf(id.charAt(2))) + Integer.parseInt(String.valueOf(id.charAt(3))) + Integer.parseInt(String.valueOf(id.charAt(4)));

            String temp = fivePlus + "";

            int temp6 = Integer.parseInt(String.valueOf(temp.charAt(1)));

            if (id.length() == 6 && temp6 == Integer.parseInt(String.valueOf(id.charAt(5)))) {
                checkId();
            }
            else{
                Toast.makeText(requireActivity(), "Please write correct id!", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e) {
            Toast.makeText(requireActivity(), "Please write only numbers!", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkId() {

        firebaseFirestore.collection("Quiz").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.get("0") != null && documentSnapshot.get("Pending").toString().equals("1")) {

                    String temp = documentSnapshot.get("Repeatable").toString();

                    if (temp.equals("Not Repeatable")){

                        firebaseFirestore.collection("User").document(firebaseUser.getUid()).collection("Answers").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                try {
                                    if (!documentSnapshot.get("Title").toString().isEmpty()) {
                                        Toast.makeText(requireActivity(), "You can do this test once!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (Exception e) {

                                    Intent intent = new Intent(requireActivity(), FindOutQuizActivity.class);
                                    intent.putExtra("WriteIdActivityId" , id);
                                    startActivity(intent);
                                    requireActivity().finish();

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else {

                        Intent intent = new Intent(requireActivity(), FindOutQuizActivity.class);
                        intent.putExtra("WriteIdActivityId" , id);
                        startActivity(intent);
                        requireActivity().finish();

                    }

                }
                else Toast.makeText(requireActivity(), "Quiz not found or pending!", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}