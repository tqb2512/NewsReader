package com.tqb.newsreader.backend.thanhnien;

import com.tqb.newsreader.backend.RSSFeed;

import javax.security.auth.callback.Callback;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {
    @GET("/rss/home.rss")
    Call<RSSFeed> latest();

    @GET("/rss/the-gioi.rss")
    Call<RSSFeed> world();

    @GET("/rss/suc-khoe.rss")
    Call<RSSFeed> health();
}
