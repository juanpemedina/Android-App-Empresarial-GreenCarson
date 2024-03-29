package com.example.greencarson_industria_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //TextInputEditText editTextEmail, editTextPassword;
    EditText editTextEmail, editTextPassword;
    Button buttonLog;
    FirebaseAuth mAuth;
    TextView alert;
    TextView emailTextView;
    TextView claveTextView;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.email);
        editTextPassword =  findViewById(R.id.password);

        buttonLog = findViewById(R.id.btn_login);
        alert = findViewById(R.id.alertView);
        emailTextView = findViewById(R.id.textView10);
        claveTextView= findViewById(R.id.textView12);

        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    claveTextView.setTextColor(getResources().getColor(R.color.black));
                                    emailTextView.setTextColor(getResources().getColor(R.color.black));
                                    alert.setVisibility(View.INVISIBLE);
                                    editTextEmail.setBackgroundResource(R.drawable.text_border); //quitar si no sirve
                                    editTextPassword.setBackgroundResource(R.drawable.text_border); //quitar si no sirve
                                    Toast.makeText(getApplicationContext(), "Login correctamente", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    //Cambiar el ingles
                                    Toast.makeText(LoginActivity.this, "Contraseña o Correo incorrecto.", Toast.LENGTH_SHORT).show();
                                    alert.setVisibility(View.VISIBLE);
                                    claveTextView.setTextColor(getResources().getColor(R.color.redAdver));
                                    emailTextView.setTextColor(getResources().getColor(R.color.redAdver));
                                    editTextEmail.setBackgroundResource(R.drawable.text_border_alert); //quitar si no sirve
                                    editTextPassword.setBackgroundResource(R.drawable.text_border_alert); //quitar si no sirve
                                }
                            }
                        });


            }
        });

    }
    //Go to main
    public void goToMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}