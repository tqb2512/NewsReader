package com.tqb.newsreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.moshi.Json;
import com.tqb.newsreader.backend.RSSAsyncParam;
import com.tqb.newsreader.backend.RSSFeed;
import com.tqb.newsreader.backend.RSSItem;
import com.tqb.newsreader.backend.ReceiveRSS;
import com.tqb.newsreader.backend.adapter.NewsFeedAdapter;
import com.tqb.newsreader.backend.adapter.TopicAdapter;
import com.tqb.newsreader.backend.broadcast.ConnectivityReceiver;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static WebView webView;
    public static AlertDialog loadingDialog;
    BottomNavigationView bottomNavigationView;
    public static SharedPreferences darkMode;
    public static SharedPreferences language;
    public static AlertDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        darkMode = getSharedPreferences("theme", MODE_PRIVATE);
        setDarkMode(darkMode.getBoolean("dark", false));
        language = getSharedPreferences("language", MODE_PRIVATE);
        setLanguage(this, language.getString("language", "en"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTopics(this);
        initSources(this);

        Fragment fragment = new NewsFeed(this);
        loadFragment(fragment);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        BroadcastReceiver broadcastReceiver = new ConnectivityReceiver();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, intentFilter);
        noInternetDialog = new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                .setMessage(R.string.no_internet_message)
                .setCancelable(false)
                .create();
    }

    public static void openUrl(Context context, RSSItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(item.getLink());
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        builder.setView(webView);
        builder.setNegativeButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveNewToFile(context, item);
            }
        });

        builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.destroy();
                dialog.dismiss();
            }
        });

        builder.show().getWindow().setLayout((int) (context.getResources().getDisplayMetrics().widthPixels), (int) (context.getResources().getDisplayMetrics().heightPixels));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(item.getItemId() == R.id.action_home) {
                loadFragment(new NewsFeed(MainActivity.this));
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
            } else if (item.getItemId() == R.id.action_search) {
                loadFragment(new Search(MainActivity.this));
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
            } else if (item.getItemId() == R.id.action_settings) {
                loadFragment(new Settings(MainActivity.this));
                bottomNavigationView.getMenu().getItem(3).setChecked(true);
            } else if (item.getItemId() == R.id.action_bookmark) {
                loadFragment(new Bookmark(MainActivity.this));
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
            }
            return false;
        }
    };
    public static void initTopics(Context context) {
        File file = new File(context.getFilesDir(), "topics.txt");
        if(!file.exists()) {
            try {
                file.createNewFile();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("Latest", "1");
                jsonObject.addProperty("World", "1");
                jsonObject.addProperty("Business", "1");
                jsonObject.addProperty("Technology", "1");
                jsonObject.addProperty("Entertainment", "1");
                jsonObject.addProperty("Sports", "1");
                jsonObject.addProperty("Science", "1");
                jsonObject.addProperty("Health", "1");
                jsonObject.addProperty("Football", "1");
                jsonObject.addProperty("Car", "1");
                jsonObject.addProperty("Law", "1");
                jsonObject.addProperty("Education", "1");
                saveTopicsToFile(context, jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void initSources(Context context) {
        File file = new File(context.getFilesDir(), "sources.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("VNExpress", "1");
                jsonObject.addProperty("ThanhNien", "1");
                jsonObject.addProperty("TuoiTre", "1");
                jsonObject.addProperty("VTC", "1");
                jsonObject.addProperty("DocBao", "1");
                jsonObject.addProperty("TheThao247", "1");
                jsonObject.addProperty("TienPhong", "1");
                jsonObject.addProperty("NguoiLaoDong", "1");
                saveSourcesToFile(context, jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setDarkMode(boolean dark) {
        if(dark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static void setLanguage(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }

    public static void saveSourcesToFile(Context context, JsonObject jsonObject) {
        File file = new File(context.getFilesDir(), "sources.txt");
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonObject.toString());
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static void saveNewToFile(Context context, RSSItem item)
    {
        Gson gson = new Gson();
        String jsonString = gson.toJson(item);
        File file = new File(context.getFilesDir(), "news.txt");
        if (!file.exists())
        {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            java.util.Scanner scanner = new java.util.Scanner(file);
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                if (line.equals(jsonString))
                {
                    scanner.close();
                    return;
                }
            }
            scanner.close();
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(jsonString + "\n");
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteNewFromFile(Context context, RSSItem item) {
        File file = new File(context.getFilesDir(), "news.txt");
        try {
            java.util.Scanner scanner = new java.util.Scanner(file);
            List<String> lines = new ArrayList<>();
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                Gson gson = new Gson();
                RSSItem newItem = gson.fromJson(line, RSSItem.class);
                if (!newItem.getTitle().equals(item.getTitle()))
                {
                    lines.add(line);
                }
            }
            scanner.close();
            FileWriter fileWriter = new FileWriter(file);
            for (String line : lines)
            {
                fileWriter.write(line + "\n");
            }
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<RSSItem> readNewsFromFile(Context context) {
        File file = new File(context.getFilesDir(), "news.txt");
        List<RSSItem> items = new ArrayList<>();
        try {
            java.util.Scanner scanner = new java.util.Scanner(file);
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                Gson gson = new Gson();
                RSSItem item = gson.fromJson(line, RSSItem.class);
                items.add(item);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}