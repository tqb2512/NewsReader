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

public class SourcesAdapter extends RecyclerView.Adapter {
    private final JsonObject sources;
    private final Context context;

    public SourcesAdapter(Context context, JsonObject topic) {
        this.context = context;
        this.sources = topic;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.source_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String source = this.sources.keySet().toArray(new String[0])[position];
        int selected = this.sources.get(source).getAsInt();
        switch (source)
        {
            case "ThanhNien":
                ((ViewHolder) holder).source.setText(context.getString(R.string.ThanhNien));
                break;
            case "TuoiTre":
                ((ViewHolder) holder).source.setText(context.getString(R.string.TuoiTre));
                break;
            case "VNExpress":
                ((ViewHolder) holder).source.setText(context.getString(R.string.VnExpress));
                break;
            case "VTC":
                ((ViewHolder) holder).source.setText(context.getString(R.string.VTC));
                break;
            case "DocBao":
                ((ViewHolder) holder).source.setText(context.getString(R.string.DocBao));
                break;
            case "TheThao247":
                ((ViewHolder) holder).source.setText(context.getString(R.string.TheThao247));
                break;
            case "TienPhong":
                ((ViewHolder) holder).source.setText(context.getString(R.string.TienPhong));
                break;
            case "NguoiLaoDong":
                ((ViewHolder) holder).source.setText(context.getString(R.string.NguoiLaoDong));
            default:
                break;
        }
        ((ViewHolder) holder).toggle.setChecked(selected == 1);
        ((ViewHolder) holder).toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                this.sources.addProperty(source, 1);
            } else {
                this.sources.addProperty(source, 0);
            }
            saveSourcesToFile(context, this.sources);
        });
    }

    public static void saveSourcesToFile(Context context, JsonObject jsonObject) {
        File file = new File(context.getFilesDir(), "sources.txt");
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
        return this.sources.keySet().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private final TextView source;
        private final SwitchCompat toggle;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            source = itemView.findViewById(R.id.source_name);
            toggle = itemView.findViewById(R.id.source_switch);
        }
    }
}
