package jp.k_ui.lomilwa.feedcrawler;

import java.net.*;
import java.util.*;

import jp.k_ui.lomilwa.feedcrawler.agent.*;
import jp.k_ui.lomilwa.feedcrawler.processor.FeedProcessor;
import jp.k_ui.lomilwa.feedcrawler.scheduler.*;
import jp.k_ui.lomilwa.feedcrawler.scheduler.CrawlerScheduler.FeedClient;
import jp.k_ui.lomilwa.feedcrawler.scheduler.fixedinterval.FixedIntervalScheduler;

import org.slf4j.*;

import com.sun.syndication.feed.synd.SyndFeed;

public class FeedCrawler {
    public static final Logger LOG = LoggerFactory.getLogger(FeedCrawler.class);

    private CrawlerAgent agent;
    private CrawlerScheduler scheduler;
    private FeedProcessor processor;
    private Collection<FeedInfo> feedInfoCollection;

    public void startUp() {
        initialize();
        agent.initialize();
        scheduler.startUp(new FeedClient() {
            @Override
            public void request(FeedInfo feedInfo, Callback callback) {
                LOG.debug("request {}", feedInfo);
                agent.request(feedInfo, new MultiCastDelegationCallback(
                        feedInfo, new FeedInfoUpdateCallback(feedInfo),
                        callback, processor.getCallback(feedInfo)));
            }
        }, feedInfoCollection);
    }

    public void startAndWait() throws InterruptedException {
        startUp();
        scheduler.awaitTermination();
    }

    public boolean isTeminated() {
        return scheduler.isTerminated();
    }

    private void initialize() {
        if (processor == null)
            throw new IllegalStateException("processor must be set");

        if (feedInfoCollection == null)
            throw new IllegalStateException("feedInfoCollection must be set");

        if (agent == null)
            throw new IllegalStateException("agent must be set");

        if (scheduler == null)
            scheduler = new FixedIntervalScheduler();
    }

    public void shutDown() throws Exception {
        agent.destroy();
        scheduler.shutdown();
    }

    public CrawlerAgent getAgent() {
        return agent;
    }

    public void setAgent(CrawlerAgent agent) {
        this.agent = agent;
    }

    public CrawlerScheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(CrawlerScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public FeedProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(FeedProcessor processor) {
        this.processor = processor;
    }

    public Collection<FeedInfo> getFeedInfoCollection() {
        return feedInfoCollection;
    }

    public void setFeedInfoCollection(Collection<FeedInfo> feedInfoCollection) {
        for (FeedInfo fi : feedInfoCollection)
            if (fi.getFeedUrl() == null)
                throw new IllegalArgumentException("require FeedUrl: " + fi);

        this.feedInfoCollection = feedInfoCollection;
    }

    @Override
    public String toString() {
        return "FeedCrawler [agent=" + agent + ", scheduler=" + scheduler
                + ", processor=" + processor + "]";
    }

    static class FeedInfoUpdateCallback implements Callback {
        final private FeedInfo feedInfo;

        public FeedInfoUpdateCallback(FeedInfo feedInfo) {
            this.feedInfo = feedInfo;
        }

        @Override
        public void completed(SyndFeed feed) {
            LOG.info("request completed: {}", feedInfo);

            try {
                updateFeedInfo(feed);
            } catch (MalformedURLException e) {
                failure(e);
            }
        }

        private void updateFeedInfo(SyndFeed feed) throws MalformedURLException {
            feedInfo.setLastFetchDate(new Date());
            feedInfo.setTitle(feed.getTitle());
            feedInfo.setHtmlUrl(new URL(feed.getLink()));
            feedInfo.setType(feed.getFeedType());
        }

        @Override
        public void failure(Exception ex) {
            LOG.warn("request faildure: {}", feedInfo);
        }

        @Override
        public void cancelled() {
            LOG.warn("request canlled: {}", feedInfo);
        }

        @Override
        public FeedInfo getFeedInfo() {
            return feedInfo;
        }
    }
}
