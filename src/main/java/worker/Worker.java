package worker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class Worker implements Runnable {

    private final String domain;

    private final int LIMIT = 100;
    private int count;
    private Queue<String> internalLinks = new ArrayDeque<>(10000);
    private Set<String> uniqueLinks = new HashSet<>();
    private Set<String> newDomains = new HashSet<>();


    Worker(String domain) {
        this.domain = domain;
        internalLinks.add("http://" + domain);
    }

    @Override
    public void run() {
       while (count < LIMIT) {
           String link = internalLinks.poll();
           if (link == null) break;
           proccessLinks(getDocumentFromUrl(link));
       }

       UpstreamUpdater.postDomains(newDomains);

        System.out.println(internalLinks);
        System.out.println(newDomains);
        System.out.println("Finished!!!");
    }

    private Document getDocumentFromUrl(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            count++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    private void proccessLinks(Document doc) {
      for (Element e : doc.select("a[href]")) {
          String url = e.attr("abs:href");
          if (!url.contains("http")) continue;
          if (isInternalUrl(url) && !uniqueLinks.contains(url)) {
              internalLinks.add(url);
              uniqueLinks.add(url);
          } else {
              newDomains.add(getDomainName(url));
          }
      }
    }

    private boolean isInternalUrl(String url) {
        int index = url.startsWith("https://") ? 8 : 7;
        return url.substring(index).startsWith(domain);
    }

    public static String getDomainName(String url)  {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "dummy.com";
        }
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }



}
