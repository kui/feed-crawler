package jp.k_ui.feedcrawler.scheduler.fixedinterval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import jp.k_ui.feedcrawler.FeedInfo;
import jp.k_ui.feedcrawler.agent.Callback;
import mockit.NonStrictExpectations;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.syndication.feed.synd.SyndFeed;

public class FixedIntervalStrategyTest {

  FixedIntervalStrategy strategy;

  @Before
  public void setUp() throws Exception {
    Collection<FeedInfo> feedInfos = new HashSet<FeedInfo>();
    feedInfos.add(new FeedInfo());

    strategy = new FixedIntervalStrategy(feedInfos);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetCallbackOnCompleted(final FeedInfo mockFeedInfo, SyndFeed feed)
      throws Exception {
    new NonStrictExpectations() {
      {
        mockFeedInfo.getLastFetchDate();
        result = new Date();
      }
    };

    assertEquals(1, strategy.getFeedQueue().size());

    Callback cb = strategy.getCallback(mockFeedInfo);
    cb.completed(feed);

    assertEquals(2, strategy.getFeedQueue().size());
  }

  @Test
  public void testGetCallbackOnFailure(final FeedInfo mockFeedInfo) {
    new NonStrictExpectations() {
      {
        mockFeedInfo.getLastFetchDate();
        result = new Date();
      }
    };

    assertEquals(1, strategy.getFeedQueue().size());

    Callback cb = strategy.getCallback(mockFeedInfo);
    cb.failed(new Exception());

    assertEquals(2, strategy.getFeedQueue().size());
  }

  @Test
  public void testGetCallbackOnCancelled(final FeedInfo mockFeedInfo) throws Exception {
    new NonStrictExpectations() {
      {
        mockFeedInfo.getLastFetchDate();
        result = new Date();
      }
    };

    assertEquals(1, strategy.getFeedQueue().size());

    Callback cb = strategy.getCallback(mockFeedInfo);
    cb.cancelled();

    assertEquals(2, strategy.getFeedQueue().size());
  }

  @Test
  public void testPopOlderFeeds() {
    long current = System.currentTimeMillis();

    strategy.getFeedQueue().add(buildFeedInfo(current));
    strategy.getFeedQueue().add(buildFeedInfo(current + 1000));
    strategy.getFeedQueue().add(buildFeedInfo(current - 1000));

    Date threshold = new Date(current);
    List<FeedInfo> feeds = strategy.popOlderFeedsThan(threshold);

    // System.out.println("Threshold: " + threshold);
    // System.out.println("targets: " + feeds);
    // System.out.println("not targets:" + strategy.getFeedQueue());

    assertEquals(1, strategy.getFeedQueue().size());
    assertEquals(3, feeds.size());

    for (FeedInfo fi : strategy.getFeedQueue()) {
      assertTrue(fi.getLastFetchDate().after(threshold));
    }

    for (FeedInfo fi : feeds) {
      // System.out.println(fi);
      assertTrue(fi.getLastFetchDate() == null
          || fi.getLastFetchDate().equals(threshold)
          || fi.getLastFetchDate().before(threshold));
    }
  }

  private FeedInfo buildFeedInfo(long time) {
    FeedInfo fi = new FeedInfo();
    fi.setLastFetchDate(new Date(time));
    return fi;
  }
}
