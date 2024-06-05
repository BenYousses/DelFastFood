package com.example.masterchef_app.AppControlingActivities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import com.example.masterchef_app.OBJs.BunnerModel;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.databinding.ActivityAddNewUpDateBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Add_New_Bunner extends AppCompatActivity {
ActivityAddNewUpDateBinding binding ;
  private int GET_BUNNER_PIC_REQ_COD = 7 ;
   private ActivityResultLauncher<Intent> bunnerArl ;
   private Uri bunnerPic ;




   private String bunnerPicString ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAddNewUpDateBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        getExternalStrogePermesstion();
        getBunnerImg();
        onBunnerClicked();
        bunnerSetUp();
    }

    private void bunnerSetUp() {
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    saveData();
            }
        });
    }

    // save Bunner Image Operation
    private void saveData() {
        // save bunnerImage after it Ready
        if (bunnerPic != null){
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("BunnerImages");
            upLoadbunnerFoodImage(storageRef , bunnerPic);
        }else {
            Toast.makeText(this, "Please add Bunner Image", Toast.LENGTH_SHORT).show();
        }

    }

// upload Image into Storage Opation afte save it into Storage
    private void upLoadbunnerFoodImage(StorageReference storageRef , Uri imageUri) {
        //get Image ref into Storage
       final StorageReference fileFoodRef = storageRef.child(System.currentTimeMillis() + "." + getFoodFileExtension(imageUri));
       // save the Image into Stroge under Path "fileFoodRef"
        fileFoodRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                // downLoad Samê photoUri  food save it inRealTimeDatabase for be easy to access it
                fileFoodRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        // get imageUri
                        String stringPic = task.getResult().toString();
                             bunnerPicString = stringPic ;
                             Toast.makeText(Add_New_Bunner.this, "bunner Img UpLoaded", Toast.LENGTH_SHORT).show();
                             Toast.makeText(Add_New_Bunner.this, bunnerPicString, Toast.LENGTH_SHORT).show();
                    if (bunnerPicString != null){
                        Intent intent = new Intent(getBaseContext(),Related_Bunner_Food.class);
                        intent.putExtra("bunnerFood",bunnerPicString);
                        startActivity(intent);
                        finish();
                    }
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                binding.addingNewBunnerProgress.setProgress((int)progress);
                binding.addingNewBunnerProgress.setVisibility(View.VISIBLE);
            }
        });

    }




//UpDate Bunner Image Operation
    private void onBunnerClicked() {
        binding.foodBunnerPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                bunnerArl.launch(intent);

            }
        });

    }
    private void getBunnerImg() {
            bunnerArl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK && result.getData() != null){
                bunnerPic = result.getData().getData();
                binding.foodBunnerPic.setImageURI(bunnerPic);
            }
            }
        });
    }
    // take Pemesion for access to User Photos
    private void getExternalStrogePermesstion() {

                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Add_New_Bunner.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},GET_BUNNER_PIC_REQ_COD);
                }
            }

            // method rerturn the  Extention of Images
    public  String getFoodFileExtension(Uri categoryImageUri){
        ContentResolver cr = getContentResolver();
        //get type of File by his Extention
        MimeTypeMap mtm = MimeTypeMap.getSingleton();
        return mtm.getExtensionFromMimeType(cr.getType(categoryImageUri));
    }
    }
