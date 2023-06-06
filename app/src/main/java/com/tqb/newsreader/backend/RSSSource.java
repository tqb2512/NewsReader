package com.tqb.newsreader.backend;

public class RSSSource {
    private String topic;
    private String[] urls;

    public RSSSource(String topic, String[] urls) {
        this.topic = topic;
        this.urls = urls;
    }

    public RSSSource(String topic)
    {
        switch (topic)
        {
            case "latest":
                this.urls = new String[]{
                        "https://thanhnien.vn/rss/home.rss",
                        "https://vnexpress.net/rss/tin-moi-nhat.rss",
                        "https://tuoitre.vn/rss/tin-moi-nhat.rss"
                };
                break;
            case "world":
                this.urls = new String[]{
                        "https://thanhnien.vn/rss/the-gioi.rss",
                        "https://vnexpress.net/rss/the-gioi.rss",
                        "https://tuoitre.vn/rss/the-gioi.rss"
                };
                break;
            case "business":
                this.urls = new String[]{
                        "https://vnexpress.net/rss/kinh-doanh.rss",
                        "https://thanhnien.vn/rss/kinh-te.rss",
                        "https://tuoitre.vn/rss/kinh-doanh.rss"
                };
                break;
            case "technology":
                this.urls = new String[]{
                        "https://thanhnien.vn/rss/cong-nghe-game.rss",
                        "https://vnexpress.net/rss/so-hoa.rss",
                        "https://tuoitre.vn/rss/nhip-song-so.rss"
                };
                break;
            case "entertainment":
                this.urls = new String[]{
                        "https://thanhnien.vn/rss/giai-tri.rss",
                        "https://vnexpress.net/rss/giai-tri.rss",
                        "https://tuoitre.vn/rss/giai-tri.rss"
                };
                break;
            case "sports":
                this.urls = new String[]{
                        "https://thanhnien.vn/rss/the-thao.rss",
                        "https://vnexpress.net/rss/the-thao.rss",
                        "https://tuoitre.vn/rss/the-thao.rss"
                };
                break;
            case "health":
                this.urls = new String[]{
                        "https://thanhnien.vn/rss/suc-khoe.rss",
                        "https://vnexpress.net/rss/suc-khoe.rss",
                        "https://tuoitre.vn/rss/suc-khoe.rss"
                };
                break;
            case "science":
                this.urls = new String[]{
                        "https://thanhnien.vn/rss/khoa-hoc.rss",
                        "https://vnexpress.net/rss/khoa-hoc.rss",
                        "https://tuoitre.vn/rss/khoa-hoc.rss"
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


}
