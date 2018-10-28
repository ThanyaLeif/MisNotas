package com.example.tanialeif.misnotas.Model;

public class Memo {
    private long id;
    private String date;
    private  String time;

    public Memo(long id, String date, String time) {
        this.id = id;
        this.date = date;
        this.time = time;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }
}
