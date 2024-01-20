package com.project.drivr;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.project.drivr.databinding.ActivityMainAdminBinding;

public class MainActivity_Admin extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainAdminBinding binding; // Use the correct binding class
    private SharedPrefManager sharedPrefManager;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the correct layout inflation
        binding = ActivityMainAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout2;
        NavigationView navigationView = binding.navView2;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top-level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_add, R.id.nav_delete, R.id.nav_view_customers, R.id.nav_feedback, R.id.nav_log_out_admin)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        sharedPrefManager = SharedPrefManager.getInstance(this);
        String userName = sharedPrefManager.readString("userName", null);
        dataBaseHelper = DataBaseHelper.getInstance(this, "registration", null, 1);
        Cursor cursor = dataBaseHelper.getAdminByEmail(userName);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("FIRSTNAME")) + " " + cursor.getString(cursor.getColumnIndex("LASTNAME"));
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
        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_admin);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
