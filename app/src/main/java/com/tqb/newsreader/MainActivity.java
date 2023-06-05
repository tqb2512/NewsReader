package com.tqb.newsreader;

import static com.tqb.newsreader.backend.AsyncParam.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tqb.newsreader.backend.AsyncController;
import com.tqb.newsreader.backend.AsyncParam;
import com.tqb.newsreader.backend.Controller;
import com.tqb.newsreader.backend.DatabaseHandler;
import com.tqb.newsreader.backend.RSSItem;
import com.tqb.newsreader.backend.adapter.NewsFeedAdapter;
import com.tqb.newsreader.backend.adapter.TopicAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MainActivity extends AppCompatActivity {

    RecyclerView topicRecyclerView;
    RecyclerView newsFeedRecyclerView;
    NewsFeedAdapter newsFeedAdapter;
    TopicAdapter topicAdapter;
    RSSItem[] items;
    String selectedTopic = "Latest";
    String[] topics = {"Latest", "World", "Business", "Technology", "Entertainment", "Sports", "Science", "Health"};
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout constraintLayout = new ConstraintLayout(this);

        topicRecyclerView = findViewById(R.id.topic_recycler);
        topicAdapter = new TopicAdapter(this, topics);
        topicRecyclerView.setAdapter(topicAdapter);
        topicRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    public void setTopic(String topic)
    {
        //clear recycler view
        @SuppressLint("StaticFieldLeak") AsyncController asyncController = new AsyncController() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                items = db.getNews();
                newsFeedRecyclerView = findViewById(R.id.news_feed);
                newsFeedAdapter = new NewsFeedAdapter(controller.getContext(), items);
                newsFeedRecyclerView.setAdapter(newsFeedAdapter);
                newsFeedRecyclerView.setLayoutManager(new LinearLayoutManager(controller.getContext(), LinearLayoutManager.VERTICAL, false));
            }
        };
        if (topic.equals("Latest"))
        {
            asyncController.execute(new AsyncParam(new Controller(this), "latest", ""));
        }
        else
        {
            asyncController.execute(new AsyncParam(new Controller(this), "byCategory", topic.toLowerCase()));
        }
    }
}