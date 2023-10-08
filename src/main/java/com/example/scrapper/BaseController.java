package com.example.scrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/crawl")
public class BaseController {

    @Autowired
    BaseScrapper baseScrapper;

    @GetMapping()
    private ResponseEntity<LinkInfo> baseCrawl(@RequestBody LinkInfo linkInfo) {
//        String url = "https://en.wikipedia.org";
//        String url = "https://farsroid.com/";
        System.out.println("**********" + linkInfo.getLink() + "**********");

        LinkInfo crawl = baseScrapper.crawl(1, "http://" + linkInfo.getLink(), new ArrayList<String>());
        return ResponseEntity.ok().body(crawl);
    }

}
