package com.example.masterchef_app.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.masterchef_app.AppControlingActivities.Control_Main_Activity;
import com.example.masterchef_app.OBJs.User;
import com.example.masterchef_app.R;
import com.example.masterchef_app.databinding.ActivitySplachBinding;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class splach_Activity extends AppCompatActivity {
    private Handler handler;
    private SharedPreferences masterChefSP ;
    String userString;
    ActivitySplachBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySplachBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
                 masterChefSP = getSharedPreferences("AlreadyRun",MODE_PRIVATE);
        boolean  alreadyRun = masterChefSP.getBoolean("AlreadyRun",true);
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();

            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }
        if(isWifiConn == false && isMobileConn == false){
            Toast.makeText(this, "Check Your Connection And Try Again !", Toast.LENGTH_SHORT).show();
        }else{
            if (alreadyRun){
                // go To Logain Activity for get CurrentUser
                goToLogain();

                // change value of "AlreadyRun" because now User Is Already Logained
                SharedPreferences.Editor editor = masterChefSP.edit();
                editor.putBoolean("AlreadyRun",false);
                editor.apply();
            }else{
// I get User into sharedPrefrense As String OBJ
                userString = masterChefSP.getString("UserString",null);
                if (userString != null){
                    // here we Chage UserString To User As Obj

                    getCurrentyUser();

                }else{
                    // go To Logain Activity for get CurrentUser if usrString Is Empty
                    goToLogain();

                }
            }
        }



    }

    private void goToLogain() {
        Intent intent = new Intent(getBaseContext(), Logain_Activity.class);
        startActivity(intent);
        finish();
    }

    private void getCurrentyUser() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("MasterChefDataBase").child("Users");
        Gson gson = new Gson();
        User user = gson.fromJson(userString, User.class);
        usersRef.child(user.getUserKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
if (dataSnapshot.exists()){
        User user1 = dataSnapshot.getValue(User.class);
        if (user.getUserKey().equals("B7uIdtC8TbZP2X0M5wmm6nT2y3F3")){
            Intent intent = new Intent (getBaseContext(), Control_Main_Activity.class);
            intent.putExtra("support",user1);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent (getBaseContext(), MainActivity.class);
            intent.putExtra("User",user);
            Toast.makeText(getBaseContext(), "Welcom !", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
    }
}else{
    goToLogain();
}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                goToLogain();
            }
        });


    }


}