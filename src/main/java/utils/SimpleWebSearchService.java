package utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static java.lang.System.out;

public class SimpleWebSearchService {

    public String search(String query) throws Exception {
            query = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = "https://html.duckduckgo.com/html/?q=" + query;
            out.println("Searching the web for: " + query);
            var doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
            var results = doc.select("div.result__body");
            var resultsBuilder = new StringBuilder();
            for (Element result : results) {
                Element resultTitle = result.selectFirst(".result__title a");
                String snippet = result.selectFirst("a.result__snippet").text();
                String title = resultTitle.text();
                String link = resultTitle.absUrl("href");  // Get absolute link
                resultsBuilder.append("""
                    Title: %s
                    Link: %s
                    Snippet: %s
                    ---
                    """.formatted(title, link, snippet));
            }

            return resultsBuilder.toString();
        }
}