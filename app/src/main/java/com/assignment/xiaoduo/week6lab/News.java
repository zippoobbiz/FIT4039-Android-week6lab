package com.assignment.xiaoduo.week6lab;


import android.net.Uri;

/**
 * Created by xiaoduo on 4/19/15.
 */
public class News {

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getNewsLink() {
        return newsLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNewsLink(String newsLink) {
        this.newsLink = newsLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    private String title;
    private String description;
    private String newsLink;
    private String imageLink;
}
