package com.prometrx.questionscresolver.Settings;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accounts.Account;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.prometrx.questionscresolver.Account.SignInActivity;
import com.prometrx.questionscresolver.MainActivity;
import com.prometrx.questionscresolver.R;
import com.squareup.picasso.Picasso;


import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    //Component
    private ImageView backImage, changeImage;
    private LinearLayout accountSettings, deleteAccount, signOut;
    private CircleImageView userImage;

    //Var
    private ActivityResultLauncher<String> permissionGallery;

    //Firebase
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
        clickSignOut();
        clickAccountSettings();
        clickDeleteAccount();
        clickBack();;

    }

    private void init() {

        backImage = findViewById(R.id.SettingsActivityBackButton);
        changeImage = findViewById(R.id.SettingsActivityChangeImage);
        userImage = findViewById(R.id.SettingsActivityUserImage);
        accountSettings = findViewById(R.id.SettingsActivityAccountSettings);
        deleteAccount = findViewById(R.id.SettingsActivityDeleteAccount);
        signOut = findViewById(R.id.SettingsActivitySignOut);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.setLoggingEnabled(true);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        clickChangeImage();
        getUserData();
        launchers();


    }


    private void clickBack() {

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void clickSignOut() {

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth.signOut();

                Intent intent = new Intent(SettingsActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }

    private void getUserData() {

        firebaseFirestore.collection("User").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String imageUrl = documentSnapshot.get("ImageUrl").toString();

                if (imageUrl.equals("0")){
                    userImage.setImageResource(R.drawable.account_logo);
                }else {
                    Picasso.get().load(imageUrl).into(userImage);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SettingsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void clickAccountSettings() {

        accountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SettingsActivity.this, AccountSettingsActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private void clickDeleteAccount() {

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(SettingsActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void clickChangeImage() {

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    permissionGallery.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                else{
                    imagePicker();
                }

            }
        });

    }

    private void launchers() {

        permissionGallery = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {

                if (result) {
                    imagePicker();
                }
                else{
                    Toast.makeText(SettingsActivity.this, "Permission Needed for Gallery!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void imagePicker() {

        ImagePicker.with(SettingsActivity.this)
                .cropSquare()//Crop image(Optional), Check Customization for more option
                .galleryOnly()
                .compress(1024)//Final image size will be less than 1 MB(Optional)
                .maxResultSize(256, 256)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {

            userImage.setImageURI(data.getData());

            String path = "UserImage/"+ firebaseUser.getUid()+".jpg";

            storageReference.child(path).putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    StorageReference newRefrence = firebaseStorage.getReference(path);

                    newRefrence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            firebaseFirestore.collection("User").document(firebaseUser.getUid()).update("ImageUrl", uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {



                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SettingsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SettingsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SettingsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });



        }
    }
}