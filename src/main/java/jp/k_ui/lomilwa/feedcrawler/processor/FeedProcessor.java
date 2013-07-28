package jp.k_ui.lomilwa.feedcrawler.processor;

import jp.k_ui.lomilwa.feedcrawler.FeedInfo;
import jp.k_ui.lomilwa.feedcrawler.agent.Callback;

/**
 * A main processor class for feeds.
 * 
 * <p>
 * implement {@link Callback#completed(com.sun.syndication.feed.synd.SyndFeed)},
 * {@link Callback#cancelled()}, {@link Callback#failed(Exception)}
 * 
 * @author ui_keiichiro
 */
public interface FeedProcessor {

    Callback getCallback(FeedInfo feedInfo);

}
