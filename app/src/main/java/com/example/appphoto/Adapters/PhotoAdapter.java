package com.example.appphoto.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appphoto.R;
import com.example.appphoto.config.DatabaseHelper;
import com.example.appphoto.models.PhotoModel;

import java.util.List;

public class PhotoAdapter extends ArrayAdapter<PhotoModel> {

    private Context context;
    private List<PhotoModel> photoList;
    private OnPhotoClickListener listener;

    public PhotoAdapter(Context context, List<PhotoModel> photos) {
        super(context, 0, photos);
        this.context = context;
        this.photoList = photos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        }

        final PhotoModel currentPhoto = photoList.get(position);

        ImageView ivPhoto = listItem.findViewById(R.id.ivPhoto);
        TextView tvDescripcion = listItem.findViewById(R.id.tvDescripcion);

        // Convertir byte array a Bitmap
        Bitmap bitmap = DatabaseHelper.getBitmapFromBytes(currentPhoto.getImagenBytes());
        ivPhoto.setImageBitmap(bitmap);

        tvDescripcion.setText(currentPhoto.getDescripcion());

        // Añadir listener para el clic en toda la fila
        listItem.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPhotoClick(currentPhoto);
            }
        });

        return listItem;
    }

    public interface OnPhotoClickListener {
        void onPhotoClick(PhotoModel photo);
    }

    public void setOnPhotoClickListener(OnPhotoClickListener listener) {
        this.listener = listener;
    }
}