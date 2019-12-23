package com.geektech.taskapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.widget.Toast.LENGTH_SHORT;


//        1. view smsCode
//        2. Добавить в меню в MainActivity кнопку sign out (AlertDialog)

//        3. Лист с картинками доделать

public class PhoneActivity extends AppCompatActivity {
    private EditText editText, editCode;
    LinearLayout linearNumber, linearCode;
    private Button continueCode;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth mAuth;
    FirebaseAuth audth;

    boolean a;

    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String verificationId;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        editText = findViewById(R.id.editNumber);
        editCode = findViewById(R.id.editCode);
        continueCode = findViewById(R.id.buttonCode);
        linearNumber = findViewById(R.id.linearNumber);
        linearCode = findViewById(R.id.linearCode);


        continueCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String cod = editCode.getText().toString();
                if (cod.isEmpty()) {
                    editCode.setError("СМС код не введен");
                    editCode.requestFocus();
                    return;
                }
                PhoneAuthCredential phoneCredential = PhoneAuthProvider.getCredential(verificationId, cod);
                signIn(phoneCredential);




            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                Log.e("TAG", "onVerificationCompleted: ");

                if (a) {
//                    verifyCode(code);
                } else {
                    signIn(phoneAuthCredential);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d("TAG", "onVerificationFailed: " + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                a = true;
                verificationId = s;
                mResendToken = forceResendingToken;


            }


        };

    }

    private void signIn(final PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                    finish();

                } else {
                    Toast.makeText(PhoneActivity.this, "Код введен не правильно", LENGTH_SHORT).show();
                }
            }
        });
    }


    public void onClick(View view) {
        String number = editText.getText().toString().trim();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, this, callbacks);


        linearNumber.setVisibility(View.GONE);
        linearCode.setVisibility(View.VISIBLE);
    }




}

