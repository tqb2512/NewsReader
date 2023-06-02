package com.tqb.newsreader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.tqb.newsreader.backend.AsyncController;
import com.tqb.newsreader.backend.AsyncParam;
import com.tqb.newsreader.backend.Controller;
import com.tqb.newsreader.backend.DatabaseHandler;
import com.tqb.newsreader.backend.RSSFeed;
import com.tqb.newsreader.backend.thanhnien.ThanhNien;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sử dụng database bằng cách truy vấn trực tiếp*/
        DatabaseHandler db = new DatabaseHandler(this);
        db.clearNews();

        AsyncController asyncController = new AsyncController();
        AsyncParam asyncParam = new AsyncParam(new Controller(this), "baseOnFavorite");
        asyncController.execute(asyncParam);
    }
}