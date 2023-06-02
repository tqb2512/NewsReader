package com.tqb.newsreader.backend.thanhnien;

import android.content.Context;

import com.tqb.newsreader.backend.DatabaseHandler;
import com.tqb.newsreader.backend.RSSFeed;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class ThanhNien implements Callback<RSSFeed> {

    private static final String BASE_URL = "https://thanhnien.vn";
    private static Context context;
    public static boolean isReady = false;

    public static RSSFeed rssFeed = new RSSFeed();

    public void latest(Context context)
    {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);

        Call<RSSFeed> call = api.latest();
        call.enqueue(this);
    }

    public void byCategory(Context context, String category)
    {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);
        Call<RSSFeed> call;
        switch (category)
        {
            case "world":
                call = api.world();
                call.enqueue(this);
                break;
            case "health":
                call = api.health();
                call.enqueue(this);
                break;
        }
    }


    @Override
    public void onResponse(Call<RSSFeed> call, Response<RSSFeed> response) {
        if (response.isSuccessful()) {
            RSSFeed rss = response.body();
            DatabaseHandler db = new DatabaseHandler(context);
            db.importNews(rss);
            RSSFeed rssFeed = response.body();
            isReady = true;
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<RSSFeed> call, Throwable t) {
        t.printStackTrace();
    }

}
