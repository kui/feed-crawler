package jp.k_ui.lomilwa.feedcrawler.agent;

import jp.k_ui.lomilwa.feedcrawler.FeedInfo;

public interface CrawlerAgent {

    void initialize();

    void destroy();

    void request(FeedInfo feedInfo, Callback callback);

}
