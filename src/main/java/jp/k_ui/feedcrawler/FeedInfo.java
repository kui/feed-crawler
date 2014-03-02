package jp.k_ui.feedcrawler;

import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * feed information about title or the feed URL etc.
 *
 * @author ui_keiichiro
 */
public class FeedInfo {

  private String title;
  private Date subscribedDate;
  private Date lastFetchDate;
  private URL feedUrl;
  private URL htmlUrl;
  private List<String> categories;

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

  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  @Override
  public String toString() {
    return "FeedInfo [title=" + title + ", subscribedDate=" + subscribedDate + ", lastFetchDate="
        + lastFetchDate + ", feedUrl=" + feedUrl + ", htmlUrl=" + htmlUrl + ", categories="
        + categories + "]";
  }
}
