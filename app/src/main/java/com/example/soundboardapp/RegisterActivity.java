package com.example.soundboardapp;

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
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email;
    private TextInputLayout password, confPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        confPassword = findViewById(R.id.register_confPassword);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView textLogin = findViewById(R.id.text_login);

        // Set onClickListener event for when the Register button is clicked, it will call the Register method.
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        // Set onClickListener event for when the user clicks the TextView area, which will lead them to the login page.
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    /**
     * A method that handles the register functionality.
     */
    private void register() {
        String user = email.getText().toString().trim();
        String pass = password.getEditText().getText().toString().trim();
        String confPass = confPassword.getEditText().getText().toString().trim();

        // Checks if inputs are empty and also checks if password and confPass are equal.
        if (!pass.equals(confPass)){
            Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
        }

        if (user.isEmpty()){
            email.setError("Email must not be empty!");
        }

        if (pass.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Password must not be empty!", Toast.LENGTH_SHORT).show();
//            password.setError("Password must not be empty!");

        }

        if (confPass.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Confirmation password must not be empty!", Toast.LENGTH_SHORT).show();
//            confPassword.setError("Confirmation password must not be empty!");

        }

        // Using Firebase's createUserWithEmailAndPassword function, it allows the user to register an account on the Firebase authentication,
        // which if successful, it will lead to the login page.
        if(!user.isEmpty() && !pass.isEmpty() && !confPass.isEmpty() && pass.equals(confPass)){
            mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        User newUser = new User(user);

                        FirebaseDatabase.getInstance("https://soundboard-8ddb0-default-rtdb.firebaseio.com/")
                                .getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                }
                                else{
                                    Toast.makeText(RegisterActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Registration failed! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
}