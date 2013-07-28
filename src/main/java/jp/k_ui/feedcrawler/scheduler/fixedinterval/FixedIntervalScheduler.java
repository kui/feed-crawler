package jp.k_ui.feedcrawler.scheduler.fixedinterval;

import java.util.*;
import java.util.concurrent.*;

import jp.k_ui.feedcrawler.FeedInfo;
import jp.k_ui.feedcrawler.agent.Callback;
import jp.k_ui.feedcrawler.processor.FeedProcessor;
import jp.k_ui.feedcrawler.scheduler.CrawlerScheduler;

public class FixedIntervalScheduler implements CrawlerScheduler {

    public static final long DEFAULT_CHECK_INTERVAL = 30; // 30 sec

    private long checkInterval = DEFAULT_CHECK_INTERVAL; // sec
    private CrawlerStrategy strategy;
    private FeedClient client;
    private ScheduledThreadPoolExecutor executor;

    public FixedIntervalScheduler() {
        this(null);
    }

    public FixedIntervalScheduler(CrawlerStrategy strategy) {
        this.strategy = strategy;
        this.executor = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public void startUp(FeedClient client,
            Collection<FeedInfo> feedInfoCollection) {
        this.client = client;

        if (strategy == null)
            strategy = new FixedIntervalStrategy(feedInfoCollection);

        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOneIteration();
            }
        }, 0, checkInterval, TimeUnit.SECONDS);
    }

    @Override
    public void shutdown() {
        executor.shutdownNow();
    }

    private void runOneIteration() {
        System.out.println(new Date());
        for (FeedInfo feedInfo : strategy.fetchTargetFeeds())
            client.request(feedInfo, strategy.getCallback(feedInfo));
    }

    @Override
    public void awaitTermination() throws InterruptedException {
        while (isTerminated())
            executor.awaitTermination(2, TimeUnit.SECONDS);
    }

    @Override
    public boolean isTerminated() {
        return executor.isTerminated();
    }

    /**
     * set a interval which this scheduler poll the strategy at
     * 
     * @param checkIntervalSecond
     *            interval [sec]
     */
    public void setCheckInterval(long checkIntervalSecond) {
        this.checkInterval = checkIntervalSecond;
    }

    public long getCheckInterval() {
        return checkInterval;
    }

    /**
     * set a interval which this scheduler poll the strategy at
     * 
     * @return the interval seconds
     */
    public CrawlerStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(CrawlerStrategy strategy) {
        this.strategy = strategy;
    }

    public interface CrawlerStrategy {

        /**
         * Derive the feed which should be crawled on the interation.
         * 
         * <p>
         * This method will not return {@code null}.
         * 
         * @return feeds which should be crawled on the iteration.
         */
        List<FeedInfo> fetchTargetFeeds();

        /**
         * return callback to feedback this Strategy.
         * 
         * <p>
         * <strong>Do not implement feeds processing.</strong> A main processing
         * should be implement on {@link FeedProcessor}.
         * 
         * @return an callback which be executed when FeedCrawler fetched a
         *         feed.
         */
        Callback getCallback(FeedInfo feedInfo);
    }
}
