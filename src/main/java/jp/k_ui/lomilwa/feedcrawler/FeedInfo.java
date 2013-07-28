package jp.k_ui.lomilwa.feedcrawler;

import java.net.URL;
import java.util.Date;

/**
 * feed infomation about title or the feed URL etc.
 * 
 * @author ui_keiichiro
 */
public class FeedInfo {

    private String title;
    private Date subscribedDate;
    private Date lastFetchDate;
    private URL feedUrl;
    private URL htmlUrl;
    private String type;

    public Date getSubscribedDate() {
        return subscribedDate;
    }

    public void setSubscribedDate(Date subscribedDate) {
        this.subscribedDate = subscribedDate;
    }

    public Date getLastFetchDate() {
        return lastFetchDate;
    }

    public void setLastFetchDate(Date lastFetchDate) {
        this.lastFetchDate = lastFetchDate;
    }

    public URL getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(URL feedUrl) {
        this.feedUrl = feedUrl;
    }

    public URL getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(URL htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "FeedInfo [subscribedDate=" + subscribedDate
                + ", lastFetchDate=" + lastFetchDate + ", xmlUrl=" + feedUrl
                + ", htmlUrl=" + htmlUrl + ", title=" + title + ", type="
                + type + "]";
    }
}
