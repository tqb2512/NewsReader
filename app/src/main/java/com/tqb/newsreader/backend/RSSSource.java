package com.tqb.newsreader.backend;

import android.content.Context;

import com.google.gson.JsonObject;
import com.tqb.newsreader.R;

import java.io.File;

public class RSSSource {
    private String topic;
    private String[] urls;
    private Context context;

    public RSSSource(String topic, String[] urls, Context context) {
        this.topic = topic;
        this.urls = urls;
        this.context = context;
    }

    public RSSSource(String topic, Context context)
    {
        JsonObject sources = readSourcesFromFile(context);
        this.urls = new String[5];
        switch (topic)
        {
            case "latest":
                if (sources.get("ThanhNien").getAsInt() == 1)
                    this.urls[0] = "https://thanhnien.vn/rss/home.rss";
                if (sources.get("TuoiTre").getAsInt() == 1)
                    this.urls[1] = "https://tuoitre.vn/rss/tin-moi-nhat.rss";
                if (sources.get("VNExpress").getAsInt() == 1)
                    this.urls[2] = "https://vnexpress.net/rss/tin-moi-nhat.rss";
                if (sources.get("VTC").getAsInt() == 1)
                    this.urls[3] = "https://vtc.vn/rss/feed.rss";
                break;
            case "world":
                if (sources.get("ThanhNien").getAsInt() == 1)
                    this.urls[0] = "https://thanhnien.vn/rss/the-gioi.rss";
                if (sources.get("TuoiTre").getAsInt() == 1)
                    this.urls[1] = "https://tuoitre.vn/rss/the-gioi.rss";
                if (sources.get("VNExpress").getAsInt() == 1)
                    this.urls[2] = "https://vnexpress.net/rss/the-gioi.rss";
                if (sources.get("VTC").getAsInt() == 1)
                    this.urls[3] = "https://vtc.vn/rss/the-gioi.rss";
                break;
            case "business":
                if (sources.get("ThanhNien").getAsInt() == 1)
                    this.urls[0] = "https://thanhnien.vn/rss/kinh-te.rss";
                if (sources.get("TuoiTre").getAsInt() == 1)
                    this.urls[1] = "https://tuoitre.vn/rss/kinh-doanh.rss";
                if (sources.get("VNExpress").getAsInt() == 1)
                    this.urls[2] = "https://vnexpress.net/rss/kinh-doanh.rss";
                if (sources.get("VTC").getAsInt() == 1)
                    this.urls[3] = "https://vtc.vn/rss/kinh-te.rss";
                break;
            case "technology":
                if (sources.get("ThanhNien").getAsInt() == 1)
                    this.urls[0] = "https://thanhnien.vn/rss/cong-nghe.rss";
                if (sources.get("TuoiTre").getAsInt() == 1)
                    this.urls[1] = "https://tuoitre.vn/rss/nhip-song-so.rss";
                if (sources.get("VNExpress").getAsInt() == 1)
                    this.urls[2] = "https://vnexpress.net/rss/so-hoa.rss";
                if (sources.get("VTC").getAsInt() == 1)
                    this.urls[3] = "https://vtc.vn/rss/khoa-hoc-cong-nghe.rss";
                break;
            case "entertainment":
                if (sources.get("ThanhNien").getAsInt() == 1)
                    this.urls[0] = "https://thanhnien.vn/rss/giai-tri.rss";
                if (sources.get("TuoiTre").getAsInt() == 1)
                    this.urls[1] = "https://tuoitre.vn/rss/giai-tri.rss";
                if (sources.get("VNExpress").getAsInt() == 1)
                    this.urls[2] = "https://vnexpress.net/rss/giai-tri.rss";
                if (sources.get("VTC").getAsInt() == 1)
                    this.urls[3] = "https://vtc.vn/rss/van-hoa-giai-tri.rss";
                break;
            case "sports":
                if (sources.get("ThanhNien").getAsInt() == 1)
                    this.urls[0] = "https://thanhnien.vn/rss/thethao.rss";
                if (sources.get("TuoiTre").getAsInt() == 1)
                    this.urls[1] = "https://tuoitre.vn/rss/the-thao.rss";
                if (sources.get("VNExpress").getAsInt() == 1)
                    this.urls[2] = "https://vnexpress.net/rss/the-thao.rss";
                if (sources.get("VTC").getAsInt() == 1)
                    this.urls[3] = "https://vtc.vn/rss/the-thao.rss";
                break;
            case "health":
                if (sources.get("ThanhNien").getAsInt() == 1)
                    this.urls[0] = "https://thanhnien.vn/rss/suc-khoe.rss";
                if (sources.get("TuoiTre").getAsInt() == 1)
                    this.urls[1] = "https://tuoitre.vn/rss/suc-khoe.rss";
                if (sources.get("VNExpress").getAsInt() == 1)
                    this.urls[2] = "https://vnexpress.net/rss/suc-khoe.rss";
                if (sources.get("VTC").getAsInt() == 1)
                    this.urls[3] = "https://vtc.vn/rss/suc-khoe.rss";
                break;
            case "science":
                if (sources.get("ThanhNien").getAsInt() == 1)
                    this.urls[0] = "https://thanhnien.vn/rss/khoa-hoc.rss";
                if (sources.get("TuoiTre").getAsInt() == 1)
                    this.urls[1] = "https://tuoitre.vn/rss/khoa-hoc.rss";
                if (sources.get("VNExpress").getAsInt() == 1)
                    this.urls[2] = "https://vnexpress.net/rss/khoa-hoc.rss";
                if (sources.get("VTC").getAsInt() == 1)
                    this.urls[3] = "https://vtc.vn/rss/khoa-hoc-cong-nghe.rss";
                break;
            default:
                this.urls = new String[]{
                        "https://news.google.com/rss/search?q=" + topic + "?hl%3Dvi&gl=VN&ceid=VN:vi&hl=vi"
                };
        }
    }

    public String getTopic() {
        return topic;
    }

    public String[] getUrl() {
        return urls;
    }

    public static JsonObject readSourcesFromFile(Context context) {
        JsonObject jsonObject = new JsonObject();
        File file = new File(context.getFilesDir(), "sources.txt");
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
}
