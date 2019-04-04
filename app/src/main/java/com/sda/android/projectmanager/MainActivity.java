package com.sda.android.projectmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnSignIn;
    private Button btnSignUp;

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }

        mDialog = new ProgressDialog(this);

        etEmail = findViewById(R.id.email_login);
        etPassword = findViewById(R.id.password_login);

        btnSignIn = findViewById(R.id.btnsignin);
        btnSignUp = findViewById(R.id.btnsignup);

        // for complete login
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                boolean emailAndPasswordNotEmpty = true;

                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Required Field..");
                    emailAndPasswordNotEmpty = false;
                }
                if(TextUtils.isEmpty(password)){
                    etPassword.setError("Required Field..");
                    emailAndPasswordNotEmpty = false;
                }

                if(emailAndPasswordNotEmpty){

                    mDialog.setMessage("Processing..");
                    mDialog.show();

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    mDialog.dismiss();
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                                        Toast.makeText(getApplicationContext(), "Login Complete", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Please, try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

        // for sign up page
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

    }
}
