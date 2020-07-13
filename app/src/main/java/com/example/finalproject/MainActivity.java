package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.navigation.NavigationView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    ListView listView;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = findViewById(R.id.mainActivity_ListView);
        listAdapter = new ListAdapter();
        listView.setAdapter(listAdapter);

        toolbar = findViewById(R.id.testToolbar_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.testToolbar_navDrawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.close, R.string.open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.testToolbar_navViewLayout);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        String message;
        if (item.getItemId() == R.id.toolbar_search) {
            message = "You clicked on search";
        } else {
            return super.onOptionsItemSelected(item);
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.drawer_home:
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                break;
            case R.id.drawer_favorites:
                Log.i("Drawer:", " Favorites pressed");
                break;

            case R.id.drawer_info:
                // Fragment or alert?
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("App Information");
                alertDialogBuilder.setMessage("Made By: Sebastien Corneau\nVersion: 1.0\nContact: corn0123@algonquinlive.com");
                alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                alertDialogBuilder.create().show();
                break;

            case R.id.drawer_sign_out:
                finish();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return false;
    }
}
