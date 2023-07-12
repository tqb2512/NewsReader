package com.tqb.newsreader.backend.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.recyclerview.widget.RecyclerView;

import com.tqb.newsreader.MainActivity;
import com.tqb.newsreader.NewsFeed;
import com.tqb.newsreader.R;

public class TopicAdapter extends RecyclerView.Adapter{

    private final String[] topics;
    private int selectedPosition = 0;
    private final Context context;

    public TopicAdapter(Context context, String[] topics) {
        this.context = context;
        this.topics = topics;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.topic_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String topic = topics[position];
        RadioButton topicName = ((ViewHolder) holder).topicName;
        switch (topic)
        {
            case "Latest":
                topicName.setText(context.getString(R.string.latest));
                break;
            case "World":
                topicName.setText(context.getString(R.string.world));
                break;
            case "Business":
                topicName.setText(context.getString(R.string.business));
                break;
            case "Technology":
                topicName.setText(context.getString(R.string.technology));
                break;
            case "Sports":
                topicName.setText(context.getString(R.string.sports));
                break;
            case "Science":
                topicName.setText(context.getString(R.string.science));
                break;
            case "Health":
                topicName.setText(context.getString(R.string.health));
                break;
            case "Entertainment":
                topicName.setText(context.getString(R.string.entertainment));
                break;
            case "Football":
                topicName.setText(context.getString(R.string.football));
                break;
            case "Law":
                topicName.setText(context.getString(R.string.law));
                break;
            case "Education":
                topicName.setText(context.getString(R.string.education));
                break;
            case "Car":
                topicName.setText(context.getString(R.string.car));
                break;
            default:
                topicName.setText(topic);
                break;
        }
        topicName.setChecked(selectedPosition == position);
        topicName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsFeed.setTopic(topics[position]);
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return topics.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton topicName;
        public ViewHolder(View itemView) {
            super(itemView);
            topicName = itemView.findViewById(R.id.recycler_button);

            topicName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
