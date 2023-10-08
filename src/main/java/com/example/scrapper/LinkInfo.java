package com.example.scrapper;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.beans.Transient;
import java.util.ArrayList;

public class LinkInfo {
    @JsonIgnore
    private String title;
//    @JsonIgnore
    private String link;

    private ArrayList<String> titles;
    private ArrayList<String> links;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<String> links) {
        this.links = links;
    }
}
