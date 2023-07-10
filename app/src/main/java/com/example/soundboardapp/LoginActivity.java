package com.example.soundboardapp;

//import androidx.annotation.NonNull;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email;
    private TextInputLayout password;
    private Button btnSignin;
    private TextView textRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        btnSignin = findViewById(R.id.signin);
        textRegister = findViewById(R.id.text_register);

        // Set onClickListener event for when the Sign in button is clicked, it will call the login method.
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        // Set onClickListener event for when the user clicks the TextView area, which will lead them to register page.
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    /**
     * A method that handles the login functionality.
     */
    private void login() {
        String pass = password.getEditText().getText().toString().trim();
        String user = email.getText().toString().trim();

        // Checks if user and password is empty and sets and error.
        if (user.isEmpty()){
            email.setError("Email must not be empty!");
        }

        if (pass.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Password must not be empty!", Toast.LENGTH_SHORT).show();
//            password.setError("Password must not be empty!");
        }
        // Using Firebase's signInWithEmailAndPassword function, it allows the user to sign in and will redirect to the MainActivity page.
        if(!user.isEmpty() && !pass.isEmpty()){
            mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NotNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else{
                        email.setError(task.getException().getMessage());
                        //Toast.makeText(LoginActivity.this, "Sign in failed! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}