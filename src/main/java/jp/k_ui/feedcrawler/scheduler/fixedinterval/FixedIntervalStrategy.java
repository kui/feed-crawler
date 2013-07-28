package jp.k_ui.feedcrawler.scheduler.fixedinterval;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

import jp.k_ui.feedcrawler.FeedInfo;
import jp.k_ui.feedcrawler.agent.Callback;

import com.sun.syndication.feed.synd.SyndFeed;

public class FixedIntervalStrategy implements
        FixedIntervalScheduler.CrawlerStrategy {

    public static final long DEFAULT_CRAWLING_INTERVAL = 30 * 60; // 30 min

    private long crawlingInterval;
    private PriorityBlockingQueue<FeedInfo> feedQueue;

    public FixedIntervalStrategy(Collection<FeedInfo> feedInfoCollection) {
        this(feedInfoCollection, DEFAULT_CRAWLING_INTERVAL);
    }

    public FixedIntervalStrategy(Collection<FeedInfo> feedInfoCollection,
            long crawlingIntervalSecond) {
        setCrawlingInterval(crawlingIntervalSecond);
        feedQueue = new PriorityBlockingQueue<>(feedInfoCollection.size(),
                new FeedInfoLastFetchComparator());
        feedQueue.addAll(feedInfoCollection);
    }

    @Override
    public Callback getCallback(final FeedInfo feedInfo) {
        return new Callback() {
            @Override
            public void completed(SyndFeed feed) {
                feedInfo.setLastFetchDate(new Date());
                feedQueue.add(feedInfo);
            }

            @Override
            public void failed(Exception ex) {
                feedQueue.add(feedInfo);
            }

            @Override
            public void cancelled() {
                feedQueue.add(feedInfo);
            }

            @Override
            public FeedInfo getFeedInfo() {
                return feedInfo;
            }
        };
    }

    @Override
    public List<FeedInfo> fetchTargetFeeds() {
        Date thresholdDate = new Date(System.currentTimeMillis()
                - crawlingInterval * 1000);
        return popOlderFeedsThan(thresholdDate);
    }

    public List<FeedInfo> popOlderFeedsThan(Date threshold) {
        List<FeedInfo> targets = new ArrayList<>();
        FeedInfo feedInfo;
        while ((feedInfo = popOlderFeedInfoThan(threshold)) != null)
            targets.add(feedInfo);
        // System.out.printf("pop " + targets.size() + " feedInfo\n");
        return targets;
    }

    private FeedInfo popOlderFeedInfoThan(Date threshold) {
        FeedInfo feedInfo = feedQueue.peek();

        if (feedInfo == null)
            return null;

        if (feedInfo.getLastFetchDate() != null
                && feedInfo.getLastFetchDate().after(threshold))
            return null;

        feedQueue.poll();
        return feedInfo;
    }

    public long getCrawlingInterval() {
        return crawlingInterval;
    }

    public void setCrawlingInterval(long crawlingInterval) {
        this.crawlingInterval = crawlingInterval;
    }

    public PriorityBlockingQueue<FeedInfo> getFeedQueue() {
        return feedQueue;
    }

    static class FeedInfoLastFetchComparator implements Comparator<FeedInfo> {
        @Override
        public int compare(FeedInfo a, FeedInfo b) {
            if (a.getLastFetchDate() == null)
                return -1;
            if (b.getLastFetchDate() == null)
                return 1;

            return (int) (a.getLastFetchDate().getTime() - b.getLastFetchDate()
                    .getTime());
        }
    }
}
