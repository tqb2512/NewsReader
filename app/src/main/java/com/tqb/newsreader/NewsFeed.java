package com.tqb.newsreader;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tqb.newsreader.backend.RSSAsyncParam;
import com.tqb.newsreader.backend.RSSFeed;
import com.tqb.newsreader.backend.RSSItem;
import com.tqb.newsreader.backend.ReceiveRSS;
import com.tqb.newsreader.backend.adapter.NewsFeedAdapter;
import com.tqb.newsreader.backend.adapter.TopicAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFeed#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFeed extends Fragment {

    private static Context context;
    static RecyclerView topicRecyclerView;
    static RecyclerView newsFeedRecyclerView;
    static NewsFeedAdapter newsFeedAdapter;
    static TopicAdapter topicAdapter;
    public static List<RSSItem> items;
    String[] topics = {"Latest", "World", "Business", "Technology", "Entertainment", "Sports", "Science", "Health"};

    public NewsFeed(Context context) {
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        ConstraintLayout constraintLayout = new ConstraintLayout(context);
        topicRecyclerView = view.findViewById(R.id.topic_recycler);
        topicAdapter = new TopicAdapter(context, topics);
        topicRecyclerView.setAdapter(topicAdapter);
        topicRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        newsFeedRecyclerView = view.findViewById(R.id.news_feed);
        newsFeedRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        ReceiveRSS receiveRSS = new ReceiveRSS();
        MainActivity.loadingDialog = new AlertDialog.Builder(context)
                .setView(R.layout.loading_dialog)
                .setCancelable(false)
                .create();
        MainActivity.loadingDialog.show();
        receiveRSS.execute(new RSSAsyncParam(context, "latest"));
        return view;
    }

    public static void setFeed(Context context, RSSFeed feed) {
        items = feed.getItems();
        newsFeedAdapter = new NewsFeedAdapter(context, items);
        newsFeedRecyclerView.setAdapter(newsFeedAdapter);
    }

    public static void setTopic(String topic) {
        MainActivity.loadingDialog = new AlertDialog.Builder(context)
                .setView(R.layout.loading_dialog)
                .setCancelable(false)
                .create();
        MainActivity.loadingDialog.show();
        ReceiveRSS receiveRSS = new ReceiveRSS();
        receiveRSS.execute(new RSSAsyncParam(context, topic.toLowerCase()));
    }
}