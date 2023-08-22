package com.example.ntfctns.classes;

import java.util.List;

public class Article {
    public String image;
    public String link;
    public String body;
    public long time;
    public List<String> keywords;
    public Article(
            String image,
            String link,
            String body,
            long time,
            List<String> keywords
    ) {
        this.image = image;
        this.link = link;
        this.body = body;
        this.time = time;
        this.keywords = keywords;
    }
}
