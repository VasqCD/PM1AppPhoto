package com.example.appphoto.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.appphoto.models.PhotoModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";
    private SQLiteConexion conexion;
    private Context context;

    public DatabaseHelper(Context context) {
        this.context = context;
        conexion = new SQLiteConexion(context, Transacciones.NameDB, null, 1);
    }

    // Guardar una foto en la base de datos
    public long guardarFoto(PhotoModel photo) {
        long id = -1;
        SQLiteDatabase db = conexion.getWritableDatabase();

        try {
            ContentValues valores = new ContentValues();
            valores.put(Transacciones.imagen, photo.getImagenBytes());
            valores.put(Transacciones.descripcion, photo.getDescripcion());

            id = db.insert(Transacciones.tabla_fotos, null, valores);
        } catch (Exception e) {
            Log.e(TAG, "Error al guardar foto: " + e.getMessage());
        } finally {
            db.close();
        }

        return id;
    }

    // Obtener todas las fotos
    public List<PhotoModel> obtenerFotos() {
        List<PhotoModel> listaFotos = new ArrayList<>();
        SQLiteDatabase db = conexion.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery(Transacciones.SelectTablePhotos, null);

            while(cursor.moveToNext()) {
                PhotoModel photo = new PhotoModel();
                photo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Transacciones.id)));
                photo.setImagenBytes(cursor.getBlob(cursor.getColumnIndexOrThrow(Transacciones.imagen)));
                photo.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(Transacciones.descripcion)));

                listaFotos.add(photo);
            }

            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener fotos: " + e.getMessage());
        } finally {
            db.close();
        }

        return listaFotos;
    }

    // Eliminar una foto
    public boolean eliminarFoto(int idFoto) {
        SQLiteDatabase db = conexion.getWritableDatabase();
        int filasEliminadas = 0;

        try {
            String[] args = {String.valueOf(idFoto)};
            filasEliminadas = db.delete(Transacciones.tabla_fotos, Transacciones.id + "=?", args);
        } catch (Exception e) {
            Log.e(TAG, "Error al eliminar foto: " + e.getMessage());
        } finally {
            db.close();
        }

        return filasEliminadas > 0;
    }

    // Obtener una foto por ID
    public PhotoModel obtenerFotoPorId(int idFoto) {
        PhotoModel photo = null;
        SQLiteDatabase db = conexion.getReadableDatabase();

        try {
            String[] args = {String.valueOf(idFoto)};
            Cursor cursor = db.rawQuery(Transacciones.SelectPhotoById, args);

            if (cursor.moveToFirst()) {
                photo = new PhotoModel();
                photo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Transacciones.id)));
                photo.setImagenBytes(cursor.getBlob(cursor.getColumnIndexOrThrow(Transacciones.imagen)));
                photo.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(Transacciones.descripcion)));
            }

            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener foto: " + e.getMessage());
        } finally {
            db.close();
        }

        return photo;
    }

    // Convertir Bitmap a byte array
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // Convertir byte array a Bitmap
    public static Bitmap getBitmapFromBytes(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}