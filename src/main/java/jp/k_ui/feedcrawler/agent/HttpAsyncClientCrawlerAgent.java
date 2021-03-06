package jp.k_ui.feedcrawler.agent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.zip.GZIPInputStream;

import jp.k_ui.feedcrawler.FeedInfo;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.cache.CachingHttpAsyncClient;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.reactor.IOReactorException;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class HttpAsyncClientCrawlerAgent implements CrawlerAgent {

  private HttpAsyncClient client;

  public HttpAsyncClientCrawlerAgent() throws IOReactorException {
    this(new CachingHttpAsyncClient());
  }

  public HttpAsyncClientCrawlerAgent(HttpAsyncClient client) {
    setClient(client);
  }

  @Override
  public void request(FeedInfo feedInfo, Callback callback) {
    client.execute(buildGetRequest(feedInfo), new CallbackWrapper(callback));
  }

  private HttpUriRequest buildGetRequest(FeedInfo feedInfo) {
    try {
      return new HttpGet(feedInfo.getFeedUrl().toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void initialize() {
  }

  @Override
  public void destroy() {
  }

  public HttpAsyncClient getClient() {
    return client;
  }

  public void setClient(HttpAsyncClient client) {
    this.client = client;
  }

  static class CallbackWrapper implements FutureCallback<HttpResponse> {
    private Callback callback;

    public CallbackWrapper(Callback cb) {
      callback = cb;
    }

    @Override
    public void completed(HttpResponse result) {
      try {
        callback.completed(buildFeed(result));
      } catch (Exception e) {
        failed(e);
      }
    }

    private SyndFeed buildFeed(HttpResponse result) throws IOException,
        FeedException {
      InputStream stream = null;
      try {
        stream = result.getEntity().getContent();
        Header contentEnc = result.getFirstHeader("Content-Encoding");
        if (contentEnc != null
            && "gzip".equalsIgnoreCase(contentEnc.getValue()))
          stream = new GZIPInputStream(stream);

        XmlReader reader = null;
        Header contentType = result.getEntity().getContentType();
        if (contentType == null) {
          reader = new XmlReader(result.getEntity().getContent(),
              true);
        } else {
          reader = new XmlReader(result.getEntity().getContent(),
              contentType.getValue(), true);
        }

        return new SyndFeedInput().build(reader);
      } finally {
        if (stream != null)
          stream.close();
      }
    }

    @Override
    public void failed(Exception ex) {
      callback.failed(ex);
    }

    @Override
    public void cancelled() {
      try {
        callback.cancelled();
      } catch (Exception e) {
        failed(e);
      }
    }
  }
}
