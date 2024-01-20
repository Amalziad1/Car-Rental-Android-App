package com.project.drivr.ui.profile;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.content.ContextCompat.getSystemService;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.drivr.DataBaseHelper;
import com.project.drivr.LoginSignup;
import android.Manifest;

import com.project.drivr.MainActivity;
import com.project.drivr.R;
import com.project.drivr.SharedPrefManager;
import com.project.drivr.UpdateUserInfoUI;
import com.project.drivr.User;
import com.project.drivr.databinding.FragmentProfileBinding;
import com.project.drivr.registration;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    ImageView profilePictureView;
    String userName;
    Uri imageURI;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //defining all elements
        profilePictureView = binding.IVPreviewImage;
        final Button selectPFPFile = binding.BSelectImage;
        final Spinner spinnerGender = binding.gender;
        final Spinner spinnerCountry = binding.country;
        final Spinner spinnerCity = binding.city;
        final EditText editTextPhoneNumber = binding.phone;
        final EditText fname = binding.fname;
        final EditText lname = binding.lname;
        final EditText password = binding.password;
        final EditText confirm = binding.confirmPassword;
        final ConstraintLayout rootLayout = binding.root;
        final CheckBox showPass = binding.checkBoxPass;
        final CheckBox showConfirm = binding.checkBoxConfirm;
        final Button updateInformation = binding.updateInformation;

        // grabbing user from database
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(requireActivity().getApplicationContext());
        String userName = sharedPrefManager.readString("userName", null);
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(requireActivity().getApplicationContext(), "registration", null, 1);
        Cursor cursor = dataBaseHelper.getUserByEmail(userName);
        cursor.moveToLast();

        //implementing gender spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                requireActivity().getApplicationContext(),
                R.array.gender_array, //in strings.xml in values
                android.R.layout.simple_spinner_item
        );
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);
        //implementing countries spinner
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(
                requireActivity().getApplicationContext(),
                R.array.country_array,//in strings.xml in values
                android.R.layout.simple_spinner_item
        );
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapter);

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCountry = parentView.getItemAtPosition(position).toString();
                int selected;
                if(selectedCountry.equals("Select Country")){
                    selectedCountry=selectedCountry.replaceAll("\\s", "");
                    selected = getResources().getIdentifier(
                            selectedCountry.toLowerCase(),
                            "array",
                            requireActivity().getPackageName()
                    );
                }else{
                    String zipCode = getZipCodeForCountry(selectedCountry);
                    editTextPhoneNumber.setText("00" + zipCode);
                    selected = getResources().getIdentifier(
                            selectedCountry.toLowerCase().concat("_cities"),
                            "array",
                            requireActivity().getPackageName()
                    );
                }
                ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(
                        getActivity().getApplicationContext(),
                        selected,
                        android.R.layout.simple_spinner_item
                );
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCity.setAdapter(cityAdapter);
                spinnerCity.setSelection(cityAdapter.getPosition(cursor.getString(cursor.getColumnIndexOrThrow("CITY"))));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        int firstNameIndex = cursor.getColumnIndexOrThrow("FIRSTNAME");
        String PFPpath = cursor.getString(cursor.getColumnIndexOrThrow("PICTURE_PATH"));
        fname.setText(cursor.getString(firstNameIndex));
        lname.setText(cursor.getString(firstNameIndex + 1));
        spinnerGender.setSelection(genderAdapter.getPosition(cursor.getString(cursor.getColumnIndexOrThrow("GENDER"))));
        spinnerCountry.setSelection(countryAdapter.getPosition(cursor.getString(cursor.getColumnIndexOrThrow("COUNTRY"))));
        File file = new File(PFPpath);
        if (file != null) {
            Picasso.get().load(file).into(profilePictureView);
        }
        else {
            Log.e("PFP:", "Bitmap does not exist.");
        }
        selectPFPFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
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
                // String selectedCountry = spinnerCountry.getSelectedItem().toString();
                // String zipCode = getZipCodeForCountry(selectedCountry);
                // String phoneNumber = editable.toString();
                // if (!phoneNumber.startsWith("00" + zipCode)) {
                //     editTextPhoneNumber.setText("00" + zipCode + phoneNumber.substring(1));
                // }
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
        updateInformation.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                hideKeyboard();
                //Toast.makeText(getApplicationContext(), "============", Toast.LENGTH_SHORT).show();
                String firstName=fname.getText().toString();
                String lastName=lname.getText().toString();
                String Country=spinnerCountry.getSelectedItem().toString();
                String City=spinnerCity.getSelectedItem().toString();
                String Password=password.getText().toString();
                String confirmPassword=confirm.getText().toString();
                String phoneNumber=editTextPhoneNumber.getText().toString();
                String gender=spinnerGender.getSelectedItem().toString();
                int flag=0;
                if(firstName.length()<3){
                    fname.setError("Must be at least 3 characters");
                    flag++;
                }if(lastName.length()<3){
                    lname.setError("Must be at least 3 characters");
                    flag++;
                }if(Country.equalsIgnoreCase("Select Country") || City.equalsIgnoreCase("Select City")|| City.equals(null)){
                    flag++;
                }if(!validatePassword(Password)){
                    password.setError("Must be at least 5 chars, 1 character, 1 number, and 1 special character");
                    flag++;
                }if(phoneNumber.length()<13){
                    editTextPhoneNumber.setError("Must be 13 length");
                    flag++;
                }if(!Password.equals(confirmPassword)){
                    confirm.setError("Password does not match");
                    flag++;
                }
                if(flag>0){
                    Toast.makeText(requireActivity().getApplicationContext(), "Incomplete data", Toast.LENGTH_SHORT).show();
                }else{
                    Password=hashPassword(Password);
                    long number=Long.parseLong(phoneNumber);
                    String newPfpPath;
                    if (imageURI != null) {
                        File file = new File(Objects.requireNonNull(imageURI.getPath()));
                        newPfpPath = requireActivity().getFilesDir().toString() + "/" + Paths.get(file.getAbsolutePath()).getFileName();
                        copyImageToFilesDir(imageURI, newPfpPath);
                        Log.d("Image copy:", "From" + file.getAbsolutePath() + " to: " + newPfpPath);
                    }
                    else {
                        newPfpPath = PFPpath;
                    }
                    User user=new User(firstName, lastName, gender, userName, Country, City, Password, number, newPfpPath);
                    DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(requireActivity().getApplicationContext(),"registration",null,1);
                    dataBaseHelper.updateUser(user);
                    UpdateUserInfoUI callback = (UpdateUserInfoUI) requireActivity();
                    callback.updateUserInfoUI(user);
                    Toast.makeText(requireActivity().getApplicationContext(), "User information updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        editTextPhoneNumber.setText(cursor.getString(cursor.getColumnIndexOrThrow("PHONE")));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void UpdateProfileInformation() {
        Context context = getActivity().getApplicationContext();
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(context);
        String userName = sharedPrefManager.readString("userName", null);
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(context, "registration", null, 1);
        Cursor cursor = dataBaseHelper.getUserByEmail(userName);
        cursor.moveToLast();
        int firstNameIndex = cursor.getColumnIndexOrThrow("FIRSTNAME");
        String name = cursor.getString(firstNameIndex) + " " + cursor.getString(firstNameIndex + 1);
        String PFPpath = cursor.getString(cursor.getColumnIndexOrThrow("PICTURE_PATH"));
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
            String[] allowedDomains = {"gmail.com", "outlook.com", "hotmail.com", "yahoo.com", "icloud.com"};
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
        InputMethodManager inputManager = (InputMethodManager)requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
    private String getZipCodeForCountry(String country) {
        int zipCodeResourceId = getResources().getIdentifier(
                country + "_zip_code",
                "string",
                requireActivity().getApplicationContext().getPackageName()
        );
        return getString(zipCodeResourceId);
    }
    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {
        if (ContextCompat.checkSelfPermission(
                requireActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);

            // pass the constant to compare it
            // with the returned requestCode
            startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
        }else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
            );
        }
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 200) {
                // Get the url of the image from data
                imageURI = data.getData();
                Picasso.get().load(imageURI).into(profilePictureView);
                Log.d("PFP:", imageURI.getPath());
            }
        }
    }
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);

                    // pass the constant to compare it
                    // with the returned requestCode
                    startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
                } else {
                    Toast.makeText(requireActivity().getApplicationContext(), "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
                }
            });
    public void copyImageToFilesDir(Uri imageURI, String destination)
    {
        OutputStream outputStream = null;
        try
        {
            InputStream inputStream = requireActivity().getApplicationContext().getContentResolver().openInputStream(imageURI);
            File file = new File(destination);
            outputStream = new FileOutputStream(file);

            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.close();
        } catch (IOException e) {
            Log.d("IOException:", "During default pfp copy", e);
        } catch (SecurityException e) {
            Log.d("SecurityException:", "During default pfp copy", e);
        }
    }

}