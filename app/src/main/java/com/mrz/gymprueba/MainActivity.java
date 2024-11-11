package com.mrz.gymprueba;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText ed_nombre, ed_apellido, ed_edad, ed_progreso;
    private Button b_agregar, b_ver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_nombre = findViewById(R.id.et_nombre);
        ed_apellido = findViewById(R.id.et_apellido);
        ed_edad = findViewById(R.id.et_edad);
        ed_progreso = findViewById(R.id.et_progreso);

        b_agregar = findViewById(R.id.btn_agregar);
        b_ver = findViewById(R.id.btn_ver);

        // Cambiar el nombre del botón
        b_ver.setText("Ver todos mis progresos");

        b_ver.setOnClickListener(v -> {
            // Redirigir a la actividad de progreso
            Intent i = new Intent(getApplicationContext(), Leer.class);
            startActivity(i);
        });

        b_agregar.setOnClickListener(v -> insertar());
    }

    public void insertar() {
        String nombre = ed_nombre.getText().toString().trim();
        String apellido = ed_apellido.getText().toString().trim();
        String edad = ed_edad.getText().toString().trim();
        String progreso = ed_progreso.getText().toString().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || edad.isEmpty() || progreso.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            SQLiteDatabase db = openOrCreateDatabase("GymDB", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS persona(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR, apellido VARCHAR, edad VARCHAR, progreso VARCHAR)");

            String sql = "INSERT INTO persona(nombre, apellido, edad, progreso) VALUES(?, ?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, nombre);
            statement.bindString(2, apellido);
            statement.bindString(3, edad);
            statement.bindString(4, progreso);
            statement.execute();

            Toast.makeText(this, "Datos agregados correctamente.", Toast.LENGTH_LONG).show();

            ed_nombre.setText("");
            ed_apellido.setText("");
            ed_edad.setText("");
            ed_progreso.setText("");
            ed_nombre.requestFocus();

        } catch (Exception ex) {
            Toast.makeText(this, "Error al guardar los datos.", Toast.LENGTH_LONG).show();
        }
    }
}
