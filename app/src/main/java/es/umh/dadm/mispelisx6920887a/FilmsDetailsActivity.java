package es.umh.dadm.mispelisx6920887a;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import es.umh.dadm.db.DatabaseHelper;
import es.umh.dadm.db.Movie;
import es.umh.dadm.db.Platform;

public class FilmsDetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textViewMovie, duration,genere,calification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_films_details);
        imageView = findViewById(R.id.imageViewMovie);

        int movieId = getIntent().getIntExtra("movieid", -1);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Movie movie = dbHelper.getMovieById(movieId);
        textViewMovie = findViewById(R.id.textViewMovie);
        duration = findViewById(R.id.duration);
        genere = findViewById(R.id.genere);
        calification = findViewById(R.id.calification);



        if (movie != null) {


            byte[] imageData = movie.getCover();
            if (imageData != null && imageData.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                imageView.setImageBitmap(bitmap);
            }
            if (textViewMovie != null) {
                textViewMovie.setText(movie.getTitle());
            }
            if (duration != null) {
                duration.setText(String.valueOf(movie.getDuration()));
            }
            if (genere != null) {
                genere.setText(movie.getGenre());
            }

            if (calification != null) {
                calification.setText(String.valueOf(movie.getRating()));
            }


        } else {
            //String mensaje = getString(R.string.peliculaNoEncontrada);
            //oast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();

            finish(); // Cerrar la actividad si no se puede encontrar la película
        }
    }
}