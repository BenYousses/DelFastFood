package com.example.masterchef_app.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.masterchef_app.AppControlingActivities.Control_Main_Activity;
import com.example.masterchef_app.OBJs.User;
import com.example.masterchef_app.databinding.ActivityLogainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class Logain_Activity extends AppCompatActivity {
private ActivityLogainBinding binding;
private String email ;
private String password ;
private boolean emailIsReady = false ;
private boolean passwordIsReady = false ;
 private FirebaseAuth masterChefAuth ;
  private   DatabaseReference masterChefRef ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLogainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());


         masterChefAuth = FirebaseAuth.getInstance();
        userLogainInputsSetUp();
        logainSetUp();
        FirebaseDatabase masterChefDB = FirebaseDatabase.getInstance();
        masterChefRef = masterChefDB.getReference("MasterChefDataBase");

    }

    // i try Here To Logain User
    private void logainSetUp() {
        binding.logainAccontBtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if (emailIsReady && passwordIsReady){
    LogainNotAvaible();
  // I check if User Is aviabel usng (email, password)
    masterChefAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            // user Founded;
            if (task.isSuccessful()) {
                // make unique Key To CurrentUser
                String userId = masterChefAuth.getCurrentUser().getUid();
                // "Below"
                retcieveUserData(userId);
                // "Below"
                LogainAvaible();

            } else {
                // "Below"
                LogainAvaible();
            }
        }
    });

}else{
    // If any User Information Input is Empty ;
    Toast.makeText(Logain_Activity.this, "Please Check your Informations", Toast.LENGTH_SHORT).show();
}
            }
        });
    }


// All Of Action  if Logain Operation Stoped
    private void LogainAvaible() {
        binding.logainAccontBtns.setText("Logain");
        binding.toCreateAccountTxt.setVisibility(View.VISIBLE);
        binding.dontHaveAccount.setVisibility(View.VISIBLE);
        binding.logainAccontBtns.setEnabled(true);
        binding.logainProgreass.setVisibility(View.GONE);
    }

// All Of Action if Logain Operation it's Starting
    private void LogainNotAvaible() {
        binding.logainAccontBtns.setText("Logain...");
        binding.toCreateAccountTxt.setVisibility(View.GONE);
        binding.dontHaveAccount.setVisibility(View.GONE);
        binding.logainAccontBtns.setEnabled(false);
        binding.logainProgreass.setVisibility(View.VISIBLE);
    }

    // I get User Inforamtion into Inputs"EDIT TEXT"
    private void userLogainInputsSetUp() {
        binding.logainEmailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
email= binding.logainEmailEt.getText().toString();
if (TextUtils.isEmpty(email)){
binding.logainEmailError.setVisibility(View.VISIBLE);
binding.logainEmailError.setText("Email mostn't be empty !");
    emailIsReady = false ;
}else if (!email.endsWith("@gmail.com")){
    binding.logainEmailError.setVisibility(View.VISIBLE);
    binding.logainEmailError.setText("Email most end with (@gmail.com)!");
    emailIsReady = false ;

}else {
    binding.logainEmailError.setVisibility(View.INVISIBLE);
    emailIsReady = true;
}

            }
        });
        binding.logainPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
password = binding.logainPasswordEt.getText().toString();
if (TextUtils.isEmpty(password)){
binding.logainPasswordError.setVisibility(View.VISIBLE);
binding.logainPasswordError.setText("Password mostn't be empty !");
    passwordIsReady = false ;
}else {
    binding.logainPasswordError.setVisibility(View.INVISIBLE);
    passwordIsReady = true ;
}
            }
        });
        binding.toCreateAccountTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),Create_Account_Activity.class);
                startActivity(intent);
            }
        });
    }

    // return  User Data In FireBaseDataBase by "userId"
    private void retcieveUserData(String userId) {
        //Reference of Users into FireBaseDataBase
        DatabaseReference userRef = masterChefRef.child("Users").child(userId);
        //return User Data
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get the Returned User
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    Toast.makeText(Logain_Activity.this,user.getName(), Toast.LENGTH_SHORT).show();

                    saveCurrentUser(user);
                } else {
                    //make Toast if user is not founded
                    Toast.makeText(getBaseContext(), "User not found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //make Toast there is a wrong in recieving User Data
                Toast.makeText(getBaseContext(), "Failed to retcieve user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // save current User Into SharedPrefrense after get CurrentUser and Go To MainActivity
    private void saveCurrentUser(User user) {
        //make a Gson Obj for convert User As Obj To User As String
        Gson gson = new Gson();
        //CurrentUser Refernce into sharedPrefrence
         SharedPreferences masterChefSP = getSharedPreferences("AlreadyRun",MODE_PRIVATE);
         // convert User As Object to User As String
        String userString = gson.toJson(user);
        //save Current StringUser into sharedPrefrence key"UserString"
        SharedPreferences.Editor editor = masterChefSP.edit();
        editor.putString("UserString",userString);
        editor.apply();
        if (user.getUserKey().equals("B7uIdtC8TbZP2X0M5wmm6nT2y3F3")){
            Intent intent = new Intent(getBaseContext() , Control_Main_Activity.class);
            intent.putExtra("User" , user);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(getBaseContext() , MainActivity.class);
            intent.putExtra("User" , user);
            startActivity(intent);
            finish();
        }

    }


}