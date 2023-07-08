package com.tqb.newsreader.backend.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.tqb.newsreader.R;

public class LanguageAdapter extends RecyclerView.Adapter{

    private final String[] languages;
    private int selectedPosition = 0;
    private final Context context;
    private final AppCompatActivity activity;

    public LanguageAdapter(Context context, String[] languages, AppCompatActivity activity) {
        this.context = context;
        this.languages = languages;
        this.activity = activity;
        SharedPreferences sharedPreferences = context.getSharedPreferences("language", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "en");
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].equals(language)) {
                selectedPosition = i;
                break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.language_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String language = languages[position];
        TextView languageName = ((ViewHolder) holder).languageName;
        MaterialRadioButton languageButton = ((ViewHolder) holder).languageButton;
        switch (language) {
            case "en":
                languageName.setText("English");
                break;
            case "vi":
                languageName.setText("Tiếng Việt");
                break;
            default:
                languageName.setText("Unknown");
                break;
        }
        languageButton.setChecked(selectedPosition == position);
        languageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                SharedPreferences sharedPreferences = context.getSharedPreferences("language", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("language", language);
                editor.apply();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return languages.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView languageName;
        public MaterialRadioButton languageButton;

        public ViewHolder(View itemView) {
            super(itemView);
            languageName = itemView.findViewById(R.id.language_name);
            languageButton = itemView.findViewById(R.id.language_radio_button);
        }
    }


}
