package com.tqb.newsreader.backend;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="item", strict=false)
public class RSSItem {
    @Element(name="title")
    private String title;
    @Element(name="description")
    private String description;
    @Element(name="pubDate")
    private String pubDate;
    @Element(name="link")
    private String link;
    @Element(name="guid")
    private String guid;
    private String image;

    public RSSItem() {
    }

    public RSSItem(String _title, String _link, String _description, String _pubDate, String _guid, String _category, String _image, String _author, String _source, int _read, int _favorite) {
        title = _title;
        link = _link;
        description = _description;
        pubDate = _pubDate;
        guid = _guid;
        image = _image;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String _title) {
        title = _title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String _description) {
        description = _description;
    }
    public String getPubDate() {
        return pubDate;
    }
    public void setPubDate(String _pubDate) {
        pubDate = _pubDate;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String _link) {
        link = _link;
    }
    public String getGuid() {
        return guid;
    }
    public void setGuid(String _guid) {
        guid = _guid;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String _image) {
        image = _image;
    }

}
