package com.example.stepup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email, password;
    private Button btnRegister;
    private TextView textLogin;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://stepup-56ccc-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        btnRegister = findViewById(R.id.register);
        textLogin = findViewById(R.id.text_login);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void Register(){
        String user = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if(user.isEmpty()){
            email.setError("E-mail nie może być pusty.");
        }
        if(pass.isEmpty()){
            password.setError("Hasło nie może być puste.");
        }
        else {
//            myRef.push().setValue(user,pass).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//
//                    if(task.isSuccessful())
//                    {
//                        Toast.makeText(RegisterActivity.this, "Użytkownik zarejestrowany.", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
//                    }
//                    else
//                    {
//                        Toast.makeText(RegisterActivity.this, "Rejestracja błędna. Wprowadź poprawne dane.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
            mAuth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(RegisterActivity.this, "Użytkownik zarejestrowany.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "Rejestracja błędna. Wprowadź poprawne dane.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}