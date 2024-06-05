package com.example.masterchef_app.AppControlingActivities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.masterchef_app.Adapters.CategorySpinnerAdapter;
import com.example.masterchef_app.Adapters.EditFoodAdapter;
import com.example.masterchef_app.OBJs.Category;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.R;
import com.example.masterchef_app.UserActivities.MainActivity;
import com.example.masterchef_app.databinding.ActivityAddNewFoodBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Add_New_Food_Activity extends AppCompatActivity {
 private ActivityAddNewFoodBinding binding ;
 private DatabaseReference foodRef ;
 private FirebaseDatabase maseterChefDataBase ;
 private ActivityResultLauncher<Intent> arl ;
 private String categoryFoodKey ;
private Uri foodImage_uri;
private Food food ;
private EditFoodAdapter editFoodAdapter ;
private int GET_FOOD_PIC_REQ_CODE = 3;
 private StorageReference foodStorgeRef;
 private String foodTitle, foodPrice ,foodTax ,foodTime,foodDelSer,foodCalories,foodDescription;
 private boolean foodTitleIsReady ,foodTimeIsReady,foodPriceIsReady ,foodTaxIsReady ,foodDelSerIsReady,foodCaloriesIsReady,foodDescriptionIsReady = false;
 private CategorySpinnerAdapter categorySpinnerAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAddNewFoodBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
         maseterChefDataBase = FirebaseDatabase.getInstance();
        foodRef = maseterChefDataBase.getReference("Foods");
       foodStorgeRef = FirebaseStorage.getInstance().getReference("FoodImages");

getFoodImage();
getFoodsInputs();
saveFoodData();
editFoodRvSettings();
CtegorySpinnerSetUp();
 onBtnBackPressed();
 editFood();
    }

    private void editFood() {
        Intent i = getIntent();
        food = (Food) i.getSerializableExtra("editFood");
        if (food != null ){
            binding.foodTitle.setText(food.getTitle());
            binding.foodPrice.setText(food.getPrice());
            binding.foodTax.setText(food.getTax());
            binding.foodDelService.setText(food.getDelSer());
            binding.foodCalories.setText(food.getCalories());
            binding.foodTime.setText(food.getTime());
            binding.foodDescription.setText(food.getDescription());
            binding.categorySpinner.setVisibility(View.INVISIBLE);
            binding.newFoodPic.setVisibility(View.GONE);
            binding.newFoodText.setText("UpDate Food");
            binding.categoyTypeText.setVisibility(View.INVISIBLE);
        }

    }

    private void onBtnBackPressed() {
        binding.goBackFoodBtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void CtegorySpinnerSetUp() {

        categorySpinnerAdapter = new CategorySpinnerAdapter(getBaseContext(), R.layout.spinner_view_holder);
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("Categories");

        categoriesRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 ArrayList<Category> spinnerCategoryList = new ArrayList<>();
                 for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()){
                     Category category = categorySnapshot.getValue(Category.class);
                     spinnerCategoryList.add(category);
                 }
                 categorySpinnerAdapter.setCatrgoryList(spinnerCategoryList);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
         binding.categorySpinner.setAdapter(categorySpinnerAdapter);
binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Category category =  categorySpinnerAdapter.getCategoryByIndex(position);
categoryFoodKey = category.getCvategoryKey() ;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});
    }

    private void editFoodRvSettings() {
      editFoodAdapter = new EditFoodAdapter();
      foodRef.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              ArrayList<Food> foodList = new ArrayList<>();
              for (DataSnapshot foodsDataSnapshot : dataSnapshot.getChildren()){
                  Food food = foodsDataSnapshot.getValue(Food.class);
                  foodList.add(food);
              }
              editFoodAdapter.setFoodsList(foodList);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });
      binding.editFoodRv.setAdapter(editFoodAdapter);
      binding.editFoodRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
binding.editFoodRv.setHasFixedSize(true);
    }
    private void saveFoodData() {
        binding.saveNewFoodBtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(food != null){
                    if (foodTitleIsReady &&foodPriceIsReady &&foodTimeIsReady  &&foodTaxIsReady &&foodDelSerIsReady&&foodCaloriesIsReady && foodDescriptionIsReady){

                        DatabaseReference foodRef =FirebaseDatabase.getInstance().getReference("Foods");
                        food = new Food(foodTitle,food.getPic(),foodPrice,foodTax,foodCalories,foodDelSer,foodDescription,food.getNumberCards(),food.getFoodKey(),food.getCategoryKey(),foodTime,food.getLikesNumbers());
                        foodRef.child(food.getFoodKey()).setValue(food);
                        finish();
                    }else{
                        Toast.makeText(Add_New_Food_Activity.this, "Please Full Food Information", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    if (foodTitleIsReady &&foodPriceIsReady &&foodTimeIsReady  &&foodTaxIsReady &&foodDelSerIsReady&&foodCaloriesIsReady && foodDescriptionIsReady && foodImage_uri!= null){

                        final StorageReference fileRef = foodStorgeRef.child(System.currentTimeMillis() + "." + getFoodFileExtension(foodImage_uri));
                        fileRef.putFile(foodImage_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(Add_New_Food_Activity.this, "UpLoading...", Toast.LENGTH_SHORT).show();
                                fileRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        binding.foodProgressBar.setVisibility(View.INVISIBLE);
                                        String foodKey = foodRef.push().getKey();
                                        String foodImageUri = task.getResult().toString();
                                        Food food = new Food(foodTitle,foodImageUri,foodPrice,foodPrice,foodCalories,foodDelSer,foodDescription,0,foodKey,categoryFoodKey,foodTime,0);
                                        foodRef.child(foodKey).setValue(food);
                                        Toast.makeText(Add_New_Food_Activity.this, "Food Added SuccessFully", Toast.LENGTH_SHORT).show();
                                        binding.foodProgressBar.setVisibility(View.INVISIBLE);
                                        binding.addingFoodProgress.setVisibility(View.GONE);
                                        binding.saveNewFoodBtns.setText("Save");
                                        binding.saveNewFoodBtns.setEnabled(true);
                                        finish();

                                    }
                                });
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                double foodProgress =(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                                binding.foodProgressBar.setProgress((int)foodProgress);
                                binding.foodProgressBar.setVisibility(View.VISIBLE);
                                binding.addingFoodProgress.setVisibility(View.VISIBLE);
                                binding.saveNewFoodBtns.setText("Adding...");
                                binding.saveNewFoodBtns.setEnabled(false);
                            }
                        });
                    }

                else{
                    Toast.makeText(Add_New_Food_Activity.this, "Please full all Information", Toast.LENGTH_SHORT).show();
                }

                }


            }
        });
    }
    private void getFoodsInputs() {
        binding.foodTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
foodTitle = binding.foodTitle.getText().toString();
if(TextUtils.isEmpty(foodTitle)){
    binding.foodTitleError.setText("food title mostn't be empty !");
    binding.foodTitleError.setVisibility(View.VISIBLE);
    foodTitleIsReady = false;
}else{
    binding.foodTitleError.setVisibility(View.INVISIBLE);
    foodTitleIsReady = true;
}

            }
        });
        binding.foodPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                foodPrice = binding.foodPrice.getText().toString();
                if(TextUtils.isEmpty(foodPrice)){
                    binding.foodPriceError.setText("food price mostn't be empty !");
                    binding.foodPriceError.setVisibility(View.VISIBLE);
                    foodPriceIsReady = false;
                }else{
                    binding.foodPriceError.setVisibility(View.INVISIBLE);
                    foodPriceIsReady = true;
                }

            }
        });
        binding.foodTax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                foodTax = binding.foodTax.getText().toString();
                if(TextUtils.isEmpty(foodTax)){
                    binding.foodTaxError.setText("food tax mostn't be empty !");
                    binding.foodTaxError.setVisibility(View.VISIBLE);
                    foodTaxIsReady = false;
                }else{
                    binding.foodTaxError.setVisibility(View.INVISIBLE);
                    foodTaxIsReady = true;
                }
            }
        });
        binding.foodDelService.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                foodDelSer = binding.foodDelService.getText().toString();
                if(TextUtils.isEmpty(foodDelSer)){
                    binding.foodDelSerError.setText("delevry Service mostn't be empty !");
                    binding.foodDelSerError.setVisibility(View.VISIBLE);
                    foodDelSerIsReady = false;

                }else{
                    binding.foodDelSerError.setVisibility(View.INVISIBLE);
                    foodDelSerIsReady = true;
                }

            }
        });
        binding.foodCalories.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                foodCalories = binding.foodCalories.getText().toString();
                if(TextUtils.isEmpty(foodCalories)){
                    binding.foodCaloriesError.setText("calories mostn't be empty !");
                    binding.foodCaloriesError.setVisibility(View.VISIBLE);
                    foodCaloriesIsReady = false;
                }else{
                    binding.foodCaloriesError.setVisibility(View.INVISIBLE);
                    foodCaloriesIsReady = true;
                }

            }
        });
        binding.foodDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                foodDescription = binding.foodDescription.getText().toString();
                if(TextUtils.isEmpty(foodDescription)){
                    binding.foodDescriptionError.setText("calories mostn't be empty !");
                    binding.foodDescriptionError.setVisibility(View.VISIBLE);
                    foodDescriptionIsReady = false;

                }else{
                    binding.foodDescriptionError.setVisibility(View.INVISIBLE);
                    foodDescriptionIsReady = true;
                }

            }
        });
        binding.foodTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                foodTime = binding.foodTime.getText().toString();
                if(TextUtils.isEmpty(foodTime)){
                    binding.foodTimeError.setText("time mostn't be empty !");
                    binding.foodTimeError.setVisibility(View.VISIBLE);
                    foodTimeIsReady = false;

                }else{
                    binding.foodTimeError.setVisibility(View.INVISIBLE);
                    foodTimeIsReady = true;
                }

            }
        });


    }
// get Food Image
    private void getFoodImage() {
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
           if(result.getResultCode() == RESULT_OK && result.getData() != null){
               foodImage_uri = result.getData().getData();
               binding.newFoodPic.setImageURI(foodImage_uri);
           }
            }
        });
        binding.newFoodPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getPermission toi access  a User Images App
                if(ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Add_New_Food_Activity.this,new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },GET_FOOD_PIC_REQ_CODE);
                }else{
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    arl.launch(intent);
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_FOOD_PIC_REQ_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                arl.launch(intent);
            }
        }
    }

    // get Food Image Extention
    public  String getFoodFileExtension(Uri categoryImageUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mtm = MimeTypeMap.getSingleton();
        return mtm.getExtensionFromMimeType(cr.getType(categoryImageUri));
    }
}