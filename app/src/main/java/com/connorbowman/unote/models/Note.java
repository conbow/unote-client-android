package com.connorbowman.unote.models;

public class Note {

    private String _id;
    private String title;
    private String body;

    public Note(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public Note(String id, String title, String body) {
        this._id = id;
        this.title = title;
        this.body = body;
    }

    public String getId() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
