package com.tqb.newsreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.Locale;

public class Settings extends Fragment {

    private static Context context;

    public Settings(Context context)
    {
        this.context = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        TextView interests = view.findViewById(R.id.settings_item_interests);
        interests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInterestsClick();
            }
        });

        TextView sources = view.findViewById(R.id.settings_item_sources);
        sources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSourcesClick();
            }
        });

        TextView language = view.findViewById(R.id.settings_item_language);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLanguageClick();
            }
        });


        SwitchCompat darkMode = view.findViewById(R.id.dark_switch);
        SharedPreferences sharedPreferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("dark", false)) {
            darkMode.setChecked(true);
        } else {
            darkMode.setChecked(false);
        }
        darkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (darkMode.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("dark", true);
                    editor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("dark", false);
                    editor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }
        });


        TextView about = view.findViewById(R.id.settings_item_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "tqb2512@gmail.com", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void onInterestsClick () {
        Intent intent = new Intent(context, Interests.class);
        startActivityForResult(intent, 0);
    }

    public void onSourcesClick () {
        Intent intent = new Intent(context, Sources.class);
        startActivityForResult(intent, 0);
    }

    public void onLanguageClick () {
        Intent intent = new Intent(context, Language.class);
        startActivityForResult(intent, 0);
    }
}
