package com.example.tanialeif.misnotas.Model;

public class Media {

    public enum TypeMedia {Audio, Photo, Video, Multi}

    private long id;
    private String archivo;
    private String idImage;
    private TypeMedia type;
    private long idNote;

    public Media(long id, String archivo, String idImage, TypeMedia type, long idNote) {
        this.id = id;
        this.archivo = archivo;
        this.idImage = idImage;
        this.type = type;
        this.idNote = idNote;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getArchivo() { return archivo; }

    public void setArchivo(String archivo) { this.archivo = archivo; }

    public String getIdImage() { return idImage; }

    public void setIdImage(String idImage) { this.idImage = idImage; }

    public long getIdNote() { return idNote; }

    public void setIdNote(long idNote) { this.idNote = idNote; }

    public TypeMedia getType() { return type; }

    public void setType(TypeMedia type) { this.type = type; }
}
