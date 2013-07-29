Feed Crawler
============

An crawler framework for feeds.

implement [FeedProcessor][] and [AbstractCallback][].

[FeedProcessor]: https://github.com/kui/feed-crawler/blob/master/src/main/java/jp/k_ui/feedcrawler/processor/FeedProcessor.java
[AbstractCallback]: https://github.com/kui/feed-crawler/blob/master/src/main/java/jp/k_ui/feedcrawler/agent/AbstractCallback.java


Sample
-----------

an crawler which do nothing:

```java
import java.net.URL;
import java.util.*;

import jp.k_ui.lomilwa.feedcrawler.agent.*;
import jp.k_ui.lomilwa.feedcrawler.processor.FeedProcessor;
import jp.k_ui.lomilwa.feedcrawler.scheduler.fixedinterval.*;

public class SampleFeedCrawler {

    public static void main(String[] args) throws Exception {
        FeedCrawler crawler = new FeedCrawler();
        Set<FeedInfo> feedInfoSet = new HashSet<>();
        FixedIntervalScheduler scheduler = new FixedIntervalScheduler();

        FeedInfo feedInfo = new FeedInfo();
        feedInfo.setFeedUrl(new URL("http://k-ui.jp/atom.xml"));
        feedInfoSet.add(feedInfo);

        feedInfo = new FeedInfo();
        feedInfo.setFeedUrl(new URL("http://k-ui.tumblr.com/rss"));
        feedInfoSet.add(feedInfo);

        crawler.setFeedInfoCollection(feedInfoSet);
        crawler.setAgent(new HttpAsyncClientCrawlerAgent());
        crawler.setProcessor(new FeedProcessor() {
            @Override
            public Callback getCallback(final FeedInfo feedInfo) {
                return new AbstractCallback(feedInfo) {
                    // no-op
                    // implement callbacks
                };
            }
        });

        FixedIntervalStrategy strategy = new FixedIntervalStrategy(feedInfoSet);
        scheduler.setStrategy(strategy);
        crawler.setScheduler(scheduler);

        crawler.startAndWait();
    }
}
```
