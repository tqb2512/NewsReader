package com.tqb.newsreader.backend.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tqb.newsreader.MainActivity;
import com.tqb.newsreader.R;
import com.tqb.newsreader.backend.RSSItem;

import java.util.List;

public class NewsFeedAdapter extends RecyclerView.Adapter{

    private final RSSItem[] items;

    private final Context context;

    public NewsFeedAdapter(Context context, RSSItem[] items) {
        this.context = context;
        this.items = items;
    }

    public NewsFeedAdapter(Context context, List<RSSItem> items) {
        this.context = context;
        this.items = items.toArray(new RSSItem[0]);
    }


    @Override
    public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.news_feed_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(androidx.recyclerview.widget.RecyclerView.ViewHolder holder, int position) {
        RSSItem item = items[position];

        TextView title = ((ViewHolder) holder).title;
        TextView source = ((ViewHolder) holder).source;
        TextView date = ((ViewHolder) holder).date;
        //TextView topic = ((ViewHolder) holder).topic;
        ImageView image = ((ViewHolder) holder).image;
        TextView description = ((ViewHolder) holder).description;

        description.setText(item.getDescription());
        title.setText(item.getTitle());
        source.setText(item.getSource());
        date.setText(item.getPubDate());
        //topic.setText(item.getCategory());
        if (!item.getImage().isEmpty())
            Picasso.get().load(item.getImage()).fit().centerCrop().into(image);
        if (description.getText().length() > 150) {
            description.setText(description.getText().subSequence(0, 100) + "...");
        }
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private final TextView title;
        private final TextView source;
        private final TextView date;
        //private final TextView topic;
        private final TextView description;
        private final ImageView image;
        private String link;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.news_feed_title);
            source = itemView.findViewById(R.id.news_feed_src);
            date = itemView.findViewById(R.id.news_feed_date);
            //topic = itemView.findViewById(R.id.news_feed_topic);
            image = itemView.findViewById(R.id.news_feed_img);
            description = itemView.findViewById(R.id.news_feed_des);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RSSItem item = items[getAdapterPosition()];
                    link = item.getLink();
                    MainActivity mainActivity = (MainActivity) context;
                    MainActivity.openUrl(context, link);
                }
            });
        }
    }
}
