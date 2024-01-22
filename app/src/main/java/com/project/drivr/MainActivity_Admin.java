package com.project.drivr;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.project.drivr.databinding.ActivityMainAdminBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity_Admin extends AppCompatActivity implements UpdateUserInfoUI{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainAdminBinding binding;
    private SharedPrefManager sharedPrefManager;
    private DataBaseHelper dataBaseHelper;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarAdmin.toolbar2);

        DrawerLayout drawer = binding.drawerLayout2;
        navigationView = binding.navView2;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.add_admin, R.id.delete_customer, R.id.view_all_reservations, R.id.view_feedback, R.id.log_out)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        sharedPrefManager = SharedPrefManager.getInstance(this);
        // Fetch data
        String userName = sharedPrefManager.readString("userName", null);
        dataBaseHelper = DataBaseHelper.getInstance(this, "registration", null, 1);
        Cursor cursor = dataBaseHelper.getAdminByEmail(userName);
        cursor.moveToLast();
        int firstNameIndex = cursor.getColumnIndexOrThrow("FIRSTNAME");
        String name = cursor.getString(firstNameIndex) + " " + cursor.getString(firstNameIndex + 1);
        String PFPpath = cursor.getString(cursor.getColumnIndexOrThrow("PICTURE_PATH"));

        // Update UI elements
        TextView nameTextView = navigationView.getHeaderView(0).findViewById(R.id.userNameTextView);
        nameTextView.setText(name);
        TextView emailTextView = navigationView.getHeaderView(0).findViewById(R.id.emailTextView);
        emailTextView.setText(userName);
        ImageView profilePictureView = navigationView.getHeaderView(0).findViewById(R.id.profilePictureImageView);
        File file = new File(PFPpath);
        if (file.isFile()) {
            Picasso.get().load(file).into(profilePictureView);
            Log.e("UI:", "Navigation drawer UI user info updated");
        } else {
            Log.e("PFP:", "Bitmap does not exist.");
        }

    }
    @Override
    public void updateUserInfoUI(User user) {
        TextView nameTextView = navigationView.getHeaderView(0).findViewById(R.id.userNameTextView);
        nameTextView.setText(user.getFirstName() + " " + user.getLastName());
        TextView emailTextView = navigationView.getHeaderView(0).findViewById(R.id.emailTextView);
        emailTextView.setText(user.getEmail());
        ImageView profilePictureView = navigationView.getHeaderView(0).findViewById(R.id.profilePictureImageView);
        File file = new File(user.getPicturePath());
        if (file.isFile()) {
            Picasso.get().load(file).into(profilePictureView);
            Log.e("UI:", "Navigation drawer UI user info updated");
        } else {
            Log.e("PFP:", "Bitmap does not exist.");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_admin);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
