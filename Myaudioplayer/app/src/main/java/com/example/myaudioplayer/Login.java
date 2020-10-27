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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class Login extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.EmailAddress_log);
        password = findViewById(R.id.Password_log);
        btnSignIn = findViewById(R.id.button2);
        tvSignUp = findViewById(R.id.textView);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Please enter E-mail address");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your Password");
                    password.requestFocus();
                }
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(Login.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                try
                                {
                                    throw task.getException();
                                }
                                catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                {
                                    Toast.makeText(Login.this,"E-mail format is not correct",Toast.LENGTH_SHORT).show();
                                }
                                catch (FirebaseAuthInvalidUserException invalidEmail)
                                {
                                    Toast.makeText(Login.this,"Account does not exist",Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(Login.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                                }

                            }
                            else{
                                Toast.makeText(Login.this,"You are logged in",Toast.LENGTH_SHORT).show();
                                Intent intToHome = new Intent(Login.this,MainActivity.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Login.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(Login.this, Register.class);
                startActivity(intSignUp);
            }
        });
    }

}
