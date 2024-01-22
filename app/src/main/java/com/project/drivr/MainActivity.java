package com.project.drivr;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.project.drivr.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements UpdateUserInfoUI {
    //implements NavigationView.OnNavigationItemSelectedListener
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private SharedPrefManager sharedPrefManager;
    private DataBaseHelper dataBaseHelper;
    private NavigationView navigationView;
    //for checking if there's new special offer
    private int year = 2024;
    private int month = 1;
    private int day = 15;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;

        sharedPrefManager = SharedPrefManager.getInstance(this);
        //notifications
        LocalDateTime currentDateTime = LocalDateTime.now();
        String strMonth = sharedPrefManager.readString("month", null);
        if (strMonth != null) {
            this.month = Integer.parseInt(strMonth);
        }
        LocalDateTime endDate = LocalDateTime.of(year, month, day, 0, 0);
        if (currentDateTime.isAfter(endDate)) {
            this.month = this.month + 1;
            sharedPrefManager.writeString("month", String.valueOf(this.month));
            notification();//notify when the new notification is released
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_car_menu, R.id.nav_reservations, R.id.nav_favorites, R.id.nav_offers, R.id.nav_profile, R.id.nav_find_us, R.id.nav_log_out)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Fetch data
        String userName = sharedPrefManager.readString("userName", null);
        dataBaseHelper = DataBaseHelper.getInstance(this, "registration", null, 1);
        Cursor cursor = dataBaseHelper.getUserByEmail(userName);
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

    public void notification() {
        createNotificationChannel();
        Notification.Builder builder = new Notification.Builder(this, "1")
                .setSmallIcon(R.drawable.directions_car)
                .setContentTitle("New special offer released")
                .setContentText("Come check it now!")
                .setAutoCancel(true) //dismiss the notification when the user taps it
                .setColor(Color.GREEN);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(/* notification_id */ 1, builder.build());
    }

    private void createNotificationChannel() {
        String channelId = "1";
        CharSequence channelName = "Channel";
        String channelDescription = "Notifying users when new offer is released";
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(channelDescription);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

}