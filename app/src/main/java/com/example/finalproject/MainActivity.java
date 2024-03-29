package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.finalproject.fragements.FavoriteFragment;
import com.example.finalproject.fragements.MainFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    String userEmail;
    DBConnection dbConnection = new DBConnection(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Navigation Drawer
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        // Get users email and add to nav header
        Bundle passedData = getIntent().getExtras();
        if (passedData != null) {
            userEmail = passedData.getString("userEmail");
        } else {
            userEmail = "Email";
        }
        TextView headerEmail = headerView.findViewById(R.id.navHeader_userEmail);
        headerEmail.setText(userEmail);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
            navigationView.setCheckedItem(R.id.drawer_home);
        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    // For ToolBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // For ToolBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.home_toolbar_help:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Help");
                alertDialogBuilder.setMessage("This page displays articles of the day unless specifically searched." +
                        " Click on a article to display more information and hold down on them to add to " +
                        "favorites.");
                alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                alertDialogBuilder.create().show();
                break;
            case R.id.toolbar_theme_dark:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case R.id.toolbar_theme_light:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    // For Navigation Drawer
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.drawer_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
                break;
            case R.id.drawer_favorites:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavoriteFragment()).commit();
                break;

            case R.id.drawer_info:
                // Alert Box with application information.
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("App Information");
                alertDialogBuilder.setMessage("Made By: Sebastien Corneau and Paul Magera\nVersion: 2.0\nContact: corn0123@algonquinlive.com,\npaul.seed18@gmail.com");
                alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                alertDialogBuilder.create().show();
                break;

            case R.id.drawer_sign_out:
                alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Sign Out");
                alertDialogBuilder.setMessage("Are you sure you want to sign out? This will end your current session.");
                alertDialogBuilder.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel());
                alertDialogBuilder.setNegativeButton("Yes, Im sure", (dialog, which) -> {
                    Intent loginPage = new Intent(this, LoginActivity.class);
                    startActivity(loginPage);
                    finish();
                });
                alertDialogBuilder.create().show();
                break;
            case R.id.drawer_user_info:
                UserModel currentUser = dbConnection.getUser(userEmail);
                alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("User Info");
                alertDialogBuilder.setMessage("First name: " + currentUser.getfName() + "\nLast name: " + currentUser.getlName() + "\nEmail : " +
                        currentUser.getMail());
                alertDialogBuilder.setNegativeButton("Close", (dialog, which) -> dialog.cancel());
                alertDialogBuilder.create().show();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
