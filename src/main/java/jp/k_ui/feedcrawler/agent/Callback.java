package jp.k_ui.feedcrawler.agent;

import jp.k_ui.feedcrawler.FeedInfo;

import com.sun.syndication.feed.synd.SyndFeed;

public interface Callback {

    /**
     * a callback method when the request was completed.
     * 
     * @throws Exception
     */
    void completed(SyndFeed feed) throws Exception;

    /**
     * a callback method when the request was failed or a exception raised on
     * {@link #completed(SyndFeed)} or {@link #cancelled())}.
     */
    void failed(Exception ex);

    /**
     * a callback method when the request was cancelled.
     * 
     * @throws Exception
     */
    void cancelled() throws Exception;

    /**
     * a FeedInfo which will be requested about this callback
     * 
     * @return a feed infomation
     */
    FeedInfo getFeedInfo();
}
