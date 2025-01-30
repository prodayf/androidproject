package es.umh.dadm.db;

import static android.provider.Settings.System.getString;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.umh.dadm.mispelisx6920887a.FilmsDetailsActivity;
import es.umh.dadm.mispelisx6920887a.R;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> mMovies;
    private int mIdReferencia;


    public MovieAdapter(List<Movie> movies, int idReferencia) {
        mMovies = movies;
        mIdReferencia = idReferencia;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.textViewTitle.setText(movie.getTitle());

        // Cargar la imagen desde los bytes
        byte[] coverBytes = movie.getCover();
        Bitmap coverBitmap = BitmapFactory.decodeByteArray(coverBytes, 0, coverBytes.length);
        holder.imageViewMovie.setImageBitmap(coverBitmap);

        //click en imagen
        holder.imageViewMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementa la lógica de clic en la imagen si es necesario
                int movieId = movie.getId();
                Intent intent = new Intent(v.getContext(), FilmsDetailsActivity.class);
                intent.putExtra("movieid", movieId);
                v.getContext().startActivity(intent);



                Toast.makeText(v.getContext(), "Clic en la imagen de la película", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar clic largo en la imagen
        holder.imageViewMovie.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition(); // Obtener la posición actual del elemento
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setMessage("¿Estás seguro de que deseas eliminar este elemento?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (position != RecyclerView.NO_POSITION) { // Verificar si la posición es válida
                                    int filmToDelete = mMovies.get(position).getId();
                                    DatabaseHelper dbHelper = new DatabaseHelper(v.getContext());
                                    dbHelper.deleteMovie(filmToDelete);
                                    mMovies.remove(position);
                                    notifyItemRemoved(position);
                                    Toast.makeText(v.getContext(), "Película eliminada", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView imageViewMovie;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewMovie);
            imageViewMovie = itemView.findViewById(R.id.imageViewMovie);
        }
    }
}