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
        this.urls = new String[8];
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
                if (sources.get("DocBao").getAsInt() == 1)
                    this.urls[4] = "https://docbao.vn/rss/trang-chu.rss";
                if (sources.get("TheThao247").getAsInt() == 1)
                    this.urls[5] = "https://thethao247.vn/trang-chu.rss";
                if (sources.get("TienPhong").getAsInt() == 1)
                    this.urls[6] = "https://tienphong.vn/rss/home.rss";
                if (sources.get("NguoiLaoDong").getAsInt() == 1)
                    this.urls[7] = "https://nld.com.vn/tin-moi-nhat.rss";
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
                if (sources.get("DocBao").getAsInt() == 1)
                    this.urls[4] = "https://docbao.vn/rss/the-gioi.rss";
                if (sources.get("TienPhong").getAsInt() == 1)
                    this.urls[6] = "https://tienphong.vn/rss/the-gioi-5.rss";
                if (sources.get("NguoiLaoDong").getAsInt() == 1)
                    this.urls[7] = "https://nld.com.vn/thoi-su-quoc-te.rss";
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
                if (sources.get("DocBao").getAsInt() == 1)
                    this.urls[4] = "https://docbao.vn/rss/kinh-te.rss";
                if (sources.get("TienPhong").getAsInt() == 1)
                    this.urls[6] = "https://tienphong.vn/rss/kinh-te-3.rss";
                if (sources.get("NguoiLaoDong").getAsInt() == 1)
                    this.urls[7] = "https://nld.com.vn/kinh-te.rss";
                break;
            case "technology":
                if (sources.get("ThanhNien").getAsInt() == 1)
                    this.urls[0] = "https://thanhnien.vn/rss/cong-nghe-game.rss";
                if (sources.get("TuoiTre").getAsInt() == 1)
                    this.urls[1] = "https://tuoitre.vn/rss/nhip-song-so.rss";
                if (sources.get("VNExpress").getAsInt() == 1)
                    this.urls[2] = "https://vnexpress.net/rss/so-hoa.rss";
                if (sources.get("VTC").getAsInt() == 1)
                    this.urls[3] = "https://vtc.vn/rss/khoa-hoc-cong-nghe.rss";
                if (sources.get("DocBao").getAsInt() == 1)
                    this.urls[4] = "https://docbao.vn/rss/cong-nghe.rss";
                if (sources.get("TienPhong").getAsInt() == 1)
                    this.urls[6] = "https://tienphong.vn/rss/cong-nghe-khoa-hoc-46.rss";
                if (sources.get("NguoiLaoDong").getAsInt() == 1)
                    this.urls[7] = "https://nld.com.vn/cong-nghe.rss";
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
                if (sources.get("DocBao").getAsInt() == 1)
                    this.urls[4] = "https://docbao.vn/rss/giai-tri.rss";
                if (sources.get("TienPhong").getAsInt() == 1)
                    this.urls[6] = "https://tienphong.vn/rss/giai-tri-36.rss";
                if (sources.get("NguoiLaoDong").getAsInt() == 1)
                    this.urls[7] = "https://nld.com.vn/giai-tri.rss";
                break;
            case "sports":
                if (sources.get("ThanhNien").getAsInt() == 1)
                    this.urls[0] = "https://thanhnien.vn/rss/the-thao.rss";
                if (sources.get("TuoiTre").getAsInt() == 1)
                    this.urls[1] = "https://tuoitre.vn/rss/the-thao.rss";
                if (sources.get("VNExpress").getAsInt() == 1)
                    this.urls[2] = "https://vnexpress.net/rss/the-thao.rss";
                if (sources.get("VTC").getAsInt() == 1)
                    this.urls[3] = "https://vtc.vn/rss/the-thao.rss";
                if (sources.get("TheThao247").getAsInt() == 1)
                    this.urls[5] = "https://thethao247.vn/the-thao-24h.rss";
                if (sources.get("TienPhong").getAsInt() == 1)
                    this.urls[6] = "https://tienphong.vn/rss/the-thao-11.rss";
                if (sources.get("NguoiLaoDong").getAsInt() == 1)
                    this.urls[7] = "https://nld.com.vn/the-thao.rss";
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
                if (sources.get("DocBao").getAsInt() == 1)
                    this.urls[4] = "https://docbao.vn/rss/gioi-tinh.rss";
                if (sources.get("TienPhong").getAsInt() == 1)
                    this.urls[6] = "https://tienphong.vn/rss/suc-khoe-210.rss";
                if (sources.get("NguoiLaoDong").getAsInt() == 1)
                    this.urls[7] = "https://nld.com.vn/suc-khoe.rss";
                break;
            case "science":
                if (sources.get("TuoiTre").getAsInt() == 1)
                    this.urls[1] = "https://tuoitre.vn/rss/khoa-hoc.rss";
                if (sources.get("VNExpress").getAsInt() == 1)
                    this.urls[2] = "https://vnexpress.net/rss/khoa-hoc.rss";
                if (sources.get("VTC").getAsInt() == 1)
                    this.urls[3] = "https://vtc.vn/rss/khoa-hoc-cong-nghe.rss";
                break;
            case "football":
                if (sources.get("TheThao247").getAsInt() == 1)
                    this.urls[0] = "https://thethao247.vn/bong-da.rss";
                if (sources.get("TienPhong").getAsInt() == 1)
                    this.urls[1] = "https://tienphong.vn/rss/the-thao-bong-da-49.rss";
                break;
            case "car":
                if (sources.get("TheThao247").getAsInt() == 1)
                    this.urls[0] = "https://thethao247.vn/xe-co-c191.rss";
                if (sources.get("VNExpress").getAsInt() == 1)
                    this.urls[1] = "https://vnexpress.net/rss/oto-xe-may.rss";
                if (sources.get("VTC").getAsInt() == 1)
                    this.urls[2] = "https://vtc.vn/rss/oto-xe-may.rss";
                if (sources.get("DocBao").getAsInt() == 1)
                    this.urls[3] = "https://docbao.vn/rss/o-to-xe-may.rss";
                if (sources.get("TuoiTre").getAsInt() == 1)
                    this.urls[4] = "https://tuoitre.vn/rss/xe.rss";
                if (sources.get("ThanhNien").getAsInt() == 1)
                    this.urls[5] = "https://thanhnien.vn/rss/xe.rss";
                if (sources.get("TienPhong").getAsInt() == 1)
                    this.urls[6] = "https://tienphong.vn/rss/xe-113.rss";
                break;
            case "law":
                if (sources.get("VNExpress").getAsInt() == 1)
                    this.urls[1] = "https://vnexpress.net/rss/phap-luat.rss";
                if (sources.get("VTC").getAsInt() == 1)
                    this.urls[2] = "https://vtc.vn/rss/phap-luat.rss";
                if (sources.get("DocBao").getAsInt() == 1)
                    this.urls[3] = "https://docbao.vn/rss/phap-luat.rss";
                if (sources.get("TuoiTre").getAsInt() == 1)
                    this.urls[4] = "https://tuoitre.vn/rss/phap-luat.rss";
                if (sources.get("TienPhong").getAsInt() == 1)
                    this.urls[6] = "https://tienphong.vn/rss/phap-luat-12.rss";
                if (sources.get("NguoiLaoDong").getAsInt() == 1)
                    this.urls[7] = "https://nld.com.vn/phap-luat.rss";
                break;
            case "education":
                if (sources.get("VNExpress").getAsInt() == 1)
                    this.urls[1] = "https://vnexpress.net/rss/giao-duc.rss";
                if (sources.get("VTC").getAsInt() == 1)
                    this.urls[2] = "https://vtc.vn/rss/giao-duc.rss";
                if (sources.get("TuoiTre").getAsInt() == 1)
                    this.urls[4] = "https://tuoitre.vn/rss/giao-duc.rss";
                if (sources.get("ThanhNien").getAsInt() == 1)
                    this.urls[5] = "https://thanhnien.vn/rss/giao-duc.rss";
                if (sources.get("TienPhong").getAsInt() == 1)
                    this.urls[6] = "https://tienphong.vn/rss/giao-duc-71.rss";
                if (sources.get("NguoiLaoDong").getAsInt() == 1)
                    this.urls[7] = "https://nld.com.vn/giao-duc-khoa-hoc.rss";
                break;
            default:
                this.urls = new String[]{
                        "https://news.google.com/rss/search?q=" + topic + "?hl%3Dvi&gl=VN&ceid=VN:vi&hl=vi"
                };
                break;
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
