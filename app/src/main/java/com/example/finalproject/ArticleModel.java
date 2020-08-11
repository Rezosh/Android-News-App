package com.example.finalproject;

/**
 * Model used to save articles
 */
public class ArticleModel {

    private String endpoint;
    private String title;
    private String url;
    private String thumbnail;
    private String section;
    private String date;


    public ArticleModel(String endpoint, String title, String url, String thumbnail, String section, String date) {
        this.endpoint = endpoint;
        this.title = title;
        this.url = url;
        this.thumbnail = thumbnail;
        this.section = section;
        this.date = date;
    }

    public ArticleModel(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
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

    public String getDate() {
        return date;
    }

    public String getSection() {
        return section;
    }
}
