package com.tqb.newsreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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
        return view;
    }

    public void onInterestsClick () {
        Intent intent = new Intent(context, Interests.class);
        startActivityForResult(intent, 0);
    }
}
