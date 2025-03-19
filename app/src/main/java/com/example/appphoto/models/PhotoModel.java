package com.example.appphoto.models;

public class PhotoModel {

    private int id;
    private byte[] imagenBytes;
    private String descripcion;

    public PhotoModel() {
    }

    public PhotoModel(int id, byte[] imagenBytes, String descripcion) {
        this.id = id;
        this.imagenBytes = imagenBytes;
        this.descripcion = descripcion;
    }

    public PhotoModel(byte[] imagenBytes, String descripcion) {
        this.imagenBytes = imagenBytes;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImagenBytes() {
        return imagenBytes;
    }

    public void setImagenBytes(byte[] imagenBytes) {
        this.imagenBytes = imagenBytes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}