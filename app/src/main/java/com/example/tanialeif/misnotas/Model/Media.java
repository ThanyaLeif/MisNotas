package com.example.tanialeif.misnotas.Model;

public class Media {
    private long id;
    private String archivo;
    private long idImage;
    private long idNote;

    public Media(long id, String archivo, long idImage, long idNote) {
        this.id = id;
        this.archivo = archivo;
        this.idImage = idImage;
        this.idNote = idNote;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getArchivo() { return archivo; }

    public void setArchivo(String archivo) { this.archivo = archivo; }

    public long getIdImage() { return idImage; }

    public void setIdImage(long idImage) { this.idImage = idImage; }

    public long getIdNote() { return idNote; }

    public void setIdNote(long idNote) { this.idNote = idNote; }
}
