package com.example.projectmodel;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private TextView userNameTextView;
    private TextView userEmailTextView;

    //private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize views
        View headerView = navigationView.getHeaderView(0);



        {
            String userEmail = "jjadhavutkarsha.gmail.com";
           // userEmailTextView.setText(userEmail);
        }




//        userNameTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Replace the fragment with AccountFragment
////             **   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFragment()).commit();
//                // Close the drawer (optional)
//                drawerLayout.closeDrawer(GravityCompat.START);
//            }
//        });
//
//        userEmailTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Replace the fragment with AccountFragment
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFragment()).commit();
//                // Close the drawer (optional)
//                drawerLayout.closeDrawer(GravityCompat.START);
//            }
//        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
        else if (itemId == R.id.nav_settings) {
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
        } else if (itemId == R.id.nav_share) {
            // Show sharing options
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "TaskPro App");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out TaskPro App: https://play.google.com/store/apps?hl=en&gl=US");
            startActivity(Intent.createChooser(shareIntent, "Share App"));
        } else if (itemId == R.id.nav_help) {
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HelpFragment()).commit();
        } else if (itemId == R.id.nav_account) {
          //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFragment()).commit();
        } else if (itemId == R.id.nav_about) {
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
        } else if (itemId == R.id.nav_contact) {
          getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactFragment()).commit();
        } else if (itemId == R.id.nav_privacy) {
          getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PrivacyFragment()).commit();
        } else if (itemId == R.id.nav_logout) {

//            Toast.makeText(this, "Logout Successfully!", Toast.LENGTH_SHORT).show();
//            Intent loginIntent = new Intent(DashboardActivity.this, LoginActivity.class);
//            startActivity(loginIntent);
//            finish(); // Optional: finish the current activity to prevent going back to it
        }
        drawerLayout.closeDrawer(GravityCompat.START); // Minimize the side menu
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}