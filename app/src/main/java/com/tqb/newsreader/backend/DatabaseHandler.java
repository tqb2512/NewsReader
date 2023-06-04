package com.tqb.newsreader.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Html;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "newsreader";
    private static final String TABLE_NAME = "news";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void importNews(RSSFeed rssFeed)
    {
        Log.d("DatabaseHandler", "Importing news...");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (RSSItem item : rssFeed.getItems()) {
            values.put("title", item.getTitle());
            values.put("link", item.getLink());
            values.put("description", item.getDescription());
            values.put("pubDate", item.getPubDate());
            values.put("guid", item.getGuid());
            values.put("image", item.getImage());
            db.insert(TABLE_NAME, null, values);
        }
        db.close();
    }

    public void importFavoriteCategory(String category)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM categories";
        android.database.Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(1).equals(category)) {
                    return;
                }
            } while (cursor.moveToNext());
        }
        db.close();

        Log.d("DatabaseHandler", "Importing favorite category...");
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", category);
        db.insert("categories", null, values);
        db.close();
    }

    public void importFavoriteCategories(List<String> categories)
    {
        Log.d("DatabaseHandler", "Importing favorite categories...");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (String category : categories) {
            values.put("name", category);
            db.insert("categories", null, values);
        }
        db.close();
    }

    public void clearNews()
    {
        Log.d("DatabaseHandler", "Clearing news...");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    public void clearFavoriteCategories()
    {
        Log.d("DatabaseHandler", "Clearing favorite categories...");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM categories");
        db.close();
    }

    public RSSItem[] getNews()
    {
        Log.d("DatabaseHandler", "Getting news...");
        List<RSSItem> items = new ArrayList<RSSItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        android.database.Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                RSSItem item = new RSSItem();
                item.setTitle(cursor.getString(1));
                item.setLink(cursor.getString(2));
                item.setDescription(cursor.getString(3));
                item.setPubDate(cursor.getString(4));
                item.setGuid(cursor.getString(5));
                item.setImage(cursor.getString(6));
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items.toArray(new RSSItem[items.size()]);
    }

    public List<String> getFavoriteCategories()
    {
        Log.d("DatabaseHandler", "Getting favorite categories...");
        List<String> categories = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM categories";
        android.database.Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return categories;
    }

    public void logToConsole()
    {
        Log.d("DatabaseHandler", "Reading all news...");
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        android.database.Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Log.d("DatabaseHandler", cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public void logCategoryToConsole()
    {
        Log.d("DatabaseHandler", "Reading all categories...");
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM categories";
        android.database.Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Log.d("DatabaseHandler", cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public void dropAllTables()
    {
        Log.d("DatabaseHandler", "Dropping all tables...");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS categories");
        db.execSQL("DROP TABLE IF EXISTS data_status");
        db.close();
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT,"
                + "link TEXT,"
                + "description TEXT,"
                + "pubDate TEXT,"
                + "guid TEXT,"
                + "image TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE IF NOT EXISTS categories ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
