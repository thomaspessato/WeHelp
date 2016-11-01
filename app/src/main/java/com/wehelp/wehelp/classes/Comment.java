package com.wehelp.wehelp.classes;

/**
 * Created by temp on 9/19/16.
 */
public class Comment {

    private String text;
    private String username;

    public Comment() {
    }

    public Comment(String text, String username) {
        this.text = text;
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
