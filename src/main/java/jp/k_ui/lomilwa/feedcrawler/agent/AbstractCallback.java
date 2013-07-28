package jp.k_ui.lomilwa.feedcrawler.agent;

import jp.k_ui.lomilwa.feedcrawler.FeedInfo;

import com.sun.syndication.feed.synd.SyndFeed;

public abstract class AbstractCallback implements Callback {
    private FeedInfo feedInfo;

    public AbstractCallback(FeedInfo feedInfo) {
        this.feedInfo = feedInfo;
    }

    @Override
    public FeedInfo getFeedInfo() {
        return feedInfo;
    }

    @Override
    public void completed(SyndFeed feed) throws Exception {
        // no-op
    }

    @Override
    public void failure(Exception ex) {
        // no-op
    }

    @Override
    public void cancelled() throws Exception {
        // no-op
    }
}
