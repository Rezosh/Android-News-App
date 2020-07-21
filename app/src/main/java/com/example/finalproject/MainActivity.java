package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<ArticleModel> articleList = new ArrayList<>();
    Toolbar toolbar;
    ListView listView;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // API Url to query
        MyHTTPRequest req = new MyHTTPRequest();
        req.execute("https://content.guardianapis.com/search?api-key=099b3bf9-74f6-4441-b4ef-fb8ec4aabb46&q=tech&page-size=50&show-fields=thumbnail");
        // TODO: Add a url builder to search api.


        listView = findViewById(R.id.mainActivity_ListView);
        listAdapter = new ListAdapter();
        listView.setAdapter(listAdapter);


        toolbar = findViewById(R.id.activityMain_toolbar);
        setSupportActionBar(toolbar);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            ArticleModel selectedArticle = articleList.get(position);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(selectedArticle.getTitle())
                    .setMessage("Url: " + selectedArticle.getUrl() + "\nSection: " + selectedArticle.getSection())
                    .setNegativeButton("Go Back", (dialog, which) -> dialog.cancel())
                    .setPositiveButton("View Article In Browser", ((dialog, which) -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(selectedArticle.getUrl())))))
                    .create()
                    .show();

        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            ArticleModel selectedArticle = articleList.get(position);
            Snackbar.make(view, "Added article to favorites", Snackbar.LENGTH_LONG).show();
            // TODO: Add favorites to the database
            return true;
        });



    }

    // For ToolBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // For ToolBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        String message;
        switch (item.getItemId()) {
            case R.id.toolbar_search:
                message = "You clicked on search";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            case R.id.toolbar_info:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("App Information");
                alertDialogBuilder.setMessage("Made By: Sebastien Corneau\nVersion: 1.0\nContact: corn0123@algonquinlive.com");
                alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                alertDialogBuilder.create().show();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return true;
    }

    // For Navigation Drawer
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


    protected class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return articleList.size();
        }

        public Object getItem(int position) {
            return "This is row " + position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View newView = convertView;

            if (newView == null) {
                newView = getLayoutInflater().inflate(R.layout.row, parent, false);
            }
            Log.i("MainActivity", "<<<<---- ADAPTER ---->>>>");
            TextView title = newView.findViewById(R.id.rowTitle);
            title.setText(articleList.get(position).getTitle());

            ImageView thumbnail = newView.findViewById(R.id.rowThumbnail);
            Picasso
                    .get()
                    .load(articleList.get(position).getThumbnail())
                    .resize(100, 100)
                    .centerCrop()
                    .into(thumbnail);
            return newView;

        }

    }

    private class MyHTTPRequest extends AsyncTask<String, Integer, String> {
        String articleTitle;
        String articleUrl;
        String articleThumbnail;
        String articleSection;


        public String doInBackground(String... args) {
            try {


                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();


                //JSON reading:
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON:
                JSONObject resp = new JSONObject(result);
                JSONObject parsed = resp.getJSONObject("response");
                Log.i("MainActivity", "JSONObject parsed = " + parsed);

                JSONArray jsonArray = parsed.getJSONArray("results");
                for (int i=0; i < jsonArray.length(); i++) {
                    try {

                        JSONObject anObject = jsonArray.getJSONObject(i);
                        JSONObject articleFields = anObject.getJSONObject("fields");

                        articleTitle = anObject.getString("webTitle");
                        articleUrl = anObject.getString("webUrl");
                        articleThumbnail = articleFields.getString("thumbnail");
                        articleSection = anObject.getString("sectionName");

                        ArticleModel articleModel = new ArticleModel(articleTitle, articleUrl, articleThumbnail, articleSection);
                        articleList.add(articleModel);

                    } catch (JSONException e) {
                        Log.e("MainActivity", "JSONException ERROR: " + e);

                    }
                }

            } catch (Exception e) {
                Log.e("MainActivity", "ERROR: " + e);
            }

            return "Done";
        }

        public void onProgressUpdate(Integer... args) {
            // TODO: Add loading bar for articles
        }

        public void onPostExecute(String fromDoInBackground) {
            // Update the listview with all the articles
            listAdapter.notifyDataSetChanged();
            super.onPostExecute(fromDoInBackground);

        }
    }



}
