package com.tqb.newsreader;

import android.annotation.SuppressLint;
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

import com.google.gson.JsonObject;
import com.tqb.newsreader.backend.RSSAsyncParam;
import com.tqb.newsreader.backend.RSSFeed;
import com.tqb.newsreader.backend.RSSItem;
import com.tqb.newsreader.backend.ReceiveRSS;
import com.tqb.newsreader.backend.adapter.NewsFeedAdapter;
import com.tqb.newsreader.backend.adapter.TopicAdapter;

import java.util.List;
public class NewsFeed extends Fragment {

    private static Context context;
    static RecyclerView topicRecyclerView;
    static RecyclerView newsFeedRecyclerView;
    static NewsFeedAdapter newsFeedAdapter;
    static TopicAdapter topicAdapter;
    public static List<RSSItem> items;

    String[] topics = {};

    public NewsFeed(Context context) {
        this.context = context;
        //read topic from main activity function
        JsonObject jsonObject = MainActivity.readTopicsFromFile(context);
        String[] temps = new String[jsonObject.size()];
        int index = 0;
        for (String key : jsonObject.keySet()) {
            if (jsonObject.get(key).getAsString().equals("1")) {
                temps[index] = key;
                index++;
            }
        }
        String[] result = new String[index];
        for (int i = 0; i < index; i++) {
            result[i] = temps[i];
        }
        topics = result;
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
        setTopic("latest");
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
        ReceiveRSS receiveRSS = new ReceiveRSS() {
            @Override
            protected void onPostExecute(String[] s) {
                super.onPostExecute(s);
                if (s != null) {
                    feed = new RSSFeed[s.length];
                    int index = 0;
                    for (String temp : s) {
                        feed[index] = parseToRSS(temp);
                        index++;
                    }
                    RSSFeed f = mergeFeed(feed);
                    f = sortItemListByPubDate(f);
                    NewsFeed.setFeed(context, f);
                }
                MainActivity.loadingDialog.dismiss();
                this.cancel(true);
            }
        };
        receiveRSS.execute(new RSSAsyncParam(context, topic.toLowerCase()));
    }
}