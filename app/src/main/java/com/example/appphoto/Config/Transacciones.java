package com.example.appphoto.Config;

public class Transacciones {
    // Nombre de la base de datos
    public static final String NameDB = "PhotoDB";

    // Tabla de fotos
    public static final String tabla_fotos = "Photos";

    // Campos de la tabla fotos
    public static final String id = "id";
    public static final String imagen = "imagen";
    public static final String descripcion = "descripcion";

    // DDL
    public static final String CreateTablePhotos =
            "CREATE TABLE " + tabla_fotos + " (" +
                    id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    imagen + " BLOB NOT NULL, " +
                    descripcion + " TEXT NOT NULL" +
                    ")";

    public static final String DropTablePhotos =
            "DROP TABLE IF EXISTS " + tabla_fotos;

    // DML
    public static final String SelectTablePhotos =
            "SELECT * FROM " + tabla_fotos;

    public static final String SelectPhotoById =
            "SELECT * FROM " + tabla_fotos + " WHERE " + id + " = ?";
}