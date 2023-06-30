package com.tqb.newsreader;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.tqb.newsreader.backend.adapter.InterestsAdapter;

import java.io.File;
import java.io.FileWriter;

public class Interests extends AppCompatActivity {

    private static InterestsAdapter interestsAdapter;
    private static RecyclerView recyclerView;
    private static JsonObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);
        ImageView back = findViewById(R.id.return_button);
        back.setOnClickListener(v -> backToMain());
        recyclerView = findViewById(R.id.interest_list);
        jsonObject = readTopicsFromFile(this);
        interestsAdapter = new InterestsAdapter(this, jsonObject);
        recyclerView.setAdapter(interestsAdapter);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
    }

    private void backToMain() {
        finish();
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

    public static JsonObject readTopicsFromFile(Context context) {
        JsonObject jsonObject = new JsonObject();
        File file = new File(context.getFilesDir(), "topics.txt");
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

}