package com.project.destinatrix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class RegisterActivity extends AppCompatActivity {
    EditText emailId, password, userName;
    Button btnSignUp, btnGoogleSignIn;
    TextView tvSignIn;//,googleSignIn;
    FirebaseAuth mFireBaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference databaseUsers;
    static final int GOOGLE_SIGN = 123;

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
        btnGoogleSignIn = findViewById(R.id.google_button_register);
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser(); // to call addUser method when clicking on the register button
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
                    mFireBaseAuth.createUserWithEmailAndPassword(email,pwd)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
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

        btnGoogleSignIn.setOnClickListener(v -> signInGoogle());

    }

    private void signInGoogle() {
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN){
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null) firebaseAuthWithGoogle(account);
                    //make request firebase

            }catch(ApiException e){
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        Log.d("TAG", "FirebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFireBaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        Log.d("TAG", "signin success");

                        FirebaseUser user = mFireBaseAuth.getCurrentUser();
                       // updateUI(user);
                    }

                    else{
                        Log.w("TAG", "signin failure", task.getException());

                        Toast.makeText(this, "Signin Failed!", Toast.LENGTH_SHORT).show();
                       // updateUI(null);
                    }
                });
    }

    private void addUser(){
        String name = userName.getText().toString().trim();
        String emailAddress = emailId.getText().toString().trim();

        if(!TextUtils.isEmpty(name)){

            String id = databaseUsers.push().getKey();

            User user = new User(name, emailAddress, id);

            databaseUsers.child(id).setValue(user);

            Toast.makeText(this, "User added", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
}
