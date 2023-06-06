package com.tqb.newsreader.backend;

import android.content.Context;

public class RSSAsyncParam {
    Context context;
    String topic;

    public RSSAsyncParam(Context context, String topic) {
        this.context = context;
        this.topic = topic;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
