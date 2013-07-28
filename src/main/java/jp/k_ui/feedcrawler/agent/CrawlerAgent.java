package jp.k_ui.feedcrawler.agent;

import jp.k_ui.feedcrawler.FeedInfo;

public interface CrawlerAgent {

    void initialize();

    void destroy();

    void request(FeedInfo feedInfo, Callback callback);

}
