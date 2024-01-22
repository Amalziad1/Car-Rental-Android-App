package com.project.drivr.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.project.drivr.Admin;
import com.project.drivr.DataBaseHelper;
import com.project.drivr.R;
import com.project.drivr.login;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class add_admin extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_admin, container, false);
        EditText fname=view.findViewById(R.id.fnameAdmin);
        EditText lname=view.findViewById(R.id.lnameAdmin);
        EditText email=view.findViewById(R.id.adminEmail);
        EditText password=view.findViewById(R.id.passwordAdmin);
        EditText confirmPassword=view.findViewById(R.id.confirmPassAdmin);
        Button addAdmin=view.findViewById(R.id.add);
        ConstraintLayout rootLayout = view.findViewById(R.id.addAdminRoot);
        CheckBox showPass = view.findViewById(R.id.showPassAdmin);
        CheckBox showConfirm = view.findViewById(R.id.confPassAdmin);
        //checkboxes
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
        showConfirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    confirmPassword.setTransformationMethod(null);
                } else {
                    confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        //handling button
        addAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = fname.getText().toString();
                String lastName = lname.getText().toString();
                String Email = email.getText().toString().toLowerCase().replaceAll("\\s", "");
                String Password = password.getText().toString();
                String confirm = confirmPassword.getText().toString();
                int flag = 0;
                if (firstName.length() < 3) {
                    fname.setError("Must be at least 3 characters");
                    flag++;
                }
                if (lastName.length() < 3) {
                    lname.setError("Must be at least 3 characters");
                    flag++;
                }
                if (!validateEmailFormat(Email)) {
                    email.setError("Invalid email format");
                    flag++;
                }
                if (!validatePassword(Password)) {
                    password.setError("Must be at least 5 chars, 1 character, 1 number, and 1 special character");
                    flag++;
                }
                if (!Password.equals(confirm)) {
                    confirmPassword.setError("Password does not match");
                    flag++;
                }
                if (flag > 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "Incomplete data", Toast.LENGTH_SHORT).show();
                } else {
                    Password = hashPassword(Password);
                    String defaultPFPpath = getActivity().getApplicationContext().getFilesDir().toString() + "/default_profile_picture.jpg";
                    copyPFPToFilesDir(R.raw.default_profile_picture, defaultPFPpath);
                    Admin admin = new Admin(firstName, lastName, Email, Password, defaultPFPpath);
                    DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getActivity().getApplicationContext(), "registration", null, 1);
                    if (dataBaseHelper.isAdminWithEmailExists(Email)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Admin with this email exists", Toast.LENGTH_SHORT).show();
                    } else {
                        dataBaseHelper.insertAdmin(admin);
                        Toast.makeText(getActivity().getApplicationContext(), "Admin added successfully", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
        return view;
    }
    public static boolean validateEmailFormat(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            String[] allowedDomains = {"cardealer.com"};
            String[] emailParts = email.split("@");
            if (emailParts.length == 2) {
                String domain = emailParts[1].toLowerCase();
                for (String allowedDomain : allowedDomains) {
                    if (domain.equals(allowedDomain)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private boolean validatePassword(String password) {
        if (password.length() < 5) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.find()) {
            return false;
        }
        return true;
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
}