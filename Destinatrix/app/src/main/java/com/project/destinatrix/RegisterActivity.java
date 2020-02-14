package com.project.destinatrix;

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

public class RegisterActivity extends AppCompatActivity {
    EditText emailId, password, userName;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFireBaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFireBaseAuth = FirebaseAuth.getInstance();
        userName = findViewById(R.id.userName_editText_register);
        emailId = findViewById(R.id.email_editText_register);
        password = findViewById(R.id.password_editText_register);
        tvSignIn = findViewById(R.id.registerSignInText);
        btnSignUp = findViewById(R.id.register_button_register);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr = userName.getText().toString();
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if (email.isEmpty() && pwd.isEmpty() && usr.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Fields Are Empty!", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()) { // missing email
                    emailId.setError("Please enter an email address");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()){ // missing password
                    password.setError("Please enter a password");
                    password.requestFocus();
                }
                else if(usr.isEmpty()){ //missing user name
                    userName.setError("Please enter user name");
                    userName.requestFocus();
                }
                else if (!email.isEmpty() && !pwd.isEmpty() && !usr.isEmpty()){ // user name, email and password provided
                    mFireBaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, getString(R.string.signUpFailMessage), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

    }
}
