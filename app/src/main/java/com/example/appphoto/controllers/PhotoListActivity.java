package com.example.appphoto.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appphoto.R;
import com.example.appphoto.Adapters.PhotoAdapter;
import com.example.appphoto.config.DatabaseHelper;
import com.example.appphoto.models.PhotoModel;

import java.util.List;

public class PhotoListActivity extends AppCompatActivity {

    private ListView listViewPhotos;
    private DatabaseHelper databaseHelper;
    private PhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        listViewPhotos = findViewById(R.id.listViewPhotos);
        databaseHelper = new DatabaseHelper(this);

        loadPhotos();

        // Configurar el listener para clics en fotos
        adapter.setOnPhotoClickListener(photo -> {
            // Crear un intent para volver a MainActivity con la foto seleccionada
            Intent intent = new Intent();
            intent.putExtra("PHOTO_ID", photo.getId());

            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void loadPhotos() {
        List<PhotoModel> photoList = databaseHelper.obtenerFotos();
        adapter = new PhotoAdapter(this, photoList);
        listViewPhotos.setAdapter(adapter);

        // Mostrar mensaje si no hay fotos
        TextView tvNoPhotos = findViewById(R.id.tvNoPhotos);
        if (photoList.isEmpty()) {
            tvNoPhotos.setVisibility(View.VISIBLE);
            listViewPhotos.setVisibility(View.GONE);
        } else {
            tvNoPhotos.setVisibility(View.GONE);
            listViewPhotos.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar las fotos al volver a la actividad
        loadPhotos();
    }
}