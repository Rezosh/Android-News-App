package com.example.finalproject.fragements;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.finalproject.ArticleModel;
import com.example.finalproject.DBConnection;
import com.example.finalproject.ListAdapter;
import com.example.finalproject.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainFragment extends Fragment {

    /**
     * Used for logging
     */
    private static final String ACTIVITY_NAME = "MainFragment";

    /**
     * Used to store articles and load them into list view
     */
    List<ArticleModel> articleList = new ArrayList<>();

    /**
     * Initialize views
     */
    ListView listView;
    ListAdapter listAdapter;
    SearchView searchView;
    ProgressBar progressBar;
    MyHTTPRequest myHTTPRequest;
    SQLiteDatabase db;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout into fragment
        View mainView = inflater.inflate(R.layout.fragment_main, container, false);

        // Set toolbar title
        if (getActivity() != null) {
            getActivity().setTitle("Home");
        }

        // Find vies in layout
        listView = mainView.findViewById(R.id.mainActivity_ListView);
        progressBar = mainView.findViewById(R.id.main_progressBar);
        swipeRefreshLayout = mainView.findViewById(R.id.swiperefresh);

        // Load content for main page; articles of the day
        myHTTPRequest = new MyHTTPRequest();
        myHTTPRequest.execute("https://content.guardianapis.com/search?api-key=099b3bf9-74f6-4441-b4ef-fb8ec4aabb46&page-size=50&show-fields=thumbnail");

        listAdapter = new ListAdapter(getContext(), articleList, R.layout.news_image);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            ArticleModel selectedArticle = articleList.get(position);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setTitle(selectedArticle.getTitle())
                    .setMessage("Url: " + selectedArticle.getUrl() + "\n\nSection: " + selectedArticle.getSection())
                    .setNegativeButton("Go Back", (dialog, which) -> dialog.cancel())
                    .setPositiveButton("View Article In Browser", ((dialog, which) -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(selectedArticle.getUrl())))))
                    .create()
                    .show();

        });

        // Add article to favorites
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            // Find info for clicked row
            ArticleModel selectedArticle = articleList.get(position);
            String endpoint = selectedArticle.getEndpoint();
            // Connect to database
            DBConnection dbConnection = new DBConnection(getContext());
            db = dbConnection.getWritableDatabase();

            // Insert article into database
            dbConnection.insertArticle(selectedArticle);
            Log.i(ACTIVITY_NAME, "Article successfully added to database");
            Snackbar.make(view, "Added article to favorites", Snackbar.LENGTH_LONG)
                .setAction("Undo", click -> {
                    dbConnection.deleteArticle(selectedArticle.getEndpoint());
                })
                .show();
            return true;
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            myHTTPRequest = new MyHTTPRequest();
            myHTTPRequest.execute("https://content.guardianapis.com/search?api-key=099b3bf9-74f6-4441-b4ef-fb8ec4aabb46&page-size=50&show-fields=thumbnail");
            swipeRefreshLayout.setRefreshing(false);
        });


        setHasOptionsMenu(true);
        return mainView;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate menu
        menu.clear();
        menuInflater.inflate(R.menu.toolbar_menu_home, menu);
        MenuItem menuItem = menu.findItem(R.id.toolbar_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String url = buildUrl(query);
                myHTTPRequest = new MyHTTPRequest();
                myHTTPRequest.execute(url);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class MyHTTPRequest extends AsyncTask<String, Integer, String> {
        String articleTitle;
        String articleUrl;
        String articleThumbnail;
        String articleSection;
        String articleId;
        String date;
        String articleDate;


        public String doInBackground(String... args) {
            try {


                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                progressBar.setProgress(25);
                //wait for data:
                InputStream response = urlConnection.getInputStream();

                progressBar.setProgress(40);
                //JSON reading:
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString(); //result is the whole string

                progressBar.setProgress(75);
                // convert string to JSON:
                JSONObject resp = new JSONObject(result);
                JSONObject parsed = resp.getJSONObject("response");
                Log.i(ACTIVITY_NAME, "JSONObject parsed = " + parsed);

                JSONArray jsonArray = parsed.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {

                        JSONObject anObject = jsonArray.getJSONObject(i);
                        JSONObject articleFields = anObject.getJSONObject("fields");

                        // Add JSON results to article model
                        articleTitle = anObject.optString("webTitle");
                        articleUrl = anObject.optString("webUrl");
                        articleThumbnail = articleFields.optString("thumbnail");
                        articleSection = anObject.optString("sectionName");
                        articleId = anObject.optString("id");
                        date = anObject.optString("webPublicationDate");
                        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);
                        articleDate = formatter.format(Objects.requireNonNull(parser.parse(date)));

                        ArticleModel articleModel = new ArticleModel(articleId, articleTitle, articleUrl, articleThumbnail, articleSection, articleDate);
                        articleList.add(articleModel);

                    } catch (JSONException e) {
                        Log.e(ACTIVITY_NAME, "JSONException ERROR: " + e);

                    }
                    progressBar.setProgress(95);
                }

            } catch (Exception e) {
                Log.e(ACTIVITY_NAME, "ERROR: " + e);
            }

            return "Done";
        }

        public void onProgressUpdate(Integer... args) {
            progressBar.setVisibility(View.VISIBLE);
        }

        public void onPostExecute(String fromDoInBackground) {
            // Update the listview with all the articles
            listAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(fromDoInBackground);

        }
    }


    private String buildUrl(String search) {
        articleList.clear();
        listAdapter.notifyDataSetChanged();
        return "https://content.guardianapis.com/search?q=" + search + "&api-key=099b3bf9-74f6-4441-b4ef-fb8ec4aabb46&page-size=50&show-fields=thumbnail";
    }

}
