package com.example.finalproject;

/**
 * Model used to save articles
 */
public class ArticleModel {

    private String title;
    private String url;
    private String thumbnail;
    private String section;


    public ArticleModel(String title, String url, String thumbnail, String section) {
        this.title = title;
        this.url = url;
        this.thumbnail = thumbnail;
        this.section = section;
    }


    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getSection() {
        return section;
    }
}
