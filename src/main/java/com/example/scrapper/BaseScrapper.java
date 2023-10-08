package com.example.scrapper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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


    public LinkInfo hardCoded() {
        try {
//            String url = "https://simplesolution.dev";
            String url = "http://188.165.227.112/portail/series/Game_of_thrones_S5/";
            Document document = Jsoup.connect(url).get();

            Elements allLinks = document.getElementsByTag("a");

            for (Element link : allLinks) {
                String relativeUrl = link.attr("href");
                String absoluteUrl = link.attr("abs:href");

                System.out.println("Relative URL: " + relativeUrl);
                System.out.println("Absolute URL: " + absoluteUrl);
                fetchedLinks.add(relativeUrl);
                fetchedTitles.add(absoluteUrl);
            }

            linkInfo.setLinks(fetchedLinks);
            linkInfo.setTitles(fetchedTitles);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return linkInfo;
    }


}
