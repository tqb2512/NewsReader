package com.tqb.newsreader.backend;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="rss", strict=false)
public class RSSFeed {
    @Element(name="channel")
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel _channel) {
        channel = _channel;
    }

    public List<RSSItem> getItems() {
        return channel.getItemList();
    }

    @Root(name="channel", strict=false)
    public static class Channel {
        @ElementList(inline=true, name="item")
        private List<RSSItem> itemList;

        public List<RSSItem> getItemList() {
            return itemList;
        }

        public void setItemList(List<RSSItem> _itemList) {
            itemList = _itemList;
        }
    }
}
