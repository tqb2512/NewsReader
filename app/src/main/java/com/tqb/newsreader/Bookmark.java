package com.tqb.newsreader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tqb.newsreader.backend.RSSItem;
import com.tqb.newsreader.backend.adapter.BookmarkAdapter;

import java.util.List;

public class Bookmark extends Fragment {
    private static Context context;
    static RecyclerView newsFeedRecyclerView;
    static BookmarkAdapter newsFeedAdapter;
    public static List<RSSItem> items;
    public static RSSItem slectedItem;

    public Bookmark(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        newsFeedRecyclerView = view.findViewById(R.id.bookmark_recycler_view);
        newsFeedRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        readNewFromFile(context);
        registerForContextMenu(newsFeedRecyclerView);
        return view;
    }

    @Override
    public void onCreateContextMenu(android.view.ContextMenu menu, View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.news_feed_cm, menu);
        MenuItem save = menu.findItem(R.id.option_save);
        save.setTitle(R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.option_save) {
            MainActivity.deleteNewFromFile(context, slectedItem);
            readNewFromFile(context);
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
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

    public static void readNewFromFile(Context context) {
        items = MainActivity.readNewsFromFile(context);
        newsFeedAdapter = new BookmarkAdapter(context, items);
        newsFeedRecyclerView.setAdapter(newsFeedAdapter);
    }

}
