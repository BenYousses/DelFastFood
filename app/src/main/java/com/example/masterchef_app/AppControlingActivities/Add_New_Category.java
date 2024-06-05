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
import android.widget.Toast;

import com.example.masterchef_app.Adapters.EditCategoryAdapter;
import com.example.masterchef_app.OBJs.Category;
import com.example.masterchef_app.databinding.ActivityAddNewCategoryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class Add_New_Category extends AppCompatActivity {
private ActivityAddNewCategoryBinding binding;
private ActivityResultLauncher<Intent> arl;
private Uri categoryImage ;
Category category ;

private EditCategoryAdapter editCategoryAdapter ;
private int GET_CATEGORY_IMG_REQ_CODE = 2 ;
private String categoryName ;
private boolean categoryNameIsReady = false ;
public static   DatabaseReference categoryRef;
private     StorageReference categoryStorgeRef ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAddNewCategoryBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
    FirebaseDatabase maseterChefDataBase = FirebaseDatabase.getInstance();
  categoryRef = maseterChefDataBase.getReference("Categories");
     categoryStorgeRef = FirebaseStorage.getInstance().getReference("CtegoryImages");

getCategoryImage();
getInputCategory();
saveCategoryData();
editCategoryRvSetting();
onBtnBackPressed();
editCategory();
    }

    private void editCategory() {
        Intent i = getIntent();
        category = (Category) i.getSerializableExtra("editCategory");
         if (category != null){
             binding.categoryName.setText(category.getCategoryName());
             binding.newCategoryText.setText("UpData Category");
             binding.categoryImage.setVisibility(View.GONE);

         }
    }

    private void onBtnBackPressed() {
        binding.addNewCatGoBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void editCategoryRvSetting() {
        // intialization new Category Adapter
        editCategoryAdapter = new EditCategoryAdapter();
        // access all Categories Under "categoryRef"
        categoryRef.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              ArrayList<Category> editCategoryList = new ArrayList<>();
              for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                  Category category = dataSnapshot1.getValue(Category.class);
                  editCategoryList.add(category);
              }
              editCategoryAdapter.setCategoryList(editCategoryList);
          }
          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {
              Toast.makeText(Add_New_Category.this, "There Is a wrong", Toast.LENGTH_SHORT).show();
          }
      });
binding.editCategoryRv.setAdapter(editCategoryAdapter);
binding.editCategoryRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
binding.editCategoryRv.setHasFixedSize(true);
    }
// save Category Operation
    private void saveCategoryData() {
    binding.saveCategoryBtns.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (category != null ){
                // UpDate Category
                if (categoryNameIsReady){
// access to Categories Refrence
                    DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Categories");
// Initialization  a new Category with a new InFormation
                    category = new Category(categoryName,category.getCategoryImage(),category.getCvategoryKey());
// save new Category in same place of old Category for To guarantee UpDate of Category
                    categoryRef.child(category.getCvategoryKey()).setValue(category);
                }

            }else{
                if (categoryImage != null && categoryNameIsReady){
                    // Initialization a new of Strage Reference for save CtaegoryImage
                    final StorageReference fileRef =categoryStorgeRef.child(System.currentTimeMillis() + "."+getCategoryFileExtension(categoryImage));
                   // save Category Image in Strage under path "fileRef"
                    fileRef.putFile(categoryImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //DonLoad the same categoryImage into Storage
                            fileRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    binding.addNewCategoryProgress.setVisibility(View.GONE);
                                    Uri downloadUri = task.getResult();
                                    Toast.makeText(Add_New_Category.this, "Add Category is Successfully", Toast.LENGTH_SHORT).show();
                                   // make a unique Key of a new Category
                                    String CtaegoryKey = categoryRef.push().getKey();
                                    // intialization a new Category
                                    Category category = new Category(categoryName,String.valueOf(downloadUri),CtaegoryKey);
                                    // save this Category by Key
                                    categoryRef.child(CtaegoryKey).setValue(category);
                                    binding.addingCategoryProgress.setVisibility(View.GONE);
                                    binding.saveCategoryBtns.setText("Save");
                                    binding.addNewCategoryProgress.setEnabled(true);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Add_New_Category.this, "DownLoad Category Image is Failure", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            // get a rate of Progress of UpLoading CategoryImage to Storage
                            double progress = (taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                            binding.addNewCategoryProgress.setVisibility(View.VISIBLE);
                            binding.addNewCategoryProgress.setProgress((int)progress);
                            binding.addingCategoryProgress.setVisibility(View.VISIBLE);
                            binding.saveCategoryBtns.setText("Adding...");
                            binding.addNewCategoryProgress.setEnabled(false);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Add_New_Category.this, "UpLoad Category Image is Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    if (categoryImage == null){
                        Toast.makeText(Add_New_Category.this, "Category Image mostn't be empty !", Toast.LENGTH_SHORT).show();
                    }else if(!categoryNameIsReady){
                        binding.categoryNameError.setText("Category name mostn't be Empty !");
                        binding.categoryNameError.setVisibility(View.VISIBLE);
                    }

                }
            }

        }
    });
    }

    private void getInputCategory() {
    binding.categoryName.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

                categoryName = binding.categoryName.getText().toString();
                if (TextUtils.isEmpty(categoryName)){
                    binding.categoryNameError.setText("Category name mostn't be Empty !");
                    binding.categoryNameError.setVisibility(View.VISIBLE);
                    categoryNameIsReady = false ;
                }else{
                    binding.categoryNameError.setVisibility(View.INVISIBLE);
                    categoryNameIsReady = true ;
            }
        }
    });
    }

    //get Category Image
    private void getCategoryImage() {
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
               if(result.getResultCode() == RESULT_OK && result.getData() != null){
                   categoryImage = result.getData().getData();
                   binding.categoryImage.setImageURI(categoryImage);
               }
            }
        });
binding.categoryImage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // take Permission to access Photoes App
        if (ContextCompat.checkSelfPermission(getBaseContext() , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Add_New_Category.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },GET_CATEGORY_IMG_REQ_CODE);
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
      if (requestCode == GET_CATEGORY_IMG_REQ_CODE){
          if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
arl.launch(intent);
          }
      }
    }

    // get Photo Extention by Imageuri
public  String getCategoryFileExtension(Uri categoryImageUri){
    ContentResolver cr = getContentResolver();
    MimeTypeMap mtm = MimeTypeMap.getSingleton();
    return mtm.getExtensionFromMimeType(cr.getType(categoryImageUri));
}
}