package com.svc2uk.readingexpert.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svc2uk.readingexpert.R;
import com.svc2uk.readingexpert.domain.Story;
import com.svc2uk.readingexpert.domain.UserResult;
import com.svc2uk.readingexpert.helper.DataHelper;
import com.svc2uk.readingexpert.helper.StorageHelper;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private ArrayList<Story> stories;
    Activity activity;
    ArrayList<UserResult> results;

    public GridViewAdapter(Activity activity, ArrayList<Story> stories) {
        this.stories = stories;
        this.activity = activity;

        results = StorageHelper.readUserResults(activity);

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return stories.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.story_item, null);
            holder.storyTitleTxt = (TextView) convertView.findViewById(R.id.story_title_txt);
            holder.storyImg = (ImageView) convertView.findViewById(R.id.story_img);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.storyTitleTxt.setText("LEVEL " + (position + 1));
        Picasso.with(activity).load(stories.get(position).getImageSource()).into(holder.storyImg);

        int previous = (position == 0) ? 0 : position - 1;
        if (DataHelper.contains(results, stories.get(previous).getApiId()) || position == 0) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.white));
        } else {
            // set the image to be half transparent if the level is not unlocked.
            holder.layout.setAlpha(0.5f);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView storyTitleTxt;
        ImageView storyImg;
        RelativeLayout layout;
    }

}
