package com.example.myaudioplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class Register extends AppCompatActivity {
    EditText email_ad, password;
    Button btn_reg;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFirebaseAuth = FirebaseAuth.getInstance();
        email_ad = findViewById(R.id.EmailAddress);
        password = findViewById(R.id.Password);
        btn_reg = findViewById(R.id.button);
        tvSignIn = findViewById(R.id.textView2);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_ad.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(Register.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your Password");
                    password.requestFocus();
                }
                else if(email.isEmpty()){
                    email_ad.setError("Please enter your E-mail address");
                    email_ad.requestFocus();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty())){

                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(
                                    new OnCompleteListener<AuthResult>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task)
                                        {
                                            if (!task.isSuccessful())
                                            {
                                                try
                                                {
                                                    throw task.getException();
                                                }
                                                catch (FirebaseAuthWeakPasswordException weakPassword)
                                                {
                                                    Toast.makeText(Register.this,"Weak Password",Toast.LENGTH_SHORT).show();
                                                }
                                                catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                                {
                                                    Toast.makeText(Register.this,"E-mail format is not correct",Toast.LENGTH_SHORT).show();
                                                }
                                                catch (FirebaseAuthUserCollisionException existEmail)
                                                {
                                                    Toast.makeText(Register.this,"Already registered",Toast.LENGTH_SHORT).show();
                                                }
                                                catch (Exception e)
                                                {
                                                    Toast.makeText(Register.this,"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else {
                                                Toast.makeText(Register.this,"SignUp successful",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Register.this,Login.class));
                                            }
                                        }
                                    });

                }
                else{
                    Toast.makeText(Register.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this,Login.class);
                startActivity(i);
            }
        });
    }
}
