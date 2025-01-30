package es.umh.dadm.mispelisx6920887a;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import es.umh.dadm.db.DatabaseHelper;

public class peliculas extends AppCompatActivity {

    private ImageView img;
    private Button button;
    private TextInputEditText titleFilm,duration,genere,calification;

    private DatabaseHelper dbHelper;

    private static final int SELECT_IMAGE_REQUEST_CODE = 100;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_peliculas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        img = findViewById(R.id.caratula);
        button = findViewById(R.id.insertFilm);

        titleFilm = findViewById(R.id.titleFilm);
        duration = findViewById(R.id.duration);
        genere = findViewById(R.id.genere);
        calification = findViewById(R.id.calification);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = getSelectedImageUri();

                int idRecibido = getIntent().getIntExtra("idReferencia", -1);
                int idPlataforma = getIntent().getIntExtra("platform_id", -1);
                //Log.d("DEBUG", "Valor de idRecibido: " + idRecibido);
                //Log.d("DEBUG", "Valor de idPlataforma: " + idPlataforma);

                if (!verificarCampos()) {

                    return;
                }

                byte[] imagenEnBytes = convertUriToBitmap(uri);


                String titulo = titleFilm.getText().toString();
                int duracion = Integer.parseInt(duration.getText().toString());
                String genero = genere.getText().toString();
                int calificacion = Integer.parseInt(calification.getText().toString());


                dbHelper.insertMovie(idRecibido,idPlataforma,imagenEnBytes,titulo,duracion,genero,calificacion);

                Toast.makeText(peliculas.this, "Película insertada", Toast.LENGTH_SHORT).show();


                //((PlatformDetailsActivity) getParent()).notifyAdapterDataSetChanged();

                finish();
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, SELECT_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            img.setImageURI(selectedImageUri);
        }
    }

    private byte[] convertUriToBitmap(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream); // Reducir calidad a 50%
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Uri getSelectedImageUri() {
        return selectedImageUri;
    }

    private boolean verificarCampos() {
        String titulo = titleFilm.getText().toString();
        String duracionStr = duration.getText().toString();
        String genero = genere.getText().toString();
        String calificacionStr = calification.getText().toString();


        if (titulo.isEmpty() || duracionStr.isEmpty() || genero.isEmpty() || calificacionStr.isEmpty()) {
            // Mostrar un mensaje de error si algún campo está vacío
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {

            int duracion = Integer.parseInt(duracionStr);
            int calificacion = Integer.parseInt(calificacionStr);


            if (duracion <= 0 || calificacion < 0 || calificacion > 5) {


                Toast.makeText(this, "La duración debe ser un valor positivo y la calificación debe estar entre 0 y 5 ", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {

            Toast.makeText(this, "Error al convertir cadenas a números", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }
}