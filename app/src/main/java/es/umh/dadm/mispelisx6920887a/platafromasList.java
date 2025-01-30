package es.umh.dadm.mispelisx6920887a;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.umh.dadm.db.DatabaseHelper;
import es.umh.dadm.db.Platform;
import es.umh.dadm.db.PlatformAdapter;

public class platafromasList extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    private Button  createPlatform;

    private RecyclerView recyclerView;
    private PlatformAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platafromas_list);

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        adapter = new PlatformAdapter(dbHelper.getAllPlatforms(), getIntent().getIntExtra("idReferencia", -1));
        recyclerView.setAdapter(adapter);



        if (adapter.getItemCount() == 0) {
            String mensaje = getString(R.string.noPlataformas);
            Toast.makeText(platafromasList.this,mensaje , Toast.LENGTH_SHORT).show();

        }



        createPlatform = findViewById(R.id.buttonCreatePlatform);

        createPlatform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(platafromasList.this, plataformas.class);
                int idRecibido = getIntent().getIntExtra("idReferencia", -1);
                intent.putExtra("idReferencia", idRecibido);
                startActivity(intent);
            }
        });
    }

    public void notifyAdapterDataSetChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
    public void notifyAdapterItemRemoved(int position) {
        if (adapter != null) {
            adapter.notifyItemRemoved(position);
        }
    }


}