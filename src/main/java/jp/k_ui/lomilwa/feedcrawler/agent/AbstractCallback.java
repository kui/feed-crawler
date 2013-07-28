package jp.k_ui.lomilwa.feedcrawler.agent;

import jp.k_ui.lomilwa.feedcrawler.FeedInfo;

import com.sun.syndication.feed.synd.SyndFeed;

public abstract class AbstractCallback implements Callback {
    private FeedInfo feedInfo;

    public AbstractCallback(FeedInfo feedInfo) {
        this.feedInfo = feedInfo;
    }

    /**
     * a FeedInfo which will be requested about this callback
     * 
     * @return a feed infomation
     */
    @Override
    public FeedInfo getFeedInfo() {
        return feedInfo;
    }

    /**
     * a callback method when the request was completed.
     * 
     * @throws Exception
     */
    @Override
    public void completed(SyndFeed feed) throws Exception {
        // no-op
    }

    /**
     * a callback method when the request was failed or a exception raised on
     * {@link #completed(SyndFeed)} or {@link #cancelled())}.
     */
    @Override
    public void failed(Exception ex) {
        // no-op
    }

    /**
     * a callback method when the request was cancelled.
     * 
     * @throws Exception
     */
    @Override
    public void cancelled() throws Exception {
        // no-op
    }
}
