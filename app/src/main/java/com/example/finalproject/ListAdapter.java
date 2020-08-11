package com.example.finalproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {

    private static final String ACTIVITY_NAME = "ListAdapter";
    /**
     * Initialize variables
     */
    private List<ArticleModel> articleList = new ArrayList<>();
    private Context context;
    private int layout;


    /**
     *
     * @param context Context of calling activity
     * @param articleList list to populate the view with
     * @param layout layout to use on view
     */
    public ListAdapter(Context context, List<ArticleModel> articleList, int layout) {
        this.articleList = articleList;
        this.context = context;
        this.layout = layout;
    }


    @Override
    public int getCount() {
        return articleList.size();
    }

    public Object getItem(int position) {
        return articleList.get(position);
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
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            newView = layoutInflater.inflate(layout, parent, false);
        }
        Log.i(ACTIVITY_NAME, "Loaded row");

        // Set text fields
        TextView title = newView.findViewById(R.id.title);
        TextView section = newView.findViewById(R.id.subtitle);
        TextView date = newView.findViewById(R.id.date);
        date.setText(articleList.get(position).getDate());
        section.setText(articleList.get(position).getSection());
        title.setText(articleList.get(position).getTitle());

        ImageView thumbnail = newView.findViewById(R.id.image);

        // Set thumbnail image
        Glide.with(context).load(articleList.get(position).getThumbnail())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(thumbnail);

        // Scrolling animation
        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        newView.startAnimation(animation);
        lastPosition = position;

        return newView;
    }

    public void myRemove(int position){
        articleList.remove(position);
        ListAdapter.super.notifyDataSetChanged();
    }

    public void updateList(List<ArticleModel> newlist) {
        articleList.clear();
        articleList.addAll(newlist);
        this.notifyDataSetChanged();
    }

}
