package com.prometrx.questionscresolver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prometrx.questionscresolver.Account.SignInActivity;
import com.prometrx.questionscresolver.Create.CreateMenuActivity;
import com.prometrx.questionscresolver.Fragments.AccountFragment;
import com.prometrx.questionscresolver.Fragments.CreatedFragment;
import com.prometrx.questionscresolver.Fragments.ExploreFragment;
import com.prometrx.questionscresolver.Fragments.SolvedFragment;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    private void init() {
        bottomNavigationView = findViewById(R.id.MainActivityBottomNav);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportFragmentManager().beginTransaction().replace(R.id.MainActivityContainer, new SolvedFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.Solved);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;

                switch (item.getItemId()){
                    case R.id.Solved:
                        fragment = new SolvedFragment();
                        break;
                    case R.id.Created:
                        fragment = new CreatedFragment();
                        break;
                    case R.id.Explore:
                        fragment = new ExploreFragment();
                        break;
                    case R.id.Account:
                        fragment = new AccountFragment();
                        break;
                    default:
                        fragment = new SolvedFragment();

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.MainActivityContainer, fragment).commit();
                
                return true;
            }
        });

    }

}