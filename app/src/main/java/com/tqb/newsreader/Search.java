package com.tqb.newsreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tqb.newsreader.backend.RSSAsyncParam;
import com.tqb.newsreader.backend.RSSFeed;
import com.tqb.newsreader.backend.RSSItem;
import com.tqb.newsreader.backend.ReceiveRSS;
import com.tqb.newsreader.backend.adapter.SearchFeedAdapter;

import java.util.List;

public class Search extends Fragment {

    private static Context context;
    static RecyclerView searchFeedRecyclerView;
    static SearchView searchView;
    static List<RSSItem> items;

    public Search(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchFeedRecyclerView = view.findViewById(R.id.news_feed);
        searchView = view.findViewById(R.id.search_view);
        searchFeedRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                @SuppressLint("StaticFieldLeak") ReceiveRSS receiveRSS = new ReceiveRSS() {
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
                            Search.setFeed(context, f);
                        }
                        MainActivity.loadingDialog.dismiss();
                        this.cancel(true);
                    }
                };
                receiveRSS.execute(new RSSAsyncParam(context, query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return view;
    }

    public static void setFeed(Context context, RSSFeed feed) {
        items = feed.getItems();
        searchFeedRecyclerView.setAdapter(new SearchFeedAdapter(context, items));
    }

}
