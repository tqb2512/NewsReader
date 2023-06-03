package com.tqb.newsreader.backend;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tqb.newsreader.backend.thanhnien.ThanhNien;
import com.tqb.newsreader.backend.vnexpress.VnExpress;

import java.util.List;

public class Controller {
    private static Context context;
    private static VnExpress vnexpress = new VnExpress();
    private static ThanhNien thanhnien = new ThanhNien();
    private DatabaseHandler db;

    AsyncTask.Status status = AsyncTask.Status.FINISHED;

    public Controller(Context context) {
        this.context = context;
        db = new DatabaseHandler(context);
    }

    public void latest() {
        db.clearNews();
        thanhnien.latest(context);
        vnexpress.latest(context);
        while (thanhnien.isReady == false || vnexpress.isReady == false) {
        }
    }

    public void baseOnFavorite() {
        db.clearNews();
        List<String> favoriteCategories = db.getFavoriteCategories();
        for (String category : favoriteCategories) {
            switch (category) {
                case "world":
                    vnexpress.byCategory(context, category);
                    thanhnien.byCategory(context, category);
                    break;
                case "health":
                    vnexpress.byCategory(context, category);
                    thanhnien.byCategory(context, category);
                    break;
                case "entertainment":
                    vnexpress.byCategory(context, category);
                    thanhnien.byCategory(context, category);
                    break;
                case "life":
                    vnexpress.byCategory(context, category);
                    thanhnien.byCategory(context, category);
                    break;
                case "sport":
                    vnexpress.byCategory(context, category);
                    thanhnien.byCategory(context, category);
                    break;
            }
        }
        while (thanhnien.isReady == false || vnexpress.isReady == false) {
        }
    }

    public void byCategory(String category)
    {
        db.clearNews();
        vnexpress.byCategory(context, category);
        thanhnien.byCategory(context, category);
        while (thanhnien.isReady == false || vnexpress.isReady == false) {
        }
    }

    public void logToConsole() {
        db.logToConsole();
    }
}
