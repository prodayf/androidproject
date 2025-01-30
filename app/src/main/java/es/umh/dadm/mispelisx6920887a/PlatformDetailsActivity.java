package es.umh.dadm.mispelisx6920887a;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.umh.dadm.db.DatabaseHelper;
import es.umh.dadm.db.MovieAdapter;
import es.umh.dadm.db.Platform;
import es.umh.dadm.db.PlatformAdapter;

public class PlatformDetailsActivity extends AppCompatActivity {

    private ImageView imageViewPlatform;
    private TextView textViewName;
    private TextView textViewUrl;

    private Button button;

    private RecyclerView recyclerViewPeliculas;
    private MovieAdapter peliculaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform_details);
        int userId = getIntent().getIntExtra("idReferencia", -1);
        int platformId = getIntent().getIntExtra("platform_id", -1);


        // Obtener el ID de la plataforma desde el intent


        button  = findViewById(R.id.buttonCreateFilm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlatformDetailsActivity.this,peliculas.class);
                intent.putExtra("idReferencia", userId);
                intent.putExtra("platform_id", platformId);

                startActivity(intent);

            }
        });

        // Inicializar vistas
        imageViewPlatform = findViewById(R.id.imageViewPlatformDetail);
        textViewName = findViewById(R.id.plataformNameDetail);
        textViewUrl = findViewById(R.id.urlDetail);

        // Recuperar detalles de la plataforma de la base de datos
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        recyclerViewPeliculas = findViewById(R.id.recyclerViewPeliculas);
        recyclerViewPeliculas.setLayoutManager(new GridLayoutManager(this, 3));
        peliculaAdapter = new MovieAdapter(dbHelper.getAllMovies(), getIntent().getIntExtra("idReferencia", -1));
        recyclerViewPeliculas.setAdapter(peliculaAdapter);

        Platform platform = dbHelper.getPlatformById(platformId);


        if (platform != null) {
            // Asignar la imagen de la plataforma
            byte[] imageData = platform.getImageUrl();
            if (imageData != null && imageData.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                imageViewPlatform.setImageBitmap(bitmap);
            }
            textViewName.setText(platform.getName());
            textViewUrl.setText(platform.getUrl());
        } else {

            String mensaje = getString(R.string.plataformaNoEncontrada);
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    public void notifyAdapterDataSetChanged() {
        if (peliculaAdapter != null) {
            peliculaAdapter.notifyDataSetChanged();
        }
    }



}