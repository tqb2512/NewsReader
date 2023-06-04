package com.tqb.newsreader;

import static com.tqb.newsreader.backend.AsyncParam.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.tqb.newsreader.backend.AsyncController;
import com.tqb.newsreader.backend.AsyncParam;
import com.tqb.newsreader.backend.Controller;
import com.tqb.newsreader.backend.DatabaseHandler;
import com.tqb.newsreader.backend.RSSItem;
import com.tqb.newsreader.backend.adapter.NewsFeedAdapter;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NewsFeedAdapter adapter;
    RSSItem[] items;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout linearLayoutManager = new ConstraintLayout(this);

        //Override lại hàm onPostExecute
        @SuppressLint("StaticFieldLeak") AsyncController asyncController = new AsyncController() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                items = db.getNews();
                recyclerView = findViewById(R.id.horizontal_recycler1);
                adapter = new NewsFeedAdapter(controller.getContext(), items);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(controller.getContext(), LinearLayoutManager.VERTICAL, false));
            }
        };
        asyncController.execute(new AsyncParam(new Controller(this), "byCategory", "sport"));

    }
}