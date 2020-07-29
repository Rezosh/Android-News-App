package com.example.finalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

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

public class MainFragment extends Fragment {
    private static final String ACTIVITY_NAME = "MainFragment";
    private SearchView.OnQueryTextListener queryTextListener;
    List<ArticleModel> articleList = new ArrayList<>();
    ListView listView;
    ListAdapter listAdapter;
    ProgressBar progressBar;
    MyHTTPRequest myHTTPRequest;
    SQLiteDatabase db;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_main, container, false);

        if(getActivity() != null) {
            getActivity().setTitle("Home");
        }

        listView = mainView.findViewById(R.id.mainActivity_ListView);
        progressBar = mainView.findViewById(R.id.main_progressBar);

        myHTTPRequest = new MyHTTPRequest();
        myHTTPRequest.execute("https://content.guardianapis.com/search?api-key=099b3bf9-74f6-4441-b4ef-fb8ec4aabb46&page-size=50&show-fields=thumbnail");

        listAdapter = new ListAdapter();
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            ArticleModel selectedArticle = articleList.get(position);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setTitle(selectedArticle.getTitle())
                    .setMessage("Url: " + selectedArticle.getUrl() + "\nSection: " + selectedArticle.getSection())
                    .setNegativeButton("Go Back", (dialog, which) -> dialog.cancel())
                    .setPositiveButton("View Article In Browser", ((dialog, which) -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(selectedArticle.getUrl())))))
                    .create()
                    .show();

        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            ArticleModel selectedArticle = articleList.get(position);

            DBConnection dbConnection = new DBConnection(getContext());
            db = dbConnection.getWritableDatabase();

            dbConnection.insertArticle(selectedArticle);
            Log.i(ACTIVITY_NAME, "Article successfully added to database");

            Snackbar.make(view, "Added article to favorites", Snackbar.LENGTH_LONG).show();
            return true;
        });


        setHasOptionsMenu(true);
        return mainView;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()){
            case R.id.home_toolbar_help:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Help");
                alertDialogBuilder.setMessage("This page displays articles of the day unless specifically searched." +
                        " Click on a article to display more information and hold down on them to add to " +
                        "favorites.");
                alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                alertDialogBuilder.create().show();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate menu
        menu.clear();
        menuInflater.inflate(R.menu.toolbar_menu_home, menu);
        MenuItem menuItem = menu.findItem(R.id.toolbar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
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

        private int lastPosition = -1;

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
            Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            newView.startAnimation(animation);
            lastPosition = position;

            return newView;
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class MyHTTPRequest extends AsyncTask<String, Integer, String> {
        String articleTitle;
        String articleUrl;
        String articleThumbnail;
        String articleSection;
        String articleId;


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
                Log.i("MainActivity", "JSONObject parsed = " + parsed);

                JSONArray jsonArray = parsed.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {

                        JSONObject anObject = jsonArray.getJSONObject(i);
                        JSONObject articleFields = anObject.getJSONObject("fields");

                        articleTitle = anObject.optString("webTitle");
                        articleUrl = anObject.optString("webUrl");
                        articleThumbnail = articleFields.optString("thumbnail");
                        articleSection = anObject.optString("sectionName");
                        articleId = anObject.optString("id");

                        ArticleModel articleModel = new ArticleModel(articleId, articleTitle, articleUrl, articleThumbnail, articleSection);
                        articleList.add(articleModel);

                    } catch (JSONException e) {
                        Log.e("MainActivity", "JSONException ERROR: " + e);

                    }
                    progressBar.setProgress(95);
                }

            } catch (Exception e) {
                Log.e("MainActivity", "ERROR: " + e);
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
