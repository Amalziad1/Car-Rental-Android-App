package com.project.drivr.admin;
import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.drivr.Admin;
import com.project.drivr.DataBaseHelper;
import com.project.drivr.R;
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
        Spinner spinnerGender = view.findViewById(R.id.gender2);
        Spinner spinnerCountry = view.findViewById(R.id.country2);
        Spinner spinnerCity = view.findViewById(R.id.city2);
        EditText editTextPhoneNumber = view.findViewById(R.id.phone2);
        Button addAdmin=view.findViewById(R.id.add);
        ConstraintLayout rootLayout = view.findViewById(R.id.addAdminRoot);
        CheckBox showPass = view.findViewById(R.id.showPassAdmin);
        CheckBox showConfirm = view.findViewById(R.id.confPassAdmin);
        //implementing gender spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.gender_array, //in strings.xml in values
                android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);
        //implementing countries spinner
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.country_array,//in strings.xml in values
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
                    int selected = getResources().getIdentifier(selectedCountry.toLowerCase(), "array", requireContext().getPackageName());
                    ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), selected, android.R.layout.simple_spinner_item);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCity.setAdapter(cityAdapter);
                } else {
                    String zipCode = getZipCodeForCountry(selectedCountry);
                    editTextPhoneNumber.setText("00" + zipCode);
                    int citiesArrayResourceId = getResources().getIdentifier(selectedCountry.toLowerCase().concat("_cities"), "array", requireContext().getPackageName());
                    ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), citiesArrayResourceId, android.R.layout.simple_spinner_item);
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
        //hiding keyboard if layout clicked
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
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
                hideKeyboard();
                String firstName = fname.getText().toString();
                String lastName = lname.getText().toString();
                String Email = email.getText().toString().toLowerCase().replaceAll("\\s", "");
                String Country = spinnerCountry.getSelectedItem().toString();
                String City = spinnerCity.getSelectedItem().toString();
                String Password = password.getText().toString();
                String confirm = confirmPassword.getText().toString();
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
                if (phoneNumber.length() < 14) {
                    editTextPhoneNumber.setError("Must be 13 length");
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
                    Admin admin = new Admin(firstName, lastName, gender, Email, Country, City, Password, phoneNumber, defaultPFPpath);
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
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = requireActivity().getCurrentFocus();
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }


    private String getZipCodeForCountry(String country) {
        int zipCodeResourceId = getResources().getIdentifier(country + "_zip_code", "string", requireContext().getPackageName());
        return getString(zipCodeResourceId);
    }
}