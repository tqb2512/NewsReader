package com.tqb.newsreader.backend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.tqb.newsreader.R;

import java.io.File;
import java.io.FileWriter;

public class InterestsAdapter extends RecyclerView.Adapter{

    private final JsonObject topic;
    private final Context context;

    public InterestsAdapter(Context context, JsonObject topic) {
        this.context = context;
        this.topic = topic;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.interest_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String topic = this.topic.keySet().toArray(new String[0])[position];
        int selected = this.topic.get(topic).getAsInt();
        ((ViewHolder) holder).topic.setText(topic);
        ((ViewHolder) holder).toggle.setChecked(selected == 1);
        ((ViewHolder) holder).toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                this.topic.addProperty(topic, 1);
            } else {
                this.topic.addProperty(topic, 0);
            }
            saveTopicsToFile(context, this.topic);
        });
    }

    public static void saveTopicsToFile(Context context, JsonObject jsonObject) {
        File file = new File(context.getFilesDir(), "topics.txt");
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonObject.toString());
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.topic.keySet().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private final TextView topic;
        private final SwitchCompat toggle;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            topic = itemView.findViewById(R.id.interest_type);
            toggle = itemView.findViewById(R.id.interest_switch);
        }
    }

}
