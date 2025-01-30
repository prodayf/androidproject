package es.umh.dadm.mispelisx6920887a;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;

import es.umh.dadm.db.DatabaseHelper;

public class SignUp extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    public EditText editTextEmail;
    public EditText editName;
    public EditText editSurname;
    public EditText dataTime;
    public EditText securityQuestion;
    public RadioGroup radioGroupInterests;

    // Variables de instancia para los RadioButtons
    public RadioButton radioButtonTecnologia;
    public RadioButton radioButtonDeportes;

    public RadioButton radioButtonRedes;
    public RadioButton radioButtonCine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        dbHelper = new DatabaseHelper(this);


        EdgeToEdge.enable(this);


        setContentView(R.layout.activity_sign_up);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para retroceder a la actividad anterior

                finish();
            }
        });


        editTextEmail = findViewById(R.id.mailField).findViewById(R.id.mailInput);
        editName = findViewById(R.id.nameField).findViewById(R.id.nameInput);
        editSurname = findViewById(R.id.surnameField).findViewById(R.id.surnameInput);
        dataTime = findViewById(R.id.dateField).findViewById(R.id.dateInput);
        securityQuestion = findViewById(R.id.questionField).findViewById(R.id.questionInput);
        radioGroupInterests = findViewById(R.id.interests);

        // los radio buttons
        radioButtonTecnologia = findViewById(R.id.option1);
        radioButtonDeportes = findViewById(R.id.option2);
        radioButtonRedes = findViewById(R.id.option3);
        radioButtonCine = findViewById(R.id.option4);

        Button createAccount = findViewById(R.id.createAccount);


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if (validateForm()) {
                String email = editTextEmail.getText().toString();
                String nombre = editName.getText().toString();
                String apellidos = editSurname.getText().toString();
                String fechaNacimiento = dataTime.getText().toString();
                String preguntaSeguridad = securityQuestion.getText().toString();
                String intereses = obtenerInteresSeleccionado();
                // Intentar insertar el usuario en la base de datos
                try {
                    dbHelper.insertarUsuario(email, nombre, apellidos, fechaNacimiento, preguntaSeguridad, "", intereses);

                    String mensaje = getString(R.string.usuarioInsertado);
                    Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                    String mensaje = getString(R.string.errorInsertarUsuario);
                    Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            }
        });
    }

    private String obtenerInteresSeleccionado() {
        String interes = "";

        // Verificar cuál de los RadioButtons está seleccionado
        if (radioButtonTecnologia.isChecked()) {
            interes = "Tecnología";
        } else if (radioButtonDeportes.isChecked()) {
            interes = "Deportes";
        } else if (radioButtonRedes.isChecked()){
            interes = "Redes";

        }else if (radioButtonCine.isChecked()) {
            interes = "Cine";
        }

        return interes; // Devolver el interés seleccionado
    }

    private boolean validateForm() {
        boolean isValid = true;


        String email = editTextEmail.getText().toString();
        String nombre = editName.getText().toString();
        String apellidos = editSurname.getText().toString();
        String fechaNacimiento = dataTime.getText().toString();
        String preguntaSeguridad = securityQuestion.getText().toString();
        String intereses = obtenerInteresSeleccionado();


        if (email.isEmpty() || !isValidEmail(email)) {
            editTextEmail.setError("Por favor ingresa un correo electrónico válido");
            isValid = false;
        }


        if (nombre.isEmpty()) {
            String mensaje = getString(R.string.campoObligatiorio);
            editName.setError(mensaje);
            isValid = false;
        }
        if (apellidos.isEmpty()) {
            String mensaje = getString(R.string.campoObligatiorio);
            editSurname.setError(mensaje);
            isValid = false;
        }


        if (fechaNacimiento.isEmpty()) {
            String mensaje = getString(R.string.campoObligatiorio);
            dataTime.setError(mensaje);
            isValid = false;
        }


        if (preguntaSeguridad.isEmpty()) {
            String mensaje = getString(R.string.campoObligatiorio);
            securityQuestion.setError(mensaje);
            isValid = false;
        }


        if (intereses.isEmpty()) {

            String mensaje = getString(R.string.seleccionarInteres);
            Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
            isValid = false;

        }

        return isValid;
    }

    private boolean isValidEmail(String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}