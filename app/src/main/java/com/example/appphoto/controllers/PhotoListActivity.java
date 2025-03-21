package com.example.appphoto.controllers;

import android.os.Bundle;
import android.util.Log;
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

        List<PhotoModel> photoList = databaseHelper.obtenerFotos();
        adapter = new PhotoAdapter(this, photoList);

        adapter.setOnPhotoClickListener(photo -> {
            Log.d("PhotoListActivity", "Foto seleccionada: " + photo.getId());

            PhotoDetailDialogFragment dialogFragment = PhotoDetailDialogFragment.newInstance(photo.getId());
            dialogFragment.show(getSupportFragmentManager(), "PhotoDetailDialog");
        });

        listViewPhotos.setAdapter(adapter);

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
        updatePhotoList();
    }

    private void updatePhotoList() {
        List<PhotoModel> photoList = databaseHelper.obtenerFotos();

        if (adapter != null) {
            adapter.clear();
            adapter.addAll(photoList);
            adapter.notifyDataSetChanged();

            TextView tvNoPhotos = findViewById(R.id.tvNoPhotos);
            if (photoList.isEmpty()) {
                tvNoPhotos.setVisibility(View.VISIBLE);
                listViewPhotos.setVisibility(View.GONE);
            } else {
                tvNoPhotos.setVisibility(View.GONE);
                listViewPhotos.setVisibility(View.VISIBLE);
            }
        }
    }
}