package com.mrz.gymprueba;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Eliminar extends AppCompatActivity {

    private EditText et_id_eliminar;
    private Button btn_eliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);

        et_id_eliminar = findViewById(R.id.et_id_eliminar);
        btn_eliminar = findViewById(R.id.btn_eliminar);

        btn_eliminar.setOnClickListener(v -> eliminarUsuario());
    }

    private void eliminarUsuario() {
        String id = et_id_eliminar.getText().toString().trim();

        if (id.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese el ID del usuario.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            SQLiteDatabase db = openOrCreateDatabase("GymDB", Context.MODE_PRIVATE, null);

            String sql = "DELETE FROM persona WHERE id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, id);
            int rowsAffected = statement.executeUpdateDelete();

            if (rowsAffected > 0) {
                Toast.makeText(this, "Usuario eliminado correctamente.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "No se encontró el usuario con ese ID.", Toast.LENGTH_LONG).show();
            }

            et_id_eliminar.setText("");

        } catch (Exception e) {
            Toast.makeText(this, "Error al eliminar el usuario.", Toast.LENGTH_LONG).show();
        }
    }
}
