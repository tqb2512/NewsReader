package com.tqb.newsreader.backend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tqb.newsreader.MainActivity;
import com.tqb.newsreader.R;
import com.tqb.newsreader.backend.RSSFeed;
import com.tqb.newsreader.backend.RSSItem;

import java.util.List;

public class SearchFeedAdapter extends RecyclerView.Adapter {
    private final RSSItem[] items;

    private final Context context;

    public SearchFeedAdapter(Context context, RSSItem[] items) {
        this.context = context;
        this.items = items;
    }

    public SearchFeedAdapter(Context context, List<RSSItem> items) {
        this.context = context;
        this.items = items.toArray(new RSSItem[0]);
    }


    @Override
    public SearchFeedAdapter.ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.search_view_item, parent, false);
        SearchFeedAdapter.ViewHolder viewHolder = new SearchFeedAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(androidx.recyclerview.widget.RecyclerView.ViewHolder holder, int position) {
        RSSItem item = items[position];

        TextView title = ((SearchFeedAdapter.ViewHolder) holder).title;
        TextView source = ((SearchFeedAdapter.ViewHolder) holder).source;
        TextView date = ((SearchFeedAdapter.ViewHolder) holder).date;

        title.setText(item.getTitle());
        source.setText(item.getSource());
        date.setText(item.getPubDate());
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
        private String link;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.search_item_title);
            source = itemView.findViewById(R.id.search_item_src);
            date = itemView.findViewById(R.id.search_item_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RSSItem item = items[getAdapterPosition()];
                    link = item.getLink();
                    MainActivity mainActivity = (MainActivity) context;
                    MainActivity.openUrl(context, item);
                }
            });
        }
    }
}
