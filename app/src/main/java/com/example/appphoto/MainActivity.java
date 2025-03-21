package com.example.appphoto;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appphoto.config.DatabaseHelper;
import com.example.appphoto.controllers.PhotoListActivity;
import com.example.appphoto.models.PhotoModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_PERMISSIONS = 100;

    private ImageView ivPhoto;
    private EditText etDescripcion;
    private Button btnSalvar;
    private Button btnCapturarReal;
    private Button btnVerFotosReal;

    private Bitmap photoBitmap;
    private DatabaseHelper databaseHelper;

    private String[] requiredPermissions;

    private ActivityResultLauncher<Intent> photoCaptureLauncher;
    private ActivityResultLauncher<Intent> photoSelectLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivPhoto = findViewById(R.id.ivPhoto);
        etDescripcion = findViewById(R.id.etDescripcion);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnCapturarReal = findViewById(R.id.btnCapturarReal);
        btnVerFotosReal = findViewById(R.id.btnVerFotosReal);

        databaseHelper = new DatabaseHelper(this);

        setupPermissions();

        photoSelectLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            int photoId = data.getIntExtra("PHOTO_ID", -1);
                            if (photoId != -1) {

                                // Cargar la foto seleccionada
                                PhotoModel selectedPhoto = databaseHelper.obtenerFotoPorId(photoId);
                                if (selectedPhoto != null) {
                                    photoBitmap = DatabaseHelper.getBitmapFromBytes(selectedPhoto.getImagenBytes());
                                    ivPhoto.setImageBitmap(photoBitmap);
                                    etDescripcion.setText(selectedPhoto.getDescripcion());
                                }
                            }
                        }
                    }
                }
        );

        photoCaptureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Bundle extras = result.getData().getExtras();
                        photoBitmap = (Bitmap) extras.get("data");
                        ivPhoto.setImageBitmap(photoBitmap);
                    }
                }
        );

        btnCapturarReal.setOnClickListener(v -> {
            if (checkPermissions()) {
                dispatchTakePictureIntent();
            } else {
                requestPermissions();
            }
        });

        btnSalvar.setOnClickListener(v -> {
            if (photoBitmap != null) {
                savePhotoToDatabase();
            } else {
                Toast.makeText(this, "No hay foto para guardar", Toast.LENGTH_SHORT).show();
            }
        });

        btnVerFotosReal.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PhotoListActivity.class);
            photoSelectLauncher.launch(intent);
            Log.d("MainActivity", "Lanzando PhotoListActivity");
        });
    }

    private void setupPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            // Para Android 12 (S) y anteriores
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        requiredPermissions = permissions.toArray(new String[0]);
    }

    private boolean checkPermissions() {
        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Se necesitan todos los permisos para capturar fotos", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            photoCaptureLauncher.launch(takePictureIntent);
        } else {
            Toast.makeText(this, "No hay aplicación de cámara disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePhotoToDatabase() {
        try {
            String descripcion = etDescripcion.getText().toString().trim();

            if (descripcion.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa una descripción", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convertir Bitmap a byte array para guardar en la base de datos SQLite la imagen blob
            byte[] photoBytes = DatabaseHelper.getBytesFromBitmap(photoBitmap);
            
            PhotoModel photo = new PhotoModel();
            photo.setImagenBytes(photoBytes);
            photo.setDescripcion(descripcion);
            long id = databaseHelper.guardarFoto(photo);

            if (id > 0) {
                Toast.makeText(this, "Foto guardada con éxito", Toast.LENGTH_SHORT).show();

                ivPhoto.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_avatar));
                etDescripcion.setText("");
                photoBitmap = null;
            } else {
                Toast.makeText(this, "Error al guardar la foto", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error al guardar foto: " + e.getMessage());
            Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show();
        }
    }
}