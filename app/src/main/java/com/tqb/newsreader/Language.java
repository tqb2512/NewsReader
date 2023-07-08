package com.tqb.newsreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.tqb.newsreader.backend.adapter.LanguageAdapter;

import java.util.Locale;

public class Language extends AppCompatActivity {

    private static LanguageAdapter languageAdapter;
    private static RecyclerView recyclerView;
    private static String[] languages = {"en", "vi"};
    private static String selectedLanguage;
    private static String previousLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("language", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "en");
        selectedLanguage = language;
        previousLanguage = language;
        setLanguage(this, language);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ImageView back = findViewById(R.id.return_button);
        back.setOnClickListener(v -> backToMain());
        recyclerView = findViewById(R.id.language_list);
        languageAdapter = new LanguageAdapter(this, languages, this);
        recyclerView.setAdapter(languageAdapter);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
    }

    private void backToMain() {
        SharedPreferences sharedPreferences = getSharedPreferences("language", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "en");
        selectedLanguage = language;
        if (!selectedLanguage.equals(previousLanguage)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            finish();
        }
    }

    public static void setLanguage(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }
}