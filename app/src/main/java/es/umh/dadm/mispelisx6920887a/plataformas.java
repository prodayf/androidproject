package es.umh.dadm.mispelisx6920887a;

import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import androidx.activity.EdgeToEdge;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import es.umh.dadm.db.DatabaseHelper;
import es.umh.dadm.db.PlatformAdapter;


public class plataformas extends AppCompatActivity {


    public ImageView img;
    public Button button;

    public TextInputEditText platformname,LINK,Email,password;
    private DatabaseHelper dbHelper;
    private PlatformAdapter Adapter;

    private static final int SELECT_IMAGE_REQUEST_CODE = 100;

    private Uri selectedImageUri;
    private PlatformAdapter mAdapter;

    private Uri getSelectedImageUri() {
        return selectedImageUri;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plataformas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbHelper = new DatabaseHelper(this);


        Intent intent = getIntent();
        if (intent != null) {
            int idRecibido = intent.getIntExtra("idReferencia", -1);

        }


        img= findViewById(R.id.imagen);
        button= findViewById(R.id.insertPlatform);

        platformname = findViewById(R.id.plataformField).findViewById(R.id.plataformName);
        LINK = findViewById(R.id.URLFIELD).findViewById(R.id.URL);
        Email = findViewById(R.id.mailField).findViewById(R.id.mail);
        password = findViewById(R.id.passField).findViewById(R.id.pass);




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
                byte[] imagenEnBytes = convertirUriABitmap(uri);

                Intent intent = getIntent();
                int idRecibido = 0;
                if (intent != null) {
                    idRecibido = intent.getIntExtra("idReferencia", -1);

                }
                if (!validateForm()) {

                    return;
                }

                img= findViewById(R.id.imagen);
                String platformNameText = platformname.getText().toString();
                String URLText = LINK.getText().toString();
                String emailText = Email.getText().toString();
                String passText = password.getText().toString();
                dbHelper.insertarPlataforma(idRecibido, imagenEnBytes, platformNameText, URLText, emailText, passText);

                String mensaje = getString(R.string.plataformaInsertada);
                Toast.makeText(plataformas.this, mensaje, Toast.LENGTH_SHORT).show();
                //recyclerView.getAdapter().notifyDataSetChanged();

               ((platafromasList) getParent()).notifyAdapterDataSetChanged();
                //recreate();
                //finish();





            }
        });







    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, SELECT_IMAGE_REQUEST_CODE);
        //la version no deprecated de startforactivity rresult da problemas, he intentado implementar
        //pero es algo complicado y enrevesado.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            img.setImageURI(selectedImageUri);
        }
    }





    private byte[] convertirUriABitmap(Uri uri) {
        try {
            // Obtener el Bitmap de la Uri
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            // Convertir el Bitmap en un byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream); // comprimo al 50 porque explota si es demasiado grande la imagen
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace(); //por buenos ussos no recomienda usar esto, pero tampoco he visto una alternativa
            return null;
        }
    }






    private Uri obtenerUriImagenSeleccionada() {
        // Lanzar un Intent para seleccionar una imagen de la galería
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Lanzar el Intent y esperar el resultado
        startActivityForResult(galleryIntent, SELECT_IMAGE_REQUEST_CODE);
        // La URI seleccionada se obtendrá en onActivityResult
        return null;
    }

    private void saveImageUriToDatabase(Uri imageUri) {
        // Convertir el Uri en un String para almacenarlo en la base de datos
        String uriString = imageUri.toString();

    }

    private byte[] saveImageUriToDatabaseV(Uri imageUri) {
        byte[] imageData = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            imageData = byteArrayOutputStream.toByteArray();
            // En este punto, imageData contiene los datos de la imagen en forma de bytes
            // Puedes hacer lo que necesites con esta información
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageData;
    }

    private boolean validarFormulario(String platformName, String URL, String email, String password) {
        // Verificar si algún campo está vacío
        if (platformName.isEmpty() || URL.isEmpty() || email.isEmpty() || password.isEmpty()) {
            // Mostrar un mensaje de error indicando que todos los campos son obligatorios
            String mensaje = getString(R.string.todosCamposObligatorios);
            Toast.makeText(plataformas.this, mensaje, Toast.LENGTH_SHORT).show();
            return false; // La validación ha fallado
        } else {
            return true; // La validación ha sido exitosa
        }
    }

    private boolean validateForm() {
        boolean isValid = true;



        String platformNameText = platformname.getText().toString();
        String URLText = LINK.getText().toString();
        String emailText = Email.getText().toString();
        String passText = password.getText().toString();

        // Verifica si el campo de correo electrónico está vacío o no es válido


        // Verifica si los campos de nombre y apellidos están vacíos
        if (platformNameText.isEmpty() ) {
            String mensaje = getString(R.string.ingresaNombrePlataforma);
            platformname.setError(mensaje);
            isValid = false;
        }
        if (URLText.isEmpty() || !isValidURL(URLText)) {
            String mensaje = getString(R.string.campoObligatiorio);
            LINK.setError(mensaje);
            isValid = false;
        }

        // Verifica si el campo de fecha de nacimiento está vacío
        if (emailText.isEmpty() || !isValidEmail(emailText)) {
            String mensaje = getString(R.string.campoObligatiorio);
            Email.setError(mensaje);
            isValid = false;
        }

        // Verifica si el campo de pregunta de seguridad está vacío
        if (passText.isEmpty()) {
            String mensaje = getString(R.string.campoObligatiorio);
            password.setError(mensaje);
            isValid = false;
        }


        return isValid;
    }

    private  boolean isValidEmail(String email) {
        // Aquí puedes implementar tu lógica de validación de correo electrónico
        // Por ejemplo, puedes usar expresiones regulares para verificar el formato del correo electrónico
        // Retorna true si el correo electrónico es válido, de lo contrario, retorna false
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidURL(String url) {
        // Aquí defines una expresión regular más general para validar la URL
        // Esta expresión permite varios formatos de URL, incluidos los que no tienen un protocolo específico
        // Por ejemplo, http://, https://, ftp://, www., etc.
        String urlPattern = "^(https?://)?([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(/[a-zA-Z0-9-._~:/?#\\[@\\]!$&'()*+,;=%]*)?$";
        // Retorna true si la cadena coincide con el patrón, de lo contrario, retorna false
        return url.matches(urlPattern);
    }




}