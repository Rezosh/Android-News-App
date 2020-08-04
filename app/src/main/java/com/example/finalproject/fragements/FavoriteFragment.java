package com.example.finalproject.fragements;

import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalproject.ArticleModel;
import com.example.finalproject.DBConnection;
import com.example.finalproject.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteFragment extends Fragment {

    private static final String ACTIVITY_NAME = "FavoriteActivity";
    List<ArticleModel> articleList;
    ListView listView;
    SQLiteDatabase db;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_favourite, container, false);

        if(getActivity() != null) {
            getActivity().setTitle("Favorites");
        }

        loadArticle();
        listView = mainView.findViewById(R.id.favoriteActivity_ListView);
        ListAdapter listAdapter = new ListAdapter();
        listView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        listView.setEmptyView(mainView.findViewById(R.id.emptyList));


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

            dbConnection.deleteArticle(selectedArticle.getEndpoint());
            Log.i(ACTIVITY_NAME, "Article successfully deleted from database");

            Snackbar.make(view, "Successfully removed article from favorites", Snackbar.LENGTH_LONG).show();

            listAdapter.notifyDataSetChanged();
            loadArticle();
            return true;
        });



        setHasOptionsMenu(true);
        return mainView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate menu
        menu.clear();
        menuInflater.inflate(R.menu.toolbar_menu_favorites, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        if (item.getItemId() == R.id.favorite_toolbar_help) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setTitle("Help");
            alertDialogBuilder.setMessage("This page displays your favorites." +
                    " Click on a article to display more information and hold down on them to remove an " +
                    "article from the list.");
            alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            alertDialogBuilder.create().show();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;

    }

    public void loadArticle() {
        DBConnection dbConnection = new DBConnection(getContext());
        articleList = dbConnection.getArticle();
    }


    private class ListAdapter extends BaseAdapter {

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
            Log.i("FavoriteActivity", "<<<<---- ADAPTER ---->>>>");
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
}
