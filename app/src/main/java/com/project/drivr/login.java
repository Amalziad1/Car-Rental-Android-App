package com.project.drivr;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class login extends AppCompatActivity {
    //    private String storedEmail;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText email = findViewById(R.id.emailLog);
        EditText password = findViewById(R.id.pass);
        CheckBox checkBoxRememberMe = findViewById(R.id.remember);
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(login.this, "registration", null, 1);
        //adding the first admin
        String defaultPFPpath = getApplicationContext().getFilesDir().toString() + "/default_profile_picture.jpg";
        copyPFPToFilesDir(R.raw.default_profile_picture, defaultPFPpath);
        Admin admin=new Admin("Female", "amal", "ziad", "amal@cardealer.com", "palestine", "jerusalem", hashPassword("Amal12*"), "00970123456789", defaultPFPpath);
        dataBaseHelper.insertAdmin(admin);
        //
        Button login = findViewById(R.id.login);
        //getting email and password if saved
        sharedPrefManager = SharedPrefManager.getInstance(this);
        String status = sharedPrefManager.readString("rememberMe", null);
        if (status != null && status.equals("yes")) {
            email.setText(sharedPrefManager.readString("userName", ""));
            password.setText(sharedPrefManager.readString("password", ""));
            checkBoxRememberMe.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
                    if (!registration.validateEmailFormat(email.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
                    } else {
                        if (dataBaseHelper.isUserWithEmailExists(email.getText().toString().toLowerCase())) {
                            String pass = hashPassword(password.getText().toString());
                            Cursor auth = dataBaseHelper.getUserByEmail(email.getText().toString());
                            if (auth != null && auth.moveToLast()) {
                                @SuppressLint("Range") String passValue = auth.getString(auth.getColumnIndexOrThrow("PASSWORD"));
                                if (passValue.equals(pass)) {
                                    sharedPrefManager.writeString("userName", email.getText().toString().toLowerCase().replaceAll("\\s", ""));
                                    sharedPrefManager.writeString("password", password.getText().toString());
                                    Toast.makeText(getApplicationContext(), "Welcome ", Toast.LENGTH_SHORT).show();
                                    if (checkBoxRememberMe.isChecked()) {
                                        sharedPrefManager.writeString("rememberMe", "yes");
                                    } else {
                                        sharedPrefManager.writeString("rememberMe", "no");
                                    }
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (auth != null) {
                                auth.close();
                            }
                        } else if(dataBaseHelper.isAdminWithEmailExists(email.getText().toString().toLowerCase())){
                            String pass = hashPassword(password.getText().toString());
                            Cursor auth = dataBaseHelper.getAdminByEmail(email.getText().toString().toLowerCase());
                            if (auth != null && auth.moveToLast()) {
                                @SuppressLint("Range") String passValue = auth.getString(auth.getColumnIndexOrThrow("PASSWORD"));
                                if (passValue.equals(pass)) {
                                    sharedPrefManager.writeString("userName", email.getText().toString().toLowerCase().replaceAll("\\s", ""));
                                    sharedPrefManager.writeString("password", password.getText().toString());
                                    Toast.makeText(getApplicationContext(), "Welcome ", Toast.LENGTH_SHORT).show();
                                    if (checkBoxRememberMe.isChecked()) {
                                        sharedPrefManager.writeString("rememberMe", "yes");
                                    } else {
                                        sharedPrefManager.writeString("rememberMe", "no");
                                    }
                                    Intent intent = new Intent(login.this, MainActivity_Admin.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (auth != null) {
                                auth.close();
                            }
                        }else {
                            Toast.makeText(login.this, "Admin with this email does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        CheckBox showPass = findViewById(R.id.checkBox);
        showPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setTransformationMethod(null);
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
    private void copyPFPToFilesDir(int resourceID, String defaultPFPpath) {
        InputStream defaultPFP = getResources().openRawResource(resourceID);
        try {
            Files.copy(defaultPFP, Paths.get(defaultPFPpath));
        } catch (IOException e) {
            Log.d("IOException:", "During default pfp copy", e);
        } catch (SecurityException e) {
            Log.d("SecurityException:", "During default pfp copy", e);
        }
    }

    public static String hashPassword(String password) {
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
}