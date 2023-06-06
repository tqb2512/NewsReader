package com.tqb.newsreader.backend;

public class RSSItem {
    private String title;
    private String link;
    private String description;
    private String pubDate;
    private String image;
    private String source;
    private String topic;

    public RSSItem(String title, String link, String pubDate, String image, String descriptionText) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.image = image;
        this.description = descriptionText;
    }

    public RSSItem() {
        this.title = "";
        this.link = "";
        this.pubDate = "";
        this.image = "";
        this.description = "";
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPubDate() {
        return pubDate;
    }
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getCategory() { return topic; }
    public void setCategory(String topic) { this.topic = topic; }
}

