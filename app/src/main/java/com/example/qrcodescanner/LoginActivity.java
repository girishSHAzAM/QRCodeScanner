package com.example.qrcodescanner;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
EditText email,password;
Button login;
FirebaseAuth mAuth;
String mEmail,mPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.authButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new firebaseAsync().execute();
            }
        });
    }
    private class firebaseAsync extends AsyncTask<Void,Void,Integer>{
        int state;
        @Override
        protected void onPreExecute() {
            mAuth = FirebaseAuth.getInstance();
            email = (EditText) findViewById(R.id.emailText);
            password = (EditText) findViewById(R.id.passwordText);
            mEmail = email.getText().toString();
            mPassword = password.getText().toString();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            state =0;
            mAuth.createUserWithEmailAndPassword(mEmail,mPassword)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                               state = 1;
                            }
                            else{
                               state = 0;
                            }
                        }
                    });
            return state;
        }

        @Override
        protected void onPostExecute(Integer s) {
            if(s == 1){
                Toast.makeText(LoginActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
            }
            else if(s == 0){
                Toast.makeText(LoginActivity.this,"Registration Unsuccessful",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
