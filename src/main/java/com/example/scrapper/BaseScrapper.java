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
            String url;
//            url = "http://188.165.227.112/portail/series/Game_of_thrones_S5/";
//            url = "https://dl3.3rver.org/cdn2/03/series/2017/Dark/";
            url = "https://s1.doshakhe.com/series/Breaking%20Bad/";


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

    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";

    public void searchGoogle(int resultsCount, String searchParameter) {
        //Taking search term input from console
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please enter the search term.");
//        String searchTerm = scanner.nextLine();
//        System.out.println("Please enter the number of results. Example: 5 10 20");
//        int num = scanner.nextInt();
//        scanner.close();

        String searchURL = GOOGLE_SEARCH_URL + "?q=" + searchParameter + "&num=" + resultsCount;
        //without proper User-Agent, we will get 403 error
        Document doc = null;
        try {
            doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        //below will print HTML data, save it to a file and open in browser to compare
//        System.out.println();
//        System.out.println("#########################");
//        System.out.println();
//        System.out.println(doc.html());
//        System.out.println();
//        System.out.println("#########################");
//        System.out.println();

        //If google search results HTML change the <h3 class="r" to <h3 class="r1"
        //we need to change below accordingly
        Elements results = doc.select("h3.r > a");


        String linkHref = null;
        String linkText = null;
        String relativeUrl = null;
        String absoluteUrl = null;
        for (Element result : results) {
            linkHref = result.attr("href");
            linkText = result.text();
//            System.out.println("Text::" + linkText + ", URL::" + linkHref.substring(6, linkHref.indexOf("&")));
        }


        Elements allLinks = doc.getElementsByTag("a");

        for (Element element : allLinks) {
            relativeUrl = element.attr("href");
            absoluteUrl = element.attr("abs:href");
//            System.out.println("");
//            System.out.println("Relative URL: " + relativeUrl);
//            System.out.println("Absolute URL: " + absoluteUrl);
//            System.out.println("");
            fetchedTitles.add(relativeUrl);
            fetchedLinks.add(absoluteUrl);
        }

//        System.out.println("");
//        System.out.println("");
//        System.out.println("Relative URL: " + relativeUrl);
//        System.out.println("Absolute URL: " + absoluteUrl);
        linkInfo.setLinks(fetchedLinks);
        linkInfo.setTitles(fetchedTitles);

        System.out.println("This is it: ");
        System.out.println(linkInfo.getLinks().get(1));
        System.out.println(linkInfo.getTitles().get(1));

        linkInfo.getTitles().clear();
        linkInfo.getLinks().clear();

    }


}
