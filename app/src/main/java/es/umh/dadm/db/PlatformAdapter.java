package es.umh.dadm.db;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import es.umh.dadm.mispelisx6920887a.PlatformDetailsActivity;
import es.umh.dadm.mispelisx6920887a.R;

public class PlatformAdapter extends RecyclerView.Adapter<PlatformAdapter.ViewHolder> {

    private List<Platform> mPlatforms;
    private int mIdReferencia;

    public PlatformAdapter(List<Platform> platforms, int idReferencia) {
        mPlatforms = platforms;
        mIdReferencia = idReferencia;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Platform platform = mPlatforms.get(position);
        holder.textViewTitle.setText(platform.getName());

        //si no usaba asynctask explota, a saber porque
        new DownloadImageTask(holder.imageViewPlatform).execute(platform.getImageUrl());

        // Configurar clic en la imagen
        holder.imageViewPlatform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Log.d("PlatformAdapter", "platform.getId(): " + platform.getId());
                //Log.d("PlatformAdapter", "mIdReferencia: " + mIdReferencia);
                Intent intent = new Intent(v.getContext(), PlatformDetailsActivity.class);
                intent.putExtra("platform_id", platform.getId());
                intent.putExtra("idReferencia", mIdReferencia);

                v.getContext().startActivity(intent);
            }
        });


        holder.imageViewPlatform.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition(); // Obtener la posición actual del elemento
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setMessage("¿Estás seguro de que deseas eliminar este elemento?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (position != RecyclerView.NO_POSITION) { // Verificar si la posición es válida
                                    int platformIdToDelete = mPlatforms.get(position).getId();

                                    DatabaseHelper dbHelper = new DatabaseHelper(v.getContext());
                                    dbHelper.deletePlatform(platformIdToDelete);


                                    mPlatforms.remove(position);
                                    notifyItemRemoved(position);
                                    Toast.makeText(v.getContext(), "Elemento eliminado", Toast.LENGTH_SHORT).show();
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
        return mPlatforms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView imageViewPlatform;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            imageViewPlatform = itemView.findViewById(R.id.imageViewPlatform);
        }
    }


    // async task porque la implementacion alternativa no funcioona
    private static class DownloadImageTask extends AsyncTask<byte[], Void, Bitmap> {
        private ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override // convierte a bitmap
        protected Bitmap doInBackground(byte[]... imageBytes) {
            byte[] imageData = imageBytes[0];
            return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                //settea la imagen una vez el codigo async a finalizado
                imageView.setImageBitmap(result);
            }
        }
    }
}
