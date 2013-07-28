package jp.k_ui.lomilwa.feedcrawler.agent;

import jp.k_ui.lomilwa.feedcrawler.FeedInfo;

import com.sun.syndication.feed.synd.SyndFeed;

public class MultiCastDelegationCallback extends AbstractCallback {

    private Callback[] callbacks;

    public MultiCastDelegationCallback(FeedInfo feedInfo, Callback... callbacks) {
        super(feedInfo);

        if (callbacks == null || callbacks.length == 0)
            throw new IllegalArgumentException("must not be empty and null");
        if (feedInfo == null)
            throw new IllegalArgumentException("must not be null");

        this.callbacks = callbacks;
    }

    @Override
    public void completed(SyndFeed feed) throws Exception {
        for (Callback cb : callbacks)
            cb.completed(feed);
    }

    @Override
    public void failed(Exception ex) {
        for (Callback cb : callbacks)
            cb.failed(ex);
    }

    @Override
    public void cancelled() throws Exception {
        for (Callback cb : callbacks)
            cb.cancelled();
    }
}
