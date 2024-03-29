package com.tqb.newsreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tqb.newsreader.backend.RSSAsyncParam;
import com.tqb.newsreader.backend.RSSFeed;
import com.tqb.newsreader.backend.RSSItem;
import com.tqb.newsreader.backend.ReceiveRSS;
import com.tqb.newsreader.backend.adapter.NewsFeedAdapter;
import com.tqb.newsreader.backend.adapter.TopicAdapter;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
public class NewsFeed extends Fragment {

    private static Context context;
    static RecyclerView topicRecyclerView;
    public static RecyclerView newsFeedRecyclerView;
    static NewsFeedAdapter newsFeedAdapter;
    static TopicAdapter topicAdapter;
    public static List<RSSItem> items;
    public static RSSItem slectedItem;
    String[] topics = {};
    static String currentTopic;

    public NewsFeed(Context context) {
        this.context = context;

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
        currentTopic = "latest";
        setTopic("latest");
        registerForContextMenu(newsFeedRecyclerView);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            setTopic(currentTopic);
            swipeRefreshLayout.setRefreshing(false);
        });
        return view;
    }

    @Override
    public void onCreateContextMenu(android.view.ContextMenu menu, View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.news_feed_cm, menu);
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.option_save) {
            MainActivity.saveNewToFile(context, slectedItem);
            return true;
        } else if (item.getItemId() == R.id.option_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, slectedItem.getLink());
            startActivity(Intent.createChooser(intent, "Share"));
            return true;
        } else if (item.getItemId() == R.id.option_open_in_browser) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(slectedItem.getLink()));
            startActivity(intent);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    public static void openNewsFeedContextMenu(RSSItem item) {
        newsFeedRecyclerView.showContextMenu();
        slectedItem = item;
    }

    public static void setFeed(Context context, RSSFeed feed) {
        items = feed.getItems();
        newsFeedAdapter = new NewsFeedAdapter(context, items);
        newsFeedRecyclerView.setAdapter(newsFeedAdapter);
    }

    public static void setTopic(String topic) {
        MainActivity.loadingDialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage(context.getString(R.string.loading))
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

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (receiveRSS.getStatus() == ReceiveRSS.Status.RUNNING) {
                receiveRSS.cancel(true);
                MainActivity.loadingDialog.dismiss();
                AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogStyle)
                        .setTitle(R.string.error)
                        .setMessage(R.string.cant_connect)
                        .setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .create();
                alertDialog.show();
            }
        }, 15000);

        currentTopic = topic;
    }
}