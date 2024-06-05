package com.example.masterchef_app.AppControlingActivities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.masterchef_app.OBJs.BunnerModel;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.R;
import com.example.masterchef_app.databinding.ActivityRelatedBunnerFoodBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Related_Bunner_Food extends AppCompatActivity {
ActivityRelatedBunnerFoodBinding binding;
    private boolean foodTitleIsReady , foodPriceIsReady  , foodTaxIsReady ,foodDelSerIsReady ,foodCaloriesIsReady , foodTimeIsReady , foodDescriptionIsReady = false ;
    String foodTitle , foodPrice , foodTax,foodDelSer ,foodCalories , foodTime , foodDescription ;
    private ActivityResultLauncher<Intent> foodArl ;
    private Uri foodPic ;
    private String foodPicString ;
    private String bunnerPicString ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
binding = ActivityRelatedBunnerFoodBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        inPutSettingd();
        onFoodImageClick();
        getFoodImg();
        onSaveBtnClicked();
    }

    private void onSaveBtnClicked() {
        binding.saveFoodBunnerBtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBunnerFood();
                if (foodTitleIsReady && foodPriceIsReady&& foodTaxIsReady&& foodDelSerIsReady&& foodCaloriesIsReady&& foodTimeIsReady&& foodDescriptionIsReady && bunnerPicString!= null ){
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference("BunnerImages");

                    upLoadbunnerFoodImage(storageRef,foodPic);
                }
            }
        });
    }

    private void getBunnerFood() {
        Intent i = getIntent();
        bunnerPicString = i.getStringExtra("bunnerFood");

    }

    private void inPutSettingd() {
        binding.foodBunnerTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                foodTitle = binding.foodBunnerTitle.getText().toString();
                if(TextUtils.isEmpty(foodTitle)){
                    binding.foodTitleBunnerError.setText("Title can't be empty!");
                    binding.foodTitleBunnerError.setVisibility(View.VISIBLE);
                    foodTitleIsReady = false ;
                }else{
                    binding.foodTitleBunnerError.setVisibility(View.GONE);
                    foodTitleIsReady = true ;
                }
            }
        });
        binding.foodBunnerPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                foodPrice = binding.foodBunnerPrice.getText().toString();
                if (TextUtils.isEmpty(foodPrice)){
                    binding.foodPriceBunnerError.setText("Price can't be empty!");
                    binding.foodPriceBunnerError.setVisibility(View.VISIBLE);
                    foodPriceIsReady = false ;
                }else{
                    binding.foodPriceBunnerError.setVisibility(View.GONE);
                    foodPriceIsReady = true ;
                }
            }
        });
        binding.foodBunnerTax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                foodTax = binding.foodBunnerTax.getText().toString();
                if (TextUtils.isEmpty(foodTax)){
                    binding.foodTaxBunnerError.setText("Tax can't be empty!");
                    binding.foodTaxBunnerError.setVisibility(View.VISIBLE);
                    foodTaxIsReady = false ;
                }else{
                    binding.foodTaxBunnerError.setVisibility(View.GONE);
                    foodTaxIsReady = true ;
                }
            }
        });
        binding.foodBunnerDelService.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                foodDelSer = binding.foodBunnerDelService.getText().toString();
                if (TextUtils.isEmpty(foodDelSer)){
                    binding.foodBunnerDelSerError.setText("Del Ser can't be empty!");
                    binding.foodBunnerDelSerError.setVisibility(View.VISIBLE);
                    foodDelSerIsReady= false ;
                }else{
                    binding.foodBunnerDelSerError.setVisibility(View.GONE);
                    foodDelSerIsReady = true ;
                }
            }
        });
        binding.foodBunnerCalories.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                foodCalories = binding.foodBunnerCalories.getText().toString();
                if (TextUtils.isEmpty(foodCalories)){
                    binding.foodCaloriesBunnerError.setText("Calories can't be empty!");
                    binding.foodCaloriesBunnerError.setVisibility(View.VISIBLE);
                    foodCaloriesIsReady= false ;
                }else{
                    binding.foodCaloriesBunnerError.setVisibility(View.GONE);
                    foodCaloriesIsReady = true ;
                }
            }
        });
        binding.foodBunnerTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                foodTime = binding.foodBunnerTime.getText().toString();
                if (TextUtils.isEmpty(foodTime)){
                    binding.foodTimeError.setText("Time can't be empty!");
                    binding.foodTimeError.setVisibility(View.VISIBLE);
                    foodTimeIsReady= false ;
                }else{
                    binding.foodTimeError.setVisibility(View.GONE);
                    foodTimeIsReady = true ;
                }
            }
        });
        binding.foodBunnerDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                foodDescription = binding.foodBunnerDescription.getText().toString();
                if (TextUtils.isEmpty(foodDescription)){
                    binding.foodDescriptionError.setText("Description can't be empty!");
                    binding.foodDescriptionError.setVisibility(View.VISIBLE);
                    foodDescriptionIsReady= false ;
                }else{
                    binding.foodDescriptionError.setVisibility(View.GONE);
                    foodDescriptionIsReady = true ;
                }
            }
        });
    }

    private void onFoodImageClick() {
        binding.newFoodBunnerPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                foodArl.launch(intent);
            }
        });

    }

    private void getFoodImg() {

        foodArl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null){
                    foodPic = result.getData().getData();
                    binding.newFoodBunnerPic.setImageURI(foodPic);
                }
            }
        });

    }
    private void upLoadbunnerFoodImage(StorageReference storageRef , Uri imageUri) {
        final StorageReference fileFoodRef = storageRef.child(System.currentTimeMillis() + "." + getFoodFileExtension(imageUri));
        fileFoodRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                fileFoodRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String stringPic = task.getResult().toString();
                        foodPicString = stringPic ;
                        if (foodPicString != null){
                            binding.addingRelatedFoodProgress.setVisibility(View.GONE);
                            binding.relatedBunnerFoodProgress.setVisibility(View.VISIBLE);

                         saveBunnerFirebase();
                        }
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                binding.addingRelatedFoodProgress.setProgress((int)progress);
                binding.addingRelatedFoodProgress.setVisibility(View.VISIBLE);
                binding.saveFoodBunnerBtns.setText("Saving...");
            }
        });

    }

    private void saveBunnerFirebase() {
        DatabaseReference bunnerRef = FirebaseDatabase.getInstance().getReference("Bunner");
       String bunnerKey = bunnerRef.push().getKey();
        Food food1 = new Food(foodTitle,foodPicString,foodPrice,foodTax,foodCalories,foodDelSer,foodDescription,0,foodTime);
        BunnerModel bunner1 = new BunnerModel(bunnerPicString,bunnerKey,food1);
        bunnerRef.child(bunnerKey).setValue(bunner1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    binding.relatedBunnerFoodProgress.setVisibility(View.GONE);
                    binding.saveFoodBunnerBtns.setText("Save");
                    Toast.makeText(Related_Bunner_Food.this, "Successfull", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    binding.relatedBunnerFoodProgress.setVisibility(View.GONE);
                    binding.saveFoodBunnerBtns.setText("SAVE");

                }}
        });
    }

    public  String getFoodFileExtension(Uri categoryImageUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mtm = MimeTypeMap.getSingleton();
        return mtm.getExtensionFromMimeType(cr.getType(categoryImageUri));
    }

}