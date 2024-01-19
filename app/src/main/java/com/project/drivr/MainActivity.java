package com.project.drivr;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.widget.ImageViewCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.project.drivr.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private SharedPrefManager sharedPrefManager;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_car_menu, R.id.nav_reservations, R.id.nav_favorites, R.id.nav_offers, R.id.nav_profile, R.id.nav_find_us, R.id.nav_log_out)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        sharedPrefManager = SharedPrefManager.getInstance(this);
        String userName = sharedPrefManager.readString("userName", null);
        dataBaseHelper = DataBaseHelper.getInstance(this, "registration", null, 1);
        Cursor cursor = dataBaseHelper.getUserByEmail(userName);
        cursor.moveToLast();
        int firstNameIndex = cursor.getColumnIndexOrThrow("FIRSTNAME");
        String name = cursor.getString(firstNameIndex) + " " + cursor.getString(firstNameIndex + 1);
        String PFPpath = cursor.getString(cursor.getColumnIndexOrThrow("PICTURE_PATH"));
        TextView nameTextView = navigationView.getHeaderView(0).findViewById(R.id.userNameTextView);
        nameTextView.setText(name);
        TextView emailTextView = navigationView.getHeaderView(0).findViewById(R.id.emailTextView);
        emailTextView.setText(userName);
        ImageView profilePictureView = navigationView.getHeaderView(0).findViewById(R.id.profilePictureImageView);
        Bitmap myBitmap = BitmapFactory.decodeFile(PFPpath);
        if (myBitmap != null) {
                profilePictureView.setImageBitmap(myBitmap);
        }
        else {
            Log.e("PFP:", "Bitmap does not exist.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}