package com.project.drivr;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class login extends AppCompatActivity {
//    private String storedEmail;
    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText email=findViewById(R.id.emailLog);
        EditText password=findViewById(R.id.pass);
        CheckBox checkBoxRememberMe=findViewById(R.id.remember);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(login.this,"registration",null,1);
        Button login=findViewById(R.id.login);
        sharedPrefManager =SharedPrefManager.getInstance(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
                    if(!registration.validateEmailFormat(email.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
                    }else{
                        if(dataBaseHelper.isUserWithEmailExists(email.getText().toString())){
                            //Toast.makeText(getApplicationContext(), "--------------", Toast.LENGTH_SHORT).show();
                            String pass=hashPassword(password.getText().toString());
                            Cursor auth = dataBaseHelper.getUserByEmail(email.getText().toString());
                            if (auth != null && auth.moveToLast()) {
                                @SuppressLint("Range") String passValue = auth.getString(auth.getColumnIndexOrThrow("PASSWORD"));
                                if(passValue.equals(pass)){
                                    sharedPrefManager.writeString("userName",email.getText().toString());
                                    sharedPrefManager.writeString("password",password.getText().toString());
                                    Toast.makeText(getApplicationContext(), "Login succeed", Toast.LENGTH_SHORT).show();
                                    if (checkBoxRememberMe.isChecked()) {
                                        sharedPrefManager.writeString("rememberMe","yes");
                                    } else {
                                        sharedPrefManager.writeString("rememberMe","no");
                                    }
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (auth != null) {
                                auth.close();
                            }
                        }else{
                            Toast.makeText(login.this, "User with this email does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
//    private SharedPreferences storeCredentials(String email, String password) {//yes to save data if 1, 0 if not
//        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("storedEmail", email);
//        editor.putString("storedPassword", password);
////        editor.putInt("rememberMe",yes);
//        editor.apply();
//        return preferences;
//    }
//
//    private void clearStoredCredentials() {
//        //clear stored credentials if "Remember Me" is unchecked
//        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.remove("storedEmail");
//        editor.remove("storedPassword");
//        editor.apply();
//    }
}