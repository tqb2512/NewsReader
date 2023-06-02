package com.tqb.newsreader;

import static com.tqb.newsreader.backend.AsyncParam.controller;

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

        DatabaseHandler db = new DatabaseHandler(this);
        db.clearNews();

        //Override lại hàm onPostExecute
        AsyncController asyncController = new AsyncController() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //Thay đổi giao diện ở đây
                controller.logToConsole();
            }
        };
        asyncController.execute(new AsyncParam(new Controller(this), "latest"));

    }
}