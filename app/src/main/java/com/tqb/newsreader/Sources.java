package com.tqb.newsreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.tqb.newsreader.backend.adapter.InterestsAdapter;
import com.tqb.newsreader.backend.adapter.SourcesAdapter;

import java.io.File;
import java.io.FileWriter;
import java.util.Locale;

public class Sources extends AppCompatActivity {

    private static SourcesAdapter sourcesAdapter;
    private static RecyclerView recyclerView;
    private static JsonObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("language", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "en");
        Language.setLanguage(this, language);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);
        ImageView back = findViewById(R.id.return_button);
        back.setOnClickListener(v -> backToMain());
        recyclerView = findViewById(R.id.sources_list);
        jsonObject = readSourcesFromFile(this);
        sourcesAdapter = new SourcesAdapter(this, jsonObject);
        recyclerView.setAdapter(sourcesAdapter);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
    }

    private void backToMain() {
        finish();
    }

    public static JsonObject readSourcesFromFile(Context context) {
        JsonObject jsonObject = new JsonObject();
        File file = new File(context.getFilesDir(), "sources.txt");
        try {
            java.util.Scanner scanner = new java.util.Scanner(file);
            String jsonString = scanner.nextLine();
            scanner.close();
            jsonObject = new com.google.gson.JsonParser().parse(jsonString).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static void setLanguage(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }
}