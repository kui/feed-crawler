package jp.k_ui.lomilwa.feedcrawler.scheduler;

import java.util.Collection;

import jp.k_ui.lomilwa.feedcrawler.FeedInfo;
import jp.k_ui.lomilwa.feedcrawler.agent.Callback;

/**
 * A feed fetching executer.
 * 
 * @author kui
 */
public interface CrawlerScheduler {

    /**
     * initialize and set a feed requestor
     * 
     * @param req
     */
    void startUp(FeedClient req, Collection<FeedInfo> feedInfoSet);

    void shutdown();

    void awaitTermination() throws InterruptedException;

    boolean isTerminated();

    /**
     * An interface to execute a feed request
     */
    public static interface FeedClient {

        /**
         * Request a feed
         * 
         * <p>
         * This method will be called by a {@link CrawlerScheduler}
         * implementation.
         * 
         * @param feedInfo
         *            a target feed
         * @param callback
         *            a callback which will be called when the feed request was
         *            executed
         */
        void request(FeedInfo feedInfo, Callback callback);

    }
}
