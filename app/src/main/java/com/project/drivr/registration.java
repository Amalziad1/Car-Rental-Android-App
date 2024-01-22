package com.project.drivr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registration extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //defining all elements
        Spinner spinnerGender = findViewById(R.id.gender);
        Spinner spinnerCountry = findViewById(R.id.country);
        Spinner spinnerCity = findViewById(R.id.city);
        EditText editTextPhoneNumber = findViewById(R.id.phone);
        EditText fname = findViewById(R.id.fname);
        EditText lname = findViewById(R.id.lname);
        EditText password = findViewById(R.id.password);
        EditText email = findViewById(R.id.email);
        EditText confirm = findViewById(R.id.confirmPassword);
        ConstraintLayout rootLayout = findViewById(R.id.root);
        CheckBox showPass = findViewById(R.id.checkBoxPass);
        CheckBox showConfirm = findViewById(R.id.checkBoxConfirm);
        Button signup = findViewById(R.id.signup);
        //implementing gender spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender_array, //in strings.xml in values
                android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);
        //implementing countries spinner
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this, R.array.country_array,//in strings.xml in values
                android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapter);
        //implementing cities spinner & phone number
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCountry = parentView.getItemAtPosition(position).toString();
                if (selectedCountry.equals("Select Country")) {
                    selectedCountry = selectedCountry.replaceAll("\\s", "");
                    int selected = getResources().getIdentifier(selectedCountry.toLowerCase(), "array", getPackageName());
                    ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(registration.this, selected, android.R.layout.simple_spinner_item);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCity.setAdapter(cityAdapter);
                } else {
                    String zipCode = getZipCodeForCountry(selectedCountry);
                    editTextPhoneNumber.setText("00" + zipCode);
                    int citiesArrayResourceId = getResources().getIdentifier(selectedCountry.toLowerCase().concat("_cities"), "array", getPackageName());
                    ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(registration.this, citiesArrayResourceId, android.R.layout.simple_spinner_item);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCity.setAdapter(cityAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        editTextPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String selectedCountry = spinnerCountry.getSelectedItem().toString();
                String zipCode = getZipCodeForCountry(selectedCountry);
                String phoneNumber = editable.toString();
                if (!phoneNumber.startsWith("00" + zipCode)) {
                    editTextPhoneNumber.setText("00" + zipCode + phoneNumber.substring(1));
                }
            }
        });
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });
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
                    confirm.setTransformationMethod(null);
                } else {
                    confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                String firstName = fname.getText().toString();
                String lastName = lname.getText().toString();
                String Email = email.getText().toString().toLowerCase().replaceAll("\\s", "");
                String Country = spinnerCountry.getSelectedItem().toString();
                String City = spinnerCity.getSelectedItem().toString();
                String Password = password.getText().toString();
                String confirmPassword = confirm.getText().toString();
                String phoneNumber = editTextPhoneNumber.getText().toString();
                String gender = spinnerGender.getSelectedItem().toString();
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
                if (Country.equalsIgnoreCase("Select Country") || City.equalsIgnoreCase("Select City") || City.equals(null)) {
                    flag++;
                }
                if (!validatePassword(Password)) {
                    password.setError("Must be at least 5 chars, 1 character, 1 number, and 1 special character");
                    flag++;
                }
                if (phoneNumber.length() < 13) {
                    editTextPhoneNumber.setError("Must be 13 length");
                    flag++;
                }
                if (!Password.equals(confirmPassword)) {
                    confirm.setError("Password does not match");
                    flag++;
                }
                if (flag > 0) {
                    Toast.makeText(getApplicationContext(), "Incomplete data", Toast.LENGTH_SHORT).show();
                } else {
                    Password = hashPassword(Password);
                    long number = Long.parseLong(phoneNumber);
                    String defaultPFPpath = getApplicationContext().getFilesDir().toString() + "/default_profile_picture.jpg";
                    copyPFPToFilesDir(R.raw.default_profile_picture, defaultPFPpath);
                    User user = new User(firstName, lastName, gender, Email, Country, City, Password, number, defaultPFPpath);
                    DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(registration.this, "registration", null, 1);
                    if (dataBaseHelper.isUserWithEmailExists(Email)) {
                        Toast.makeText(registration.this, "User with this email exists", Toast.LENGTH_SHORT).show();
                    } else {
                        dataBaseHelper.insertUser(user);
                        Toast.makeText(registration.this, "User added successfully", Toast.LENGTH_SHORT).show();
                        //opening another ui
                        Intent intent = new Intent(registration.this, login.class);
                        startActivity(intent);
                        finish();
                    }
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

    public static boolean validateEmailFormat(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            String[] allowedDomains = {"gmail.com", "outlook.com", "hotmail.com", "yahoo.com", "icloud.com","cardealer.com"};
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

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private String getZipCodeForCountry(String country) {
        int zipCodeResourceId = getResources().getIdentifier(country + "_zip_code", "string", getPackageName());
        return getString(zipCodeResourceId);
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