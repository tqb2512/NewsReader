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
import com.tqb.newsreader.Search;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public abstract class ReceiveRSS extends AsyncTask<RSSAsyncParam, Void, String[]> {
    private Context context;
    public RSSFeed[] feed;

    final int maxItem = 10;

    @Override
    protected String[] doInBackground(RSSAsyncParam... rssAsyncParam) {
        context = rssAsyncParam[0].getContext();
        RSSSource source = new RSSSource(rssAsyncParam[0].getTopic(), context);
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
            feed = new RSSFeed();
            String rootLink = rootElement.getElementsByTagName("link").item(0).getTextContent();
            if (rootLink.contains("vnexpress")) {
                feed = fromVnExpress(content);
            } else if (rootLink.contains("thanhnien")) {
                feed = fromThanhNien(content);
            } else if (rootLink.contains("tuoitre")) {
                feed = fromTuoiTre(content);
            } else if (rootLink.contains("vtc.vn")) {
                feed = fromBaoVTC(content);
            } else if (rootLink.contains("docbao.vn")) {
                feed = fromDocBao(content);
            } else if (rootLink.contains("thethao247")) {
                feed = fromTheThao247(content);
            } else if (rootLink.contains("news.google")) {
                feed = fromGoogle(content);
            } else if (rootLink.contains("tienphong")) {
                feed = fromTienPhong(content);
            } else if (rootLink.contains("nld.com")) {
                feed = fromNguoiLaoDong(content);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return feed;
    }

    public RSSFeed fromVnExpress(String content) {
        RSSFeed result = new RSSFeed();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = builder.parse(new InputSource(new StringReader(content)));
            org.w3c.dom.Element rootElement = xmlDocument.getDocumentElement();
            NodeList items = rootElement.getElementsByTagName("item");

            List<RSSItem> rssItems = new ArrayList<>();
            int size = items.getLength();
            if (size > maxItem) {
                size = maxItem;
            }

            for (int i = 0; i < size; i++) {
                org.w3c.dom.Element item = (org.w3c.dom.Element) items.item(i);
                RSSItem rssItem = new RSSItem();
                rssItem.setTitle(Html.fromHtml(item.getElementsByTagName("title").item(0).getTextContent()).toString());
                rssItem.setLink(item.getElementsByTagName("link").item(0).getTextContent());
                String pubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();
                rssItem.setPubDate(formatDate(pubDate));
                rssItem.setPubDateFull(rssItem.getPubDate() + " " + getTimeFromPubDate(pubDate));
                Document doc = Jsoup.parse(item.getElementsByTagName("description").item(0).getTextContent());
                Element img = doc.select("img").first();
                if (img != null) {
                    rssItem.setImage(img.attr("src"));
                }
                rssItem.setDescription(Html.fromHtml(doc.body().text()).toString());
                rssItem.setSource("VnExpress");
                rssItem.setCategory(rootElement.getElementsByTagName("title").item(0).getTextContent());
                rssItems.add(rssItem);
            }
            result.setItems(rssItems);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public RSSFeed fromThanhNien(String content) {
        RSSFeed result = new RSSFeed();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = builder.parse(new InputSource(new StringReader(content)));
            org.w3c.dom.Element rootElement = xmlDocument.getDocumentElement();
            NodeList items = rootElement.getElementsByTagName("item");

            List<RSSItem> rssItems = new ArrayList<>();
            int size = items.getLength();
            if (size > maxItem) {
                size = maxItem;
            }

            for (int i = 0; i < size; i++) {
                org.w3c.dom.Element item = (org.w3c.dom.Element) items.item(i);
                RSSItem rssItem = new RSSItem();
                rssItem.setTitle(Html.fromHtml(item.getElementsByTagName("title").item(0).getTextContent()).toString());
                rssItem.setLink(item.getElementsByTagName("link").item(0).getTextContent());
                String pubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();
                rssItem.setPubDate(formatDate(pubDate));
                rssItem.setPubDateFull(rssItem.getPubDate() + " " + getTimeFromPubDate(pubDate));
                Document doc = Jsoup.parse(item.getElementsByTagName("description").item(0).getTextContent());
                Element img = doc.select("img").first();
                if (img != null) {
                    rssItem.setImage(img.attr("src"));
                }
                rssItem.setDescription(Html.fromHtml(doc.body().text()).toString());
                rssItem.setSource("Thanh Niên");
                rssItem.setCategory(rootElement.getElementsByTagName("title").item(0).getTextContent());
                rssItems.add(rssItem);
            }
            result.setItems(rssItems);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public RSSFeed fromTuoiTre(String content) {
        RSSFeed result = new RSSFeed();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = builder.parse(new InputSource(new StringReader(content)));
            org.w3c.dom.Element rootElement = xmlDocument.getDocumentElement();
            NodeList items = rootElement.getElementsByTagName("item");

            List<RSSItem> rssItems = new ArrayList<>();
            int size = items.getLength();
            if (size > maxItem) {
                size = maxItem;
            }

            for (int i = 0; i < size; i++) {
                org.w3c.dom.Element item = (org.w3c.dom.Element) items.item(i);
                RSSItem rssItem = new RSSItem();
                rssItem.setTitle(Html.fromHtml(item.getElementsByTagName("title").item(0).getTextContent()).toString());
                rssItem.setLink(item.getElementsByTagName("link").item(0).getTextContent());
                String pubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();
                rssItem.setPubDate(formatDate(pubDate));
                rssItem.setPubDateFull(rssItem.getPubDate() + " " + getTimeFromPubDate(pubDate));
                Document doc = Jsoup.parse(item.getElementsByTagName("description").item(0).getTextContent());
                Element img = doc.select("img").first();
                if (img != null) {
                    rssItem.setImage(img.attr("src"));
                }
                rssItem.setDescription(Html.fromHtml(doc.body().text()).toString());
                rssItem.setSource("Tuổi Trẻ");
                rssItem.setCategory(rootElement.getElementsByTagName("title").item(0).getTextContent());
                rssItems.add(rssItem);
            }
            result.setItems(rssItems);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public RSSFeed fromBaoVTC(String content) {
        RSSFeed result = new RSSFeed();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = builder.parse(new InputSource(new StringReader(content)));
            org.w3c.dom.Element rootElement = xmlDocument.getDocumentElement();
            NodeList items = rootElement.getElementsByTagName("item");

            List<RSSItem> rssItems = new ArrayList<>();
            int size = items.getLength();
            if (size > maxItem) {
                size = maxItem;
            }

            for (int i = 0; i < size; i++) {
                org.w3c.dom.Element item = (org.w3c.dom.Element) items.item(i);
                RSSItem rssItem = new RSSItem();
                rssItem.setTitle(Html.fromHtml(item.getElementsByTagName("title").item(0).getTextContent()).toString());
                rssItem.setLink(item.getElementsByTagName("link").item(0).getTextContent());
                String pubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();
                rssItem.setPubDate(formatDate(pubDate));
                rssItem.setPubDateFull(rssItem.getPubDate() + " " + getTimeFromPubDate(pubDate));
                Document doc = Jsoup.parse(item.getElementsByTagName("description").item(0).getTextContent());
                Pattern imgPattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Pattern descPattern = Pattern.compile("</br>(.*?)\\.");
                Matcher imgMatcher = imgPattern.matcher(doc.text().toString());
                Matcher descMatcher = descPattern.matcher(doc.text().toString());
                if (imgMatcher.find()) {
                    rssItem.setImage(imgMatcher.group(1));
                }
                if (descMatcher.find()) {
                    rssItem.setDescription(descMatcher.group(1));
                }
                rssItem.setSource("Báo VTC");
                rssItem.setCategory(rootElement.getElementsByTagName("title").item(0).getTextContent());
                rssItems.add(rssItem);
            }
            result.setItems(rssItems);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public RSSFeed fromDocBao(String content) {
        RSSFeed result = new RSSFeed();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = builder.parse(new InputSource(new StringReader(content)));
            org.w3c.dom.Element rootElement = xmlDocument.getDocumentElement();
            NodeList items = rootElement.getElementsByTagName("item");

            List<RSSItem> rssItems = new ArrayList<>();
            int size = items.getLength();
            if (size > maxItem) {
                size = maxItem;
            }

            for (int i = 0; i < size; i++) {
                org.w3c.dom.Element item = (org.w3c.dom.Element) items.item(i);
                RSSItem rssItem = new RSSItem();
                rssItem.setTitle(Html.fromHtml(item.getElementsByTagName("title").item(0).getTextContent()).toString());
                rssItem.setLink(item.getElementsByTagName("link").item(0).getTextContent());
                String pubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();
                rssItem.setPubDate(formatDate(pubDate));
                rssItem.setPubDateFull(rssItem.getPubDate() + " " + getTimeFromPubDate(pubDate));
                Document doc = Jsoup.parse(item.getElementsByTagName("description").item(0).getTextContent());
                String image = "https:" + item.getElementsByTagName("image").item(0).getTextContent();
                rssItem.setImage(image);
                rssItem.setDescription(Html.fromHtml(doc.body().text()).toString());
                rssItem.setSource("DocBao.vn");
                rssItem.setCategory(rootElement.getElementsByTagName("title").item(0).getTextContent());
                rssItems.add(rssItem);
            }
            result.setItems(rssItems);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public RSSFeed fromTheThao247(String content) {
        RSSFeed result = new RSSFeed();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = builder.parse(new InputSource(new StringReader(content)));
            org.w3c.dom.Element rootElement = xmlDocument.getDocumentElement();
            NodeList items = rootElement.getElementsByTagName("item");

            List<RSSItem> rssItems = new ArrayList<>();
            int size = items.getLength();
            if (size > maxItem) {
                size = maxItem;
            }

            for (int i = 0; i < size; i++) {
                org.w3c.dom.Element item = (org.w3c.dom.Element) items.item(i);
                RSSItem rssItem = new RSSItem();
                rssItem.setTitle(Html.fromHtml(item.getElementsByTagName("title").item(0).getTextContent()).toString());
                rssItem.setLink(item.getElementsByTagName("link").item(0).getTextContent());
                String pubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();
                rssItem.setPubDate(formatDate(pubDate));
                rssItem.setPubDateFull(rssItem.getPubDate() + " " + getTimeFromPubDate(pubDate));
                Document doc = Jsoup.parse(item.getElementsByTagName("description").item(0).getTextContent());
                Element img = doc.select("img").first();
                if (img != null) {
                    rssItem.setImage(img.attr("src"));
                }
                rssItem.setDescription(Html.fromHtml(doc.body().text()).toString());
                rssItem.setSource("Thể Thao 247");
                rssItem.setCategory(rootElement.getElementsByTagName("title").item(0).getTextContent());
                rssItems.add(rssItem);
            }
            result.setItems(rssItems);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public RSSFeed fromGoogle(String content) {
        RSSFeed result = new RSSFeed();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = builder.parse(new InputSource(new StringReader(content)));
            org.w3c.dom.Element rootElement = xmlDocument.getDocumentElement();
            NodeList items = rootElement.getElementsByTagName("item");

            List<RSSItem> rssItems = new ArrayList<>();
            int size = items.getLength();
            if (size > maxItem) {
                size = maxItem;
            }

            for (int i = 0; i < size; i++) {
                org.w3c.dom.Element item = (org.w3c.dom.Element) items.item(i);
                RSSItem rssItem = new RSSItem();
                rssItem.setTitle(Html.fromHtml(item.getElementsByTagName("title").item(0).getTextContent()).toString());
                rssItem.setLink(item.getElementsByTagName("link").item(0).getTextContent());
                String pubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();
                rssItem.setPubDate(formatDate(pubDate));
                rssItem.setPubDateFull(rssItem.getPubDate() + " " + getTimeFromPubDate(pubDate));
                Document doc = Jsoup.parse(item.getElementsByTagName("description").item(0).getTextContent());
                Element img = doc.select("img").first();
                if (img != null) {
                    rssItem.setImage(img.attr("src"));
                }
                rssItem.setDescription(Html.fromHtml(doc.body().text()).toString());
                rssItem.setSource("Google");
                rssItem.setCategory(rootElement.getElementsByTagName("title").item(0).getTextContent());
                rssItems.add(rssItem);
            }
            result.setItems(rssItems);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public RSSFeed fromTienPhong(String content) {
        RSSFeed result = new RSSFeed();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = builder.parse(new InputSource(new StringReader(content)));
            org.w3c.dom.Element rootElement = xmlDocument.getDocumentElement();
            NodeList items = rootElement.getElementsByTagName("item");

            List<RSSItem> rssItems = new ArrayList<>();
            int size = items.getLength();
            if (size > maxItem) {
                size = maxItem;
            }

            for (int i = 0; i < size; i++) {
                org.w3c.dom.Element item = (org.w3c.dom.Element) items.item(i);
                RSSItem rssItem = new RSSItem();
                rssItem.setTitle(Html.fromHtml(item.getElementsByTagName("title").item(0).getTextContent()).toString());
                rssItem.setLink(item.getElementsByTagName("link").item(0).getTextContent());
                String pubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();
                rssItem.setPubDateFull(formatDate2WithTime(pubDate));
                rssItem.setPubDate(formatDate2(pubDate));
                Document doc = Jsoup.parse(item.getElementsByTagName("description").item(0).getTextContent());
                Element img = doc.select("img").first();
                if (img != null) {
                    rssItem.setImage(img.attr("src"));
                }
                rssItem.setDescription(Html.fromHtml(doc.body().text()).toString());
                rssItem.setSource("Tiền Phong");
                rssItem.setCategory(rootElement.getElementsByTagName("title").item(0).getTextContent());
                rssItems.add(rssItem);
            }
            result.setItems(rssItems);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public RSSFeed fromNguoiLaoDong(String content) {
        RSSFeed result = new RSSFeed();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = builder.parse(new InputSource(new StringReader(content)));
            org.w3c.dom.Element rootElement = xmlDocument.getDocumentElement();
            NodeList items = rootElement.getElementsByTagName("item");

            List<RSSItem> rssItems = new ArrayList<>();
            int size = items.getLength();
            if (size > maxItem) {
                size = maxItem;
            }

            for (int i = 0; i < size; i++) {
                org.w3c.dom.Element item = (org.w3c.dom.Element) items.item(i);
                RSSItem rssItem = new RSSItem();
                rssItem.setTitle(Html.fromHtml(item.getElementsByTagName("title").item(0).getTextContent()).toString());
                rssItem.setLink(item.getElementsByTagName("link").item(0).getTextContent());
                String pubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();
                rssItem.setPubDateFull(formatDate3WithTime(pubDate));
                rssItem.setPubDate(formatDate3(pubDate));
                Document doc = Jsoup.parse(item.getElementsByTagName("description").item(0).getTextContent());
                Element img = doc.select("img").first();
                if (img != null) {
                    rssItem.setImage(img.attr("src"));
                }
                rssItem.setDescription(Html.fromHtml(doc.body().text()).toString());
                rssItem.setSource("NLD");
                rssItem.setCategory(rootElement.getElementsByTagName("title").item(0).getTextContent());
                rssItems.add(rssItem);
            }
            result.setItems(rssItems);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getTimeFromPubDate(String pubDate) {
        String[] temp = pubDate.split(" ");
        String result = "";
        result = temp[temp.length-2];
        return result;
    }

    public String formatDate(String date) {
        String[] temp = date.split(" ");
        String result = "";
        if (temp[3].length() == 2) {
            temp[3] = "20" + temp[3];
        }
        // shorten date
        if (temp[0].length() > 4)
        {
            temp[0] = temp[0].substring(0, 3);
            temp[0] = temp[0] + ",";
        }
        if (temp[2].length() > 3)
        {
            temp[2] = temp[2].substring(0, 3);
        }
        result = temp[0] + " " + temp[1] + " " + temp[2] + " " + temp[3];
        return result;
    }

    public String formatDate2(String date) {
        //From 2023-07-12 11:15:21
        //To Wed, 12 Jul 2023 11:15:21
        String[] temp = date.split(" ");
        String result = "";
        String[] dateTemp = temp[0].split("-");
        String[] timeTemp = temp[1].split(":");
        String[] dateOfWeekString = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(dateTemp[0]), Integer.parseInt(dateTemp[1]) + 1, Integer.parseInt(dateTemp[2]));
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        result = dateOfWeekString[dayOfWeek] + ", " + dateTemp[2] + " " + getMonth(dateTemp[1]) + " " + dateTemp[0];
        return result;
    }

    public String formatDate2WithTime(String date) {
        //From 2023-07-12 11:15:21
        //To Wed, 12 Jul 2023 11:15:21
        String[] temp = date.split(" ");
        String result = "";
        String[] dateTemp = temp[0].split("-");
        String[] timeTemp = temp[1].split(":");
        String[] dateOfWeekString = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(dateTemp[0]), Integer.parseInt(dateTemp[1]) + 1, Integer.parseInt(dateTemp[2]));
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        result = dateOfWeekString[dayOfWeek] + ", " + dateTemp[2] + " " + getMonth(dateTemp[1]) + " " + dateTemp[0] + " " + temp[1];
        return result;
    }

    public String formatDate3(String date) {
        //From 7/12/2023 12:50:39 PM
        //To Wed, 12 Jul 2023 12:50:39
        String[] temp = date.split(" ");
        String result = "";
        String[] dateTemp = temp[0].split("/");
        String[] timeTemp = temp[1].split(":");
        String[] dateOfWeekString = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(dateTemp[2]), Integer.parseInt(dateTemp[0]) + 1, Integer.parseInt(dateTemp[1]));
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        result = dateOfWeekString[dayOfWeek] + ", " + dateTemp[1] + " " + getMonth(dateTemp[0]) + " " + dateTemp[2];
        return result;
    }

    public String formatDate3WithTime(String date) {
        //From 7/12/2023 12:50:39 PM
        //To Wed, 12 Jul 2023 12:50:39
        String[] temp = date.split(" ");
        String result = "";
        String[] dateTemp = temp[0].split("/");
        String[] timeTemp = temp[1].split(":");
        String[] dateOfWeekString = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(dateTemp[2]), Integer.parseInt(dateTemp[0]) + 1, Integer.parseInt(dateTemp[1]));
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        result = dateOfWeekString[dayOfWeek] + ", " + dateTemp[1] + " " + getMonth(dateTemp[0]) + " " + dateTemp[2] + " " + temp[1] + " " + temp[2];
        return result;
    }

    public String getMonth(String month) {
        String result = "";
        if (month.length() == 1) {
            month = "0" + month;
        }
        switch (month) {
            case "01":
                result = "Jan";
                break;
            case "02":
                result = "Feb";
                break;
            case "03":
                result = "Mar";
                break;
            case "04":
                result = "Apr";
                break;
            case "05":
                result = "May";
                break;
            case "06":
                result = "Jun";
                break;
            case "07":
                result = "Jul";
                break;
            case "08":
                result = "Aug";
                break;
            case "09":
                result = "Sep";
                break;
            case "10":
                result = "Oct";
                break;
            case "11":
                result = "Nov";
                break;
            case "12":
                result = "Dec";
                break;
        }
        return result;
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

    public RSSFeed sortItemListByPubDate(RSSFeed feed) {
        if (feed == null) {
            return null;
        }
        List<RSSItem> items = feed.getItems();
        Collections.sort(items, new Comparator<RSSItem>() {
            @Override
            public int compare(RSSItem o1, RSSItem o2) {
                return o2.getPubDateFull().compareTo(o1.getPubDateFull());
            }
        });
        feed.setItems(items);
        return feed;
    }
}
