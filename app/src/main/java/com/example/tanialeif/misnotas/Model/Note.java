package com.example.tanialeif.misnotas.Model;

public class Note {

    public enum TypeNote { Note, Task }

    private long id;
    private String title;
    private String text;
    private TypeNote type;
    private String date;
    private String time;
    private boolean checked;
    private String actualDate;

    public Note(long id, String title, String text, TypeNote type) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.type = type;
    }

    public Note(long id, String title, String text, TypeNote type, String date, String time,
                boolean checked, String actualDate) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.type = type;
        this.date = date;
        this.time = time;
        this.checked = checked;
        this.actualDate = actualDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TypeNote getType() {
        return type;
    }

    public void setType(TypeNote type) {
        this.type = type;
    }

    public String getDate() {return date;}

    public void setDate(String date) { this.date = date;}

    public String getTime() {return time;}

    public void setTime(String time) {this.time = time;}

    public boolean isChecked() { return checked; }

    public void setChecked(boolean checked) { this.checked = checked; }

    public String getActualDate() { return actualDate; }

    public void setActualDate(String actualDate) { this.actualDate = actualDate; }
}
