package com.example.stepup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.stepup.databinding.ActivityAccountBinding;
import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends DrawerBaseActivity {
    ActivityAccountBinding activityAccountBinding;

    private FirebaseAuth mAuth;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_account);
        activityAccountBinding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(activityAccountBinding.getRoot());
        allocateActivityTitle("Twoje Konto");

        mAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.btnLogout);
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(AccountActivity.this, LoginActivity.class));
    }
}