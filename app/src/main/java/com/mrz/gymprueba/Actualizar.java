package com.mrz.gymprueba;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Actualizar extends AppCompatActivity {

    private EditText et_nombre, et_apellido, et_edad, et_progreso;
    private Button btn_actualizar, btn_eliminar;
    private String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        et_nombre = findViewById(R.id.et_nombre);
        et_apellido = findViewById(R.id.et_apellido);
        et_edad = findViewById(R.id.et_edad);
        et_progreso = findViewById(R.id.et_progreso);

        btn_actualizar = findViewById(R.id.btn_actualizar);
        btn_eliminar = findViewById(R.id.btn_eliminar);

        // Obtener el ID del usuario que se desea actualizar o eliminar
        usuarioId = getIntent().getStringExtra("id");

        cargarDatosUsuario();

        // Al hacer clic en el botón de actualización, actualizamos los datos del usuario
        btn_actualizar.setOnClickListener(v -> actualizarUsuario());

        // Al hacer clic en el botón de eliminar, eliminamos el usuario de la base de datos
        btn_eliminar.setOnClickListener(v -> eliminarUsuario());
    }

    // Método para cargar los datos del usuario en los EditText
    private void cargarDatosUsuario() {
        try {
            SQLiteDatabase db = openOrCreateDatabase("GymDB", Context.MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT * FROM persona WHERE id = ?", new String[]{usuarioId});

            if (c.moveToFirst()) {
                et_nombre.setText(c.getString(1));  // c.getString(1) es el nombre
                et_apellido.setText(c.getString(2));  // c.getString(2) es el apellido
                et_edad.setText(c.getString(3));  // c.getString(3) es la edad
                et_progreso.setText(c.getString(4));  // c.getString(4) es el progreso
            }
            c.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar los datos.", Toast.LENGTH_LONG).show();
        }
    }

    // Método para actualizar los datos del usuario
    private void actualizarUsuario() {
        String nombre = et_nombre.getText().toString().trim();
        String apellido = et_apellido.getText().toString().trim();
        String edad = et_edad.getText().toString().trim();
        String progreso = et_progreso.getText().toString().trim();

        // Validación para asegurarnos de que no falten campos
        if (nombre.isEmpty() || apellido.isEmpty() || edad.isEmpty() || progreso.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            SQLiteDatabase db = openOrCreateDatabase("GymDB", Context.MODE_PRIVATE, null);

            // Actualización del usuario en la base de datos
            String sql = "UPDATE persona SET nombre = ?, apellido = ?, edad = ?, progreso = ? WHERE id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, nombre);
            statement.bindString(2, apellido);
            statement.bindString(3, edad);
            statement.bindString(4, progreso);
            statement.bindString(5, usuarioId);
            statement.executeUpdateDelete();

            Toast.makeText(this, "Usuario actualizado correctamente.", Toast.LENGTH_LONG).show();

            // Regresar a la actividad anterior después de actualizar
            finish();

        } catch (Exception ex) {
            Toast.makeText(this, "Error al actualizar los datos.", Toast.LENGTH_LONG).show();
        }
    }

    // Método para eliminar al usuario
    private void eliminarUsuario() {
        try {
            SQLiteDatabase db = openOrCreateDatabase("GymDB", Context.MODE_PRIVATE, null);

            // Eliminar el usuario de la base de datos
            String sql = "DELETE FROM persona WHERE id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, usuarioId);
            statement.executeUpdateDelete();

            Toast.makeText(this, "Usuario eliminado correctamente.", Toast.LENGTH_LONG).show();

            // Redirigir a la actividad principal después de eliminar
            Intent intent = new Intent(Actualizar.this, MainActivity.class);
            startActivity(intent);
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "Error al eliminar el usuario.", Toast.LENGTH_LONG).show();
        }
    }
}
