package com.tqb.newsreader.backend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.tqb.newsreader.MainActivity;
import com.tqb.newsreader.R;
import com.tqb.newsreader.backend.RSSItem;

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

        View view = inflater.inflate(R.layout.rv_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String topic = topics[position];
        RadioButton topicName = ((ViewHolder) holder).topicName;
        topicName.setText(topic);
        topicName.setChecked(selectedPosition == position);
        topicName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.setTopic(topics[position]);
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
