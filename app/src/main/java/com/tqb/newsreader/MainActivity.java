package com.tqb.newsreader;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.tqb.newsreader.backend.RSSAsyncParam;
import com.tqb.newsreader.backend.RSSFeed;
import com.tqb.newsreader.backend.RSSItem;
import com.tqb.newsreader.backend.ReceiveRSS;
import com.tqb.newsreader.backend.adapter.NewsFeedAdapter;
import com.tqb.newsreader.backend.adapter.TopicAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    static RecyclerView topicRecyclerView;
    static RecyclerView newsFeedRecyclerView;
    static NewsFeedAdapter newsFeedAdapter;
    static TopicAdapter topicAdapter;
    public static List<RSSItem> items;
    String[] topics = {"Latest", "World", "Business", "Technology", "Entertainment", "Sports", "Science", "Health"};
    private static WebView webView;
    public static AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout constraintLayout = new ConstraintLayout(this);

        topicRecyclerView = findViewById(R.id.topic_recycler);
        topicAdapter = new TopicAdapter(this, topics);
        topicRecyclerView.setAdapter(topicAdapter);
        topicRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        newsFeedRecyclerView = findViewById(R.id.news_feed);
        newsFeedRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ReceiveRSS receiveRSS = new ReceiveRSS();
        loadingDialog = new AlertDialog.Builder(this)
                .setView(R.layout.loading_dialog)
                .setCancelable(false)
                .create();
        MainActivity.loadingDialog.show();
        receiveRSS.execute(new RSSAsyncParam(this, "latest"));
    }

    public static void openUrl(Context context, String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        builder.setView(webView);
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.destroy();
            }
        });
        //set dialog width to 90% of screen
        builder.show().getWindow().setLayout((int) (context.getResources().getDisplayMetrics().widthPixels), (int) (context.getResources().getDisplayMetrics().heightPixels));

    }

    public void setTopic(String topic) {
        loadingDialog = new AlertDialog.Builder(this)
                .setView(R.layout.loading_dialog)
                .setCancelable(false)
                .create();
        MainActivity.loadingDialog.show();
        ReceiveRSS receiveRSS = new ReceiveRSS();
        receiveRSS.execute(new RSSAsyncParam(this, topic.toLowerCase()));
    }

    public static void setFeed(Context context, RSSFeed feed) {
        items = feed.getItems();
        newsFeedAdapter = new NewsFeedAdapter(context, items);
        newsFeedRecyclerView.setAdapter(newsFeedAdapter);
    }

    public static void setFeed(Context context, RSSItem[] feed) {
        newsFeedAdapter = new NewsFeedAdapter(context, feed);
        newsFeedRecyclerView.setAdapter(newsFeedAdapter);
    }
}