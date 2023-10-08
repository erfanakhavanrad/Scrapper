package com.example.scrapper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class BaseScrapper {
    LinkInfo linkInfo = new LinkInfo();
    ArrayList<String> fetchedLinks = new ArrayList<>();
    ArrayList<String> fetchedTitles = new ArrayList<>();

    public LinkInfo crawl(int level, String url, ArrayList<String> visitedUrls) {
        if (level <= 10) {
        Document document = request(url, visitedUrls);

        if (document != null) {
            for (Element link : document.select("a[href]")) {

                String hrefValue = link.absUrl("href");
                if (!visitedUrls.contains(hrefValue)) {
                    crawl(++level, hrefValue, visitedUrls);
                }
            }
        }
        }
        linkInfo.setLinks(fetchedLinks);
        linkInfo.setTitles(fetchedTitles);
        return linkInfo;
    }


    private Document request(String url, ArrayList<String> visited) {
        try {
            Connection connection = Jsoup.connect(url);
//            Document document = connection.get();
            Document document = connection.ignoreContentType(true).get();


            if (connection.response().statusCode() == 200) {
                fetchedLinks.add(url);
                fetchedTitles.add(document.title());
//                System.out.println("Link: " + url);
//                System.out.println("Title: " + document.title());
                visited.add(url);
                return document;
            }


        } catch (IOException e) {
            Logger.getGlobal().log(Level.WARNING, "An error has occurred.");
            e.printStackTrace();
        }
        return null;
    }

}
