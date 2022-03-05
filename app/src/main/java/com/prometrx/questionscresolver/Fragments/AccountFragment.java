package com.prometrx.questionscresolver.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.prometrx.questionscresolver.Create.CreateMenuActivity;
import com.prometrx.questionscresolver.Fragments.Adapter.AccountAdapter;
import com.prometrx.questionscresolver.Fragments.Model.AccountModel;
import com.prometrx.questionscresolver.R;
import com.prometrx.questionscresolver.Settings.SettingsActivity;
import com.squareup.picasso.Picasso;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    private TextView username, solve, create, accuracy;
    private ImageView dots;
    private CircleImageView userImage;
    private RecyclerView recyclerView;
    private AccountAdapter adapter;

    //Firebase
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    //Var
    private float tCount = 0f;
    private float tqCount = 0f;
    private List<AccountModel> accountModelList;
    private Handler handler;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        //---------------------------------------
        FirebaseFirestore.setLoggingEnabled(true);
        //---------------------------------------
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        handler = new Handler();

        username = view.findViewById(R.id.AccountFragmentUsername);
        solve = view.findViewById(R.id.AccountFragmentSolveCount);
        create = view.findViewById(R.id.AccountFragmentCreateCount);
        accuracy = view.findViewById(R.id.AccountFragmentAccuracyPercent);
        userImage = view.findViewById(R.id.AccountFragmentProfileImage);
        dots = view.findViewById(R.id.AccountFragmentDots);
        recyclerView = view.findViewById(R.id.AccountFragmentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setHasFixedSize(true);
        accountModelList = new ArrayList<>();

        getUserData();
        getSolveData();
        getCreateData();
        getAccuracyData();
        clickSettings();
        getPendingQuizData();

        return view;
    }

    private void getUserData() {

        firebaseFirestore.collection("User").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String uName = documentSnapshot.get("Username").toString();
                String imageUrl = documentSnapshot.get("ImageUrl").toString();

                username.setText(uName);

                if (imageUrl.equals("0")){
                    userImage.setImageResource(R.drawable.account_logo);
                }else {
                    Picasso.get().load(imageUrl).into(userImage);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getSolveData() {

        firebaseFirestore.collection("User").document(firebaseUser.getUid()).collection("Answers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                solve.setText(queryDocumentSnapshots.size() + "");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getCreateData() {

        firebaseFirestore.collection("Quiz").whereEqualTo("Uid", firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                create.setText(queryDocumentSnapshots.size() + "");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getAccuracyData() {

        firebaseFirestore.collection("User").document(firebaseUser.getUid()).collection("Answers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                tqCount = 0f;
                tCount = 0f;

                for (DocumentSnapshot dc: queryDocumentSnapshots) {

                    tqCount += (Integer.parseInt(dc.get("Qcount").toString()) +1);
                    String qId = dc.get("Qid").toString();

                    firebaseFirestore.collection("Quiz").document(qId).collection("Answers").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            for (int i = 0; i <= Integer.parseInt(documentSnapshot.get("Qcount").toString()); i++ ){

                                String tf = "TF" + i;

                                if ((Boolean) documentSnapshot.get(tf)){
                                    tCount++;
                                }

                            }

                            float accuracyInt = (tCount / tqCount) * 100;

                            accuracy.setText("%"+((int)accuracyInt));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void clickSettings() {

        dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(requireActivity(), SettingsActivity.class);
                startActivity(intent);
                requireActivity().finish();

            }
        });

    }

    private void getPendingQuizData() {

        firebaseFirestore.collection("Quiz").orderBy("date", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                accountModelList.clear();

                for (DocumentSnapshot dc : queryDocumentSnapshots) {

                    if (!dc.get("Pending").toString().equals("1") && dc.get("Uid").toString().equals(firebaseUser.getUid())) {

                        String qid = dc.get("Qid").toString();
                        String qTitle = dc.get("Title").toString();
                        String pending = dc.get("Pending").toString();

                        AccountModel m = new AccountModel(qid, qTitle, pending);
                        accountModelList.add(m);
                        adapter = new AccountAdapter(accountModelList);
                        recyclerView.setAdapter(adapter);

                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}