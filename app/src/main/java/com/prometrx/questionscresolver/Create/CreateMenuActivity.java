package com.prometrx.questionscresolver.Create;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.prometrx.questionscresolver.AlertDialog.CustomAlertDialog;
import com.prometrx.questionscresolver.Create.Model.QuestionModel;
import com.prometrx.questionscresolver.MainActivity;
import com.prometrx.questionscresolver.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CreateMenuActivity extends AppCompatActivity {

    private ActivityResultLauncher<String> permissionLauncher;
    private ImageView backImage, doneImage, forwardImage, questionImage, addChoiceButton, deleteChoiceButton, addImage;
    private EditText questionEditText, choice1, choice2, choice3, choice4;
    private Switch switchTC;
    private TextView questionNumber, time;
    private int questionCount = 1;
    private int questionAnswer = 0;
    private String qId = "";
    private Uri imageUri;
    private String imageUrl = "0";
    private SeekBar seekBar;

    private CustomAlertDialog customAlertDialog;

    private List<QuestionModel> questionModelList;

    //Firebase Tools
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_menu);

        init();
        timeSelect();
        back();
        done();
        forward();
        addChoice();
        deleteChoice();
        switchChange();
        doneButtons();
        createId();
        askPermission();
        clickAddImage();
    }

    private void init() {

        customAlertDialog = new CustomAlertDialog(CreateMenuActivity.this);

        backImage = findViewById(R.id.CreateMenuActivityBack);
        doneImage = findViewById(R.id.CreateMenuActivityDone);
        forwardImage = findViewById(R.id.CreateMenuActivityNextQuestion);
        questionImage = findViewById(R.id.CreateMenuActivityQuestionImage);
        addImage = findViewById(R.id.CreateMenuActivityAddImage);
        time = findViewById(R.id.CreateMenuActivityTimeTextView);
        seekBar = findViewById(R.id.CreateMenuActivitySelectTimeSeekBar);
        seekBar.setProgress(20);
        time.setText(seekBar.getProgress()+"");

        questionEditText = findViewById(R.id.CreateMenuActivityQuestionEditText);

        choice1 = findViewById(R.id.CreateMenuActivityChoiceOne);
        choice2 = findViewById(R.id.CreateMenuActivityChoiceTwo);
        choice3 = findViewById(R.id.CreateMenuActivityChoiceThree);
        choice4 = findViewById(R.id.CreateMenuActivityChoiceFour);

        addChoiceButton = findViewById(R.id.CreateMenuActivityAddChoiceButton);

        deleteChoiceButton = findViewById(R.id.CreateMenuActivityDeleteChoice);

        switchTC = findViewById(R.id.CreateMenuActivitySelectTOrC);

        questionNumber = findViewById(R.id.CreateMenuActivityQuestionNumber);

        questionNumber.setText("" + questionCount);

        questionModelList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


    }

    private void timeSelect() {

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                time.setText(i + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void back() {

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CreateMenuActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private void done() {

        doneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Fixle If ile

                if (questionModelList.size() < 1) {
                    Toast.makeText(CreateMenuActivity.this, "Please add question", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Generate Id

                    HashMap<String, Object> hashMap = new HashMap<>();

                    int x = 0;

                    for (QuestionModel qm : questionModelList) {

                        hashMap.put("" + x, qm);
                        x++;
                    }

                    hashMap.put("Uid", firebaseUser.getUid());
                    hashMap.put("Qid", qId);
                    hashMap.put("date", FieldValue.serverTimestamp());
                    hashMap.put("Qcount", questionCount - 1);
                    hashMap.put("Title", "Not Enter");
                    hashMap.put("Repeatable", "Repeatable");
                    hashMap.put("Pending", 0);// 0 = wait, 1 = ok;

                    firebaseFirestore.collection("Quiz").document(qId + "").set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent = new Intent(CreateMenuActivity.this, IdActivity.class);
                            intent.putExtra("QuizId", "" + qId);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateMenuActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private void forward() {

        forwardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!questionEditText.getText().toString().trim().isEmpty()) {

                    if (switchTC.getText().equals("Classic")){

                        //Next

                        QuestionModel questionModel = new QuestionModel(questionEditText.getText().toString().trim(), true);
                        questionModelList.add(questionModel);
                        questionCount++;
                        questionNumber.setText(""+questionCount);
                        questionEditText.setText("");

                    }

                    else {

                        if (!choice1.getText().toString().trim().isEmpty() && !choice2.getText().toString().trim().isEmpty() && questionAnswer != 0) {

                            if (choice3.getVisibility() == View.VISIBLE && choice4.getVisibility() == View.VISIBLE) {
                                //Get Both

                                if (!choice3.getText().toString().trim().isEmpty() && !choice4.getText().toString().trim().isEmpty()) {
                                    //Next

                                    if (imageUri == null) {

                                        QuestionModel questionModel = new QuestionModel(questionEditText.getText().toString().trim(), choice1.getText().toString().trim(), choice2.getText().toString().trim(), choice3.getText().toString().trim(), choice4.getText().toString().trim(), false, questionAnswer, imageUrl, seekBar.getProgress());
                                        questionModelList.add(questionModel);

                                        questionCount++;
                                        questionNumber.setText(""+questionCount);
                                        questionEditText.setText("");
                                        choice1.setText("");
                                        choice2.setText("");
                                        choice3.setText("");
                                        choice4.setText("");
                                        choice3.setVisibility(View.GONE);
                                        choice4.setVisibility(View.GONE);
                                        deleteChoiceButton.setVisibility(View.GONE);
                                        addChoiceButton.setVisibility(View.VISIBLE);

                                        switch (questionAnswer){
                                            case 1:
                                                choice1.setBackgroundResource(R.drawable.choice_edit_text);
                                                break;
                                            case 2:
                                                choice2.setBackgroundResource(R.drawable.choice_edit_text);
                                                break;
                                            case 3:
                                                choice3.setBackgroundResource(R.drawable.choice_edit_text);
                                                break;
                                            case 4:
                                                choice4.setBackgroundResource(R.drawable.choice_edit_text);
                                                break;
                                            default:
                                                choice1.setBackgroundResource(R.drawable.choice_edit_text);
                                                choice2.setBackgroundResource(R.drawable.choice_edit_text);
                                                choice3.setBackgroundResource(R.drawable.choice_edit_text);
                                                choice4.setBackgroundResource(R.drawable.choice_edit_text);
                                        }

                                        questionAnswer = 0;
                                        imageUrl = "0";
                                        imageUri = null;

                                    }

                                    else{

                                        customAlertDialog.startDialog();

                                        String imagePath = "Quiz/Images/" + qId + "/" + questionCount + ".jpg";

                                        storageReference.child(imagePath).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                StorageReference newReference = FirebaseStorage.getInstance().getReference(imagePath);
                                                newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {

                                                        imageUrl = uri + "";

                                                        QuestionModel questionModel = new QuestionModel(questionEditText.getText().toString().trim(), choice1.getText().toString().trim(), choice2.getText().toString().trim(), choice3.getText().toString().trim(), choice4.getText().toString().trim(), false,questionAnswer, imageUrl, seekBar.getProgress());
                                                        questionModelList.add(questionModel);

                                                        questionCount++;
                                                        questionNumber.setText(""+questionCount);
                                                        questionEditText.setText("");
                                                        choice1.setText("");
                                                        choice2.setText("");
                                                        choice3.setText("");
                                                        choice4.setText("");
                                                        choice3.setVisibility(View.GONE);
                                                        choice4.setVisibility(View.GONE);
                                                        deleteChoiceButton.setVisibility(View.GONE);
                                                        addChoiceButton.setVisibility(View.VISIBLE);

                                                        switch (questionAnswer){
                                                            case 1:
                                                                choice1.setBackgroundResource(R.drawable.choice_edit_text);
                                                                break;
                                                            case 2:
                                                                choice2.setBackgroundResource(R.drawable.choice_edit_text);
                                                                break;
                                                            case 3:
                                                                choice3.setBackgroundResource(R.drawable.choice_edit_text);
                                                                break;
                                                            case 4:
                                                                choice4.setBackgroundResource(R.drawable.choice_edit_text);
                                                                break;
                                                            default:
                                                                choice1.setBackgroundResource(R.drawable.choice_edit_text);
                                                                choice2.setBackgroundResource(R.drawable.choice_edit_text);
                                                                choice3.setBackgroundResource(R.drawable.choice_edit_text);
                                                                choice4.setBackgroundResource(R.drawable.choice_edit_text);
                                                        }

                                                        questionAnswer = 0;
                                                        questionImage.setVisibility(View.GONE);
                                                        imageUrl = "0";
                                                        imageUri = null;

                                                        customAlertDialog.dismissDialog();

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        customAlertDialog.dismissDialog();
                                                        Toast.makeText(CreateMenuActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                customAlertDialog.dismissDialog();
                                                Toast.makeText(CreateMenuActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                }

                                else{
                                    customAlertDialog.dismissDialog();
                                    Toast.makeText(CreateMenuActivity.this, "Please fill all the fields and select answer by double click choice.", Toast.LENGTH_SHORT).show();
                                }

                            }

                            else if (choice3.getVisibility() == View.VISIBLE && choice4.getVisibility() == View.GONE){

                                if (!choice3.getText().toString().trim().isEmpty()) {

                                    //Next

                                    if (imageUri == null) {
                                        QuestionModel questionModel = new QuestionModel(questionEditText.getText().toString().trim(), choice1.getText().toString().trim(), choice2.getText().toString().trim(), choice3.getText().toString().trim(), choice4.getText().toString().trim(), false,questionAnswer, imageUrl, seekBar.getProgress());
                                        questionModelList.add(questionModel);

                                        questionCount++;
                                        questionNumber.setText(""+questionCount);
                                        questionEditText.setText("");
                                        choice1.setText("");
                                        choice2.setText("");
                                        choice3.setText("");
                                        choice3.setVisibility(View.GONE);

                                        switch (questionAnswer){
                                            case 1:
                                                choice1.setBackgroundResource(R.drawable.choice_edit_text);
                                                break;
                                            case 2:
                                                choice2.setBackgroundResource(R.drawable.choice_edit_text);
                                                break;
                                            case 3:
                                                choice3.setBackgroundResource(R.drawable.choice_edit_text);
                                                break;
                                            default:
                                                choice1.setBackgroundResource(R.drawable.choice_edit_text);
                                                choice2.setBackgroundResource(R.drawable.choice_edit_text);
                                                choice3.setBackgroundResource(R.drawable.choice_edit_text);
                                        }

                                        questionAnswer = 0;

                                        imageUrl = "0";
                                        imageUri = null;
                                    }

                                    else{

                                        customAlertDialog.startDialog();

                                        String imagePath = "Quiz/Images/" + qId + "/" + questionCount + ".jpg";

                                        storageReference.child(imagePath).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                StorageReference newReference = FirebaseStorage.getInstance().getReference(imagePath);
                                                newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {

                                                        imageUrl = uri + "";

                                                        QuestionModel questionModel = new QuestionModel(questionEditText.getText().toString().trim(), choice1.getText().toString().trim(), choice2.getText().toString().trim(), choice3.getText().toString().trim(), choice4.getText().toString().trim(), false,questionAnswer, imageUrl, seekBar.getProgress());
                                                        questionModelList.add(questionModel);

                                                        questionCount++;
                                                        questionNumber.setText(""+questionCount);
                                                        questionEditText.setText("");
                                                        choice1.setText("");
                                                        choice2.setText("");
                                                        choice3.setText("");
                                                        choice3.setVisibility(View.GONE);

                                                        switch (questionAnswer){
                                                            case 1:
                                                                choice1.setBackgroundResource(R.drawable.choice_edit_text);
                                                                break;
                                                            case 2:
                                                                choice2.setBackgroundResource(R.drawable.choice_edit_text);
                                                                break;
                                                            case 3:
                                                                choice3.setBackgroundResource(R.drawable.choice_edit_text);
                                                                break;
                                                            default:
                                                                choice1.setBackgroundResource(R.drawable.choice_edit_text);
                                                                choice2.setBackgroundResource(R.drawable.choice_edit_text);
                                                                choice3.setBackgroundResource(R.drawable.choice_edit_text);
                                                        }

                                                        questionAnswer = 0;
                                                        questionImage.setVisibility(View.GONE);
                                                        imageUrl = "0";
                                                        imageUri = null;
                                                        customAlertDialog.dismissDialog();

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        customAlertDialog.dismissDialog();
                                                        Toast.makeText(CreateMenuActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                customAlertDialog.dismissDialog();
                                                Toast.makeText(CreateMenuActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                }

                                else {
                                    customAlertDialog.dismissDialog();
                                    Toast.makeText(CreateMenuActivity.this, "Please fill all the fields and select answer by double click choice.", Toast.LENGTH_SHORT).show();
                                }

                            }

                            else{

                                //Next
                                if (imageUri == null) {
                                    QuestionModel questionModel = new QuestionModel(questionEditText.getText().toString().trim(), choice1.getText().toString().trim(), choice2.getText().toString().trim(), choice3.getText().toString().trim(), choice4.getText().toString().trim(), false,questionAnswer, imageUrl, seekBar.getProgress());
                                    questionModelList.add(questionModel);

                                    questionCount++;
                                    questionNumber.setText(""+questionCount);
                                    questionEditText.setText("");
                                    choice1.setText("");
                                    choice2.setText("");

                                    switch (questionAnswer){
                                        case 1:
                                            choice1.setBackgroundResource(R.drawable.choice_edit_text);
                                            break;
                                        case 2:
                                            choice2.setBackgroundResource(R.drawable.choice_edit_text);
                                            break;
                                        default:
                                            choice1.setBackgroundResource(R.drawable.choice_edit_text);
                                            choice2.setBackgroundResource(R.drawable.choice_edit_text);
                                    }

                                    questionAnswer = 0;
                                    imageUrl = "0";
                                    imageUri = null;
                                }

                                else{

                                    customAlertDialog.startDialog();

                                    String imagePath = "Quiz/Images/" + qId + "/" + questionCount + ".jpg";

                                    storageReference.child(imagePath).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            StorageReference newReference = FirebaseStorage.getInstance().getReference(imagePath);
                                            newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    imageUrl = uri + "";

                                                    QuestionModel questionModel = new QuestionModel(questionEditText.getText().toString().trim(), choice1.getText().toString().trim(), choice2.getText().toString().trim(), choice3.getText().toString().trim(), choice4.getText().toString().trim(), false,questionAnswer, imageUrl, seekBar.getProgress());
                                                    questionModelList.add(questionModel);

                                                    questionCount++;
                                                    questionNumber.setText(""+questionCount);
                                                    questionEditText.setText("");
                                                    choice1.setText("");
                                                    choice2.setText("");

                                                    switch (questionAnswer){
                                                        case 1:
                                                            choice1.setBackgroundResource(R.drawable.choice_edit_text);
                                                            break;
                                                        case 2:
                                                            choice2.setBackgroundResource(R.drawable.choice_edit_text);
                                                            break;
                                                        default:
                                                            choice1.setBackgroundResource(R.drawable.choice_edit_text);
                                                            choice2.setBackgroundResource(R.drawable.choice_edit_text);
                                                    }

                                                    questionAnswer = 0;
                                                    questionImage.setVisibility(View.GONE);
                                                    imageUrl = "0";
                                                    imageUri = null;

                                                    customAlertDialog.dismissDialog();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    customAlertDialog.dismissDialog();
                                                    Toast.makeText(CreateMenuActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            customAlertDialog.dismissDialog();
                                            Toast.makeText(CreateMenuActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }

                        }
                        else {
                            Toast.makeText(CreateMenuActivity.this, "Please fill all the fields and select answer by double click choice.", Toast.LENGTH_SHORT).show();
                        }

                    }

                }

                else {
                    Toast.makeText(CreateMenuActivity.this, "Please fill question!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void addChoice() {

        addChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (choice3.getVisibility() == View.GONE){
                    choice3.setVisibility(View.VISIBLE);
                }
                else{
                    choice4.setVisibility(View.VISIBLE);
                    addChoiceButton.setVisibility(View.INVISIBLE);
                    deleteChoiceButton.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private void deleteChoice() {

        deleteChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (choice4.getVisibility() == View.VISIBLE) {
                    choice4.setVisibility(View.GONE);
                    choice4.setText("");
                    choice4.setBackgroundResource(R.drawable.choice_edit_text);

                    if (questionAnswer == 4) questionAnswer = 0;

                }
                else{
                    choice3.setVisibility(View.GONE);
                    addChoiceButton.setVisibility(View.VISIBLE);
                    deleteChoiceButton.setVisibility(View.INVISIBLE);
                    choice3.setText("");
                    choice3.setBackgroundResource(R.drawable.choice_edit_text);
                    if (questionAnswer == 3) questionAnswer = 0;
                }

            }
        });

    }

    private void switchChange() {

        switchTC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


            }
        });

    }

    //Click EditText
    private void doneButtons() {

        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (questionAnswer != 1) {
                    choice1.setBackgroundResource(R.drawable.choice_edit_text_green);
                    choice2.setBackgroundResource(R.drawable.choice_edit_text);
                    choice3.setBackgroundResource(R.drawable.choice_edit_text);
                    choice4.setBackgroundResource(R.drawable.choice_edit_text);
                    questionAnswer = 1;
                }
                else {
                    choice1.setBackgroundResource(R.drawable.choice_edit_text);
                    questionAnswer = 0;
                }

            }
        });

        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (questionAnswer != 2) {
                    choice2.setBackgroundResource(R.drawable.choice_edit_text_green);
                    choice1.setBackgroundResource(R.drawable.choice_edit_text);
                    choice3.setBackgroundResource(R.drawable.choice_edit_text);
                    choice4.setBackgroundResource(R.drawable.choice_edit_text);
                    questionAnswer = 2;
                }
                else {
                    choice2.setBackgroundResource(R.drawable.choice_edit_text);
                    questionAnswer = 0;
                }

            }
        });

        choice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (questionAnswer != 3) {
                    choice3.setBackgroundResource(R.drawable.choice_edit_text_green);
                    choice1.setBackgroundResource(R.drawable.choice_edit_text);
                    choice2.setBackgroundResource(R.drawable.choice_edit_text);
                    choice4.setBackgroundResource(R.drawable.choice_edit_text);
                    questionAnswer = 3;
                }
                else {
                    choice3.setBackgroundResource(R.drawable.choice_edit_text);
                    questionAnswer = 0;
                }

            }
        });

        choice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (questionAnswer != 4) {
                    choice4.setBackgroundResource(R.drawable.choice_edit_text_green);
                    choice1.setBackgroundResource(R.drawable.choice_edit_text);
                    choice2.setBackgroundResource(R.drawable.choice_edit_text);
                    choice3.setBackgroundResource(R.drawable.choice_edit_text);
                    questionAnswer = 4;
                }
                else {
                    choice4.setBackgroundResource(R.drawable.choice_edit_text);
                    questionAnswer = 0;
                }

            }
        });
    }

    private void createId() {

        Random r = new Random();

        for(int i = 0; i<5;i++) {

            int x = r.nextInt(9) + 1;
            qId = qId + x;

        }

        int a = Integer.parseInt(String.valueOf(qId.charAt(0)));
        int b = Integer.parseInt(String.valueOf(qId.charAt(1)));
        int c = Integer.parseInt(String.valueOf(qId.charAt(2)));
        int d = Integer.parseInt(String.valueOf(qId.charAt(3)));
        int e = Integer.parseInt(String.valueOf(qId.charAt(4)));

        String tempF = ""+(a + b + c + d +e);

        int f = Integer.parseInt(String.valueOf(tempF.charAt(1)));

        qId = qId + f;

        firebaseFirestore.collection("Quiz").document(qId +"").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                try {
                    if (documentSnapshot.get("Qid").toString() != null) {

                        qId = "";
                        createId();
                    }
                }catch (Exception e) {

                    //ID ok
                }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(CreateMenuActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void clickAddImage() {

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(CreateMenuActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                else imagePicker();

            }
        });

    }

    private void imagePicker() {

        ImagePicker.with(CreateMenuActivity.this)
                .crop(16f, 9f)
                .galleryOnly()//Crop image(Optional), Check Customization for more option
                .compress(1024)//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();

    }

    private void askPermission() {

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {

                if (result){
                    imagePicker();
                }
                else{
                    Toast.makeText(CreateMenuActivity.this, "Permission Needed for Images!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageUri = data.getData();

        if (imageUri != null) {
            questionImage.setImageURI(imageUri);
            questionImage.setVisibility(View.VISIBLE);
        }


    }

    /*
    private void getData() {

        firebaseFirestore.collection("Quiz").document("4").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                HashMap<String, Object> dc= (HashMap<String, Object>) documentSnapshot.get("0"); //Hashmap'e castledik ve oradan index numarsi ile aldik. !
                //Toast.makeText(CreateMenuActivity.this, "" + questionModel.getChoiceOne(), Toast.LENGTH_SHORT).show();

                System.out.println("asdasd--" + dc.get("question")); //Hashmap oldugu icin key yerine adi yazdik. !

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    */

    /*
                                        //------------------------------
                                    if (imageUri == null) {
                                        QuestionModel questionModel = new QuestionModel(questionEditText.getText().toString().trim(), choiceOneEditText.getText().toString().trim(), choiceTwoEditText.getText().toString().trim(), choiceThreeEditText.getText().toString().trim(), choiceFourEditText.getText().toString().trim(), false,questionAnswer);
                                        questionModelList.add(questionModel);
                                    }
                                    else{
                                        String imagePath = "Quiz/Images/" + qId + "/" + questionCount + ".jpg";

                                        storageReference.child(imagePath).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                StorageReference newReference = FirebaseStorage.getInstance().getReference(imagePath);
                                                newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {

                                                        imageUrl = uri + "";

                                                        QuestionModel questionModel = new QuestionModel(questionEditText.getText().toString().trim(), choiceOneEditText.getText().toString().trim(), choiceTwoEditText.getText().toString().trim(), choiceThreeEditText.getText().toString().trim(), choiceFourEditText.getText().toString().trim(), false,questionAnswer);
                                                        questionModelList.add(questionModel);

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(CreateMenuActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(CreateMenuActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    //------------------------------
     */

}