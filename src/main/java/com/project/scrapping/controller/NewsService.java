package com.project.scrapping.controller;

import com.project.scrapping.model.NewsItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    private static final String TARGET_URL = "https://time.com/";

    public List<NewsItem> getLatestNews() {
        try {

            Document document = Jsoup.connect(TARGET_URL)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();

            return parseLatestNews(document.body());
        } catch (IOException e) {

            List<NewsItem> errorResponse = new ArrayList<>();
            errorResponse.add(new NewsItem("Error", e.getMessage()));
            return errorResponse;
        }
    }

    private List<NewsItem> parseLatestNews(Element body) {
        List<NewsItem> news = new ArrayList<>();


        Elements latestNewsItems = body.select(".latest-stories__item");


        for (Element newsItem : latestNewsItems) {
            String title = newsItem.select("h3").text();
            String link = newsItem.select("a").attr("href");
            link = link.startsWith("http") ? link : TARGET_URL + link;
            news.add(new NewsItem(title, link));
        }

        return news.isEmpty() ? List.of(new NewsItem("No news found.", "")) : news;
    }
}
