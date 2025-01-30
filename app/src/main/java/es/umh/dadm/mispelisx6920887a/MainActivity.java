package es.umh.dadm.mispelisx6920887a;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.database.Cursor;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.widget.Toast;


import es.umh.dadm.db.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private TextView  buttonCreateAccount;
    public Button signInButton;

    public Button button2;

    public EditText MailEditText;


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
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        signInButton = findViewById(R.id.Login);


        MailEditText = findViewById(R.id.Mail);



        Intent intent = new Intent(this,SignUp.class);

        Intent intent2 = new Intent(this,platafromasList.class);

        Intent intent3 = new Intent(MainActivity.this, plataformas.class);




        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent);
            }
        });



        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = MailEditText.getText().toString();
                if (isEmailInDatabase(email)) {
                    int id = getUserIdByEmail(email);

                    intent2.putExtra("idReferencia", id);
                    startActivity(intent2);
                } else {
                    String mensaje = getString(R.string.errorMailNoExiste);
                    Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    public boolean isEmailInDatabase(String email) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean emailExists = cursor.getCount() > 0;
        cursor.close();
        return emailExists;
    }

    public int getUserIdByEmail(String email) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int userId = -1;

        // Consulta para buscar el usuario por correo electrónico
        String query = "SELECT " + DatabaseHelper.COLUMN_ID + " FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        // Verificar si el cursor tiene resultados
        if (cursor != null && cursor.moveToFirst()) {
            // Obtener el ID de usuario si bien
            int columnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
            if (columnIndex != -1) {
                userId = cursor.getInt(columnIndex);
            }
            cursor.close();
        }

      
        db.close();

        return userId;
    }






}



