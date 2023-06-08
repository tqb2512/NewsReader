package com.tqb.newsreader.backend;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.tqb.newsreader.MainActivity;
import com.tqb.newsreader.NewsFeed;
import com.tqb.newsreader.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ReceiveRSS extends AsyncTask<RSSAsyncParam, Void, String[]> {
    private Context context;
    private RSSFeed[] feed;
    @Override
    protected String[] doInBackground(RSSAsyncParam... rssAsyncParam) {
        context = rssAsyncParam[0].getContext();
        RSSSource source = new RSSSource(rssAsyncParam[0].getTopic());
        String[] urls = source.getUrl();
        String[] content = new String[urls.length];
        int index = 0;
        for (String tempUrl : urls) {
            try {
                URL url = new URL(tempUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                content[index] = stringBuilder.toString();
                index++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String[] s) {
        super.onPostExecute(s);
        if (s != null) {
            feed = new RSSFeed[s.length];
            int index = 0;
            for (String temp : s) {
                feed[index] = parseToRSS(temp);
                index++;
            }
            RSSFeed f = mergeFeed(feed);
            f = sortItemListByPubDate(f);
            NewsFeed.setFeed(context, f);
        }
        MainActivity.loadingDialog.dismiss();
        this.cancel(true);
    }

    public RSSFeed parseToRSS(String content) {
        RSSFeed feed = null;
        if (content == null) {
            return null;
        }
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = builder.parse(new InputSource(new StringReader(content)));
            org.w3c.dom.Element rootElement = xmlDocument.getDocumentElement();
            NodeList items = rootElement.getElementsByTagName("item");
            feed = new RSSFeed();
            List<RSSItem> rssItems = new ArrayList<>();
            int size = items.getLength();
            if (size > 15) {
                size = 15;
            }
            for (int i = 0; i < size; i++) {
                org.w3c.dom.Element item = (org.w3c.dom.Element) items.item(i);
                RSSItem rssItem = new RSSItem();
                rssItem.setTitle(Html.fromHtml(item.getElementsByTagName("title").item(0).getTextContent()).toString());
                rssItem.setLink(item.getElementsByTagName("link").item(0).getTextContent());
                String pubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();
                if (pubDate.contains(",")) {
                    String[] temp = pubDate.split(",");
                    rssItem.setPubDate(temp[1].trim());
                } else {
                    rssItem.setPubDate(pubDate);
                }
                Document doc = Jsoup.parse(item.getElementsByTagName("description").item(0).getTextContent());
                Element img = doc.select("img").first();
                if (img != null) {
                    rssItem.setImage(img.attr("src"));
                }
                rssItem.setDescription(Html.fromHtml(doc.body().text()).toString());
                String sourceAndTopic = rootElement.getElementsByTagName("title").item(0).getTextContent();
                if (sourceAndTopic.contains(" | ")) {
                    String[] temp = sourceAndTopic.split("\\|");
                    rssItem.setSource(temp[0].trim());
                    rssItem.setCategory(temp[1].trim());
                } else if (sourceAndTopic.contains(" - VnExpress RSS")) {
                    String[] temp = sourceAndTopic.split("-");
                    rssItem.setSource(temp[0].trim());
                    rssItem.setCategory(temp[1].trim());
                } else if (sourceAndTopic.contains("Tuổi Trẻ Online -")) {
                    String[] temp = sourceAndTopic.split("-");
                    rssItem.setSource(temp[1].trim());
                    rssItem.setCategory(temp[0].trim());
                } else if (sourceAndTopic.contains(" - Google Tin tức")) {
                    rssItem.setSource(item.getElementsByTagName("source").item(0).getTextContent());
                } else {
                    rssItem.setSource(sourceAndTopic);
                }
                rssItems.add(rssItem);
            }
            feed.setItems(rssItems);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return feed;
    }

    public RSSFeed mergeFeed(RSSFeed[] feeds) {
        RSSFeed feed = new RSSFeed();
        if (feeds == null) {
            return null;
        }
        List<RSSItem> items = new ArrayList<>();
        for (RSSFeed temp : feeds) {
            if (temp != null) {
                items.addAll(temp.getItems());
            }
        }
        feed.setItems(items);
        return feed;
    }

    public RSSFeed sortItemListByPubDate(RSSFeed feed){
        if (feed == null) {
            return null;
        }
        List<RSSItem> items = feed.getItems();
        for (int i = 0; i < items.size(); i++) {
            for (int j = i; j < items.size(); j++) {
                if (items.get(i).getPubDate().compareTo(items.get(j).getPubDate()) < 0) {
                    RSSItem temp = items.get(i);
                    items.set(i, items.get(j));
                    items.set(j, temp);
                }
            }
        }
        feed.setItems(items);
        return feed;
    }
}
