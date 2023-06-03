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

    public void byCategory(String category) {
        controller.byCategory(category);
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
            case "byCategory":
                byCategory(asyncParams[0].getCategory());
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

}
