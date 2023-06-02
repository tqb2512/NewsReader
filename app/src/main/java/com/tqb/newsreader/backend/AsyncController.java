package com.tqb.newsreader.backend;

import android.content.Context;
import android.os.AsyncTask;

import com.tqb.newsreader.backend.thanhnien.ThanhNien;

public class AsyncController extends AsyncTask<AsyncParam, Void, Void> {
    Controller controller;

    public void latest() {
        controller.latest();
    }

    public void baseOnFavorite() {
        controller.baseOnFavorite();
    }

    @Override
    protected Void doInBackground(AsyncParam... asyncParams) {
        controller = asyncParams[0].getController();
        String type = asyncParams[0].getType();
        switch (type) {
            case "latest":
                latest();
                break;
            case "baseOnFavorite":
                baseOnFavorite();
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        controller.logToConsole();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

}
