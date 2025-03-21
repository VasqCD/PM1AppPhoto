package com.example.appphoto.controllers;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.appphoto.R;
import com.example.appphoto.config.DatabaseHelper;
import com.example.appphoto.models.PhotoModel;

public class PhotoDetailDialogFragment extends DialogFragment {

    private static final String ARG_PHOTO_ID = "photo_id";

    private ImageView ivPhotoDetail;
    private TextView tvDescriptionDetail;
    private Button btnClose;

    public static PhotoDetailDialogFragment newInstance(int photoId) {
        PhotoDetailDialogFragment fragment = new PhotoDetailDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PHOTO_ID, photoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_photo_detail, container, false);

        ivPhotoDetail = view.findViewById(R.id.ivPhotoDetail);
        tvDescriptionDetail = view.findViewById(R.id.tvDescriptionDetail);
        btnClose = view.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            int photoId = getArguments().getInt(ARG_PHOTO_ID);

            DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
            PhotoModel photo = databaseHelper.obtenerFotoPorId(photoId);

            if (photo != null) {
                Bitmap bitmap = DatabaseHelper.getBitmapFromBytes(photo.getImagenBytes());

                ivPhotoDetail.setImageBitmap(bitmap);
                tvDescriptionDetail.setText(photo.getDescripcion());
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}