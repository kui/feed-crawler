package jp.k_ui.lomilwa.feedcrawler.agent;

import jp.k_ui.lomilwa.feedcrawler.FeedInfo;

import com.sun.syndication.feed.synd.SyndFeed;

public interface Callback {

    void completed(SyndFeed feed) throws Exception;

    void failure(Exception ex);

    void cancelled() throws Exception;

    FeedInfo getFeedInfo();
}
