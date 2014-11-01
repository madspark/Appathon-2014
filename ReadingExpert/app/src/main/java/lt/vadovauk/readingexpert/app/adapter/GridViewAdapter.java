package lt.vadovauk.readingexpert.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lt.vadovauk.readingexpert.app.R;
import lt.vadovauk.readingexpert.app.domain.Story;

public class GridViewAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private ArrayList<Story> stories;
    Activity activity;

    public GridViewAdapter(Activity activity, ArrayList<Story> stories) {
        this.stories = stories;
        this.activity = activity;

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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.storyTitleTxt.setText("LEVEL " + position);
        Picasso.with(activity).load(stories.get(position).getImageSource()).into(holder.storyImg);

        return convertView;
    }

    static class ViewHolder {
        TextView storyTitleTxt;
        ImageView storyImg;
    }
}
