package com.example.masterchef_app.UserActivities;

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

import com.example.masterchef_app.OBJs.User;
import com.example.masterchef_app.databinding.ActivityCreateAccountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Pattern;

public class Create_Account_Activity extends AppCompatActivity {
ActivityCreateAccountBinding binding ;
private String fname ;
private String email ;
    private String password ;
   private String conPasword ;
    private Uri user_imageUri ;
    int REQ_PERMISSION_CODE = 1;
    private boolean fnameIsReady = false ;
    private boolean emailIsReady = false ;
    private boolean passwordIsReady = false ;
    private boolean conPasswordIsReady = false ;
    private ActivityResultLauncher<Intent> arl ;
   private FirebaseDatabase database ;
    private DatabaseReference masterChefRef ;
    private FirebaseAuth masterChefAuth ;
    private StorageReference masterChefStorageRef ;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
     database = FirebaseDatabase.getInstance();
 masterChefAuth = FirebaseAuth.getInstance();
masterChefRef = database.getReference("MasterChefDataBase");
    masterChefStorageRef = FirebaseStorage.getInstance().getReference("UserImages");
        userInputSetUp();
        userProfileSetUp();
        userDataStore();
}
    private void userDataStore() {
    binding.createAccountBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (fnameIsReady && emailIsReady && passwordIsReady && conPasswordIsReady && user_imageUri != null) {
                final StorageReference fileReference = masterChefStorageRef.child(System.currentTimeMillis()
                        + "." + getFileExtension(user_imageUri));

                fileReference.putFile(user_imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> uriTask) {
                                masterChefAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            Uri downloadUri = uriTask.getResult();
                                            String userKey = masterChefAuth.getCurrentUser().getUid();
                                            User user = new User(fname , email,password ,String.valueOf(downloadUri),userKey);
                                            masterChefRef.child("Users").child(userKey).setValue(user);
                                            Toast.makeText(Create_Account_Activity.this, "Create Account is Successfully", Toast.LENGTH_SHORT).show();
                                       binding.dontHaveAccount.setVisibility(View.VISIBLE);
                                       binding.toLogainTxt.setVisibility(View.VISIBLE);
                                       binding.createAccountProgreass.setVisibility(View.GONE);
                                       binding.createAccountBtn.setText("Create Account ");
                                            binding.createAccountBtn.setEnabled(true);
                                            Intent intent = new Intent(getBaseContext(), Logain_Activity.class);
                                            startActivity(intent);
                                        }else{
                                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                                String ErrorCodeTxt = ((FirebaseAuthUserCollisionException)task.getException()).getErrorCode();
                                                if(ErrorCodeTxt.equals("ERROR_EMAIL_ALREADY_IN_USE")){
                                                    Toast.makeText(Create_Account_Activity.this, "This email" + "\""+email+"\""  + "already used", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        })

                                .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Create_Account_Activity.this, "there is a wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                double createAccounteProgress =( taskSnapshot.getBytesTransferred() * 100)/taskSnapshot.getTotalByteCount() ;
                                binding.createAccountProgreass.setProgress((int)createAccounteProgress);
                                binding.dontHaveAccount.setVisibility(View.GONE);
                                binding.toLogainTxt.setVisibility(View.GONE);
                                binding.createAccountProgreass.setVisibility(View.VISIBLE);
                                binding.createAccountBtn.setText("Creating Account...");
                                binding.createAccountBtn.setEnabled(false);
                            }
                        })

                        .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Create_Account_Activity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                });



            }else{
                Toast.makeText(Create_Account_Activity.this, "You have A problem", Toast.LENGTH_SHORT).show();
            }
        }
    });
    }
    private void userProfileSetUp() {
       arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
               if (result.getResultCode() == RESULT_OK && result.getData() != null){
                   user_imageUri = result.getData().getData();
                   binding.userProfile.setImageURI(user_imageUri);
               }
            }
        });
    binding.userProfile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Create_Account_Activity.this , new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},REQ_PERMISSION_CODE);
            }else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                arl.launch(intent);
            }
        }
    });
    }

    private void userInputSetUp() {
        binding.createAccountFname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               fname = binding.createAccountFname.getText().toString();
               int fnameLenght = fname.length();
               if (fnameLenght <= 4){
                   binding.fNameError.setText("First name most contains 5 Letters !");
                   binding.fNameError.setVisibility(View.VISIBLE);
                   fnameIsReady = false ;
               } else if (TextUtils.isEmpty(fname)) {
                   binding.fNameError.setText("First name mostn't be Empty");
                   binding.fNameError.setVisibility(View.VISIBLE);
                   fnameIsReady = false ;
               }else{
                   binding.fNameError.setVisibility(View.INVISIBLE);
                   fnameIsReady = true ;
               }

            }
        });
        binding.createAccountEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                email = binding.createAccountEmail.getText().toString();
                if (!email.endsWith("@gmail.com")){
                    binding.emailError.setText("Email most end with (@gmail.com)");
                    binding.emailError.setVisibility(View.VISIBLE);
                    emailIsReady = false ;
                } else if (TextUtils.isEmpty(email)) {
                    binding.emailError.setText("Email name mostn't be Empty");
                    binding.emailError.setVisibility(View.VISIBLE);
                    emailIsReady = false ;
                }else{
                    binding.emailError.setVisibility(View.INVISIBLE);
                    emailIsReady = true;
                }

            }
        });
        binding.createAccountPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password = binding.createAccountPassword.getText().toString();
                String passwordPattern ="^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$";
                int passwordLenght = password.length();
                if (!Pattern.matches(passwordPattern,password)){
                    binding.passwordError.setText("Password most contais(Letters_Nums_Symbols)");
                    binding.passwordError.setVisibility(View.VISIBLE);
                    passwordIsReady = false ;
                } else if (TextUtils.isEmpty(password)) {
                    binding.passwordError.setText("Password name mostn't be Empty");
                    binding.passwordError.setVisibility(View.VISIBLE);
                    passwordIsReady = false ;
                } else if (passwordLenght <= 8) {
                    binding.passwordError.setText("Password most contains at least 7 characters !");
                    binding.passwordError.setVisibility(View.VISIBLE);
                    passwordIsReady = false ;
                }else{
                    binding.passwordError.setVisibility(View.INVISIBLE);
                    passwordIsReady = true;
                }

            }
        });
        binding.createAccountConPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                conPasword = binding.createAccountConPassword.getText().toString();
                if (!conPasword.equals(password)){
                    binding.conPasswordError.setText("The passwords do not match");
                    binding.conPasswordError.setVisibility(View.VISIBLE);
                    conPasswordIsReady = false ;
                }else{
                    binding.conPasswordError.setVisibility(View.INVISIBLE);
                    conPasswordIsReady = true;
                }

            }
        });
        binding.toLogainTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Logain_Activity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQ_PERMISSION_CODE){
            if(grantResults.length > 1 && grantResults[0]  == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                arl.launch(intent);
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}


