package com.mrz.gymprueba;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Leer extends AppCompatActivity {

    private ListView listViewProgresos;
    private ArrayList<String> listaProgresos;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        listViewProgresos = findViewById(R.id.listViewProgresos);
        listaProgresos = new ArrayList<>();

        cargarProgresos();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaProgresos);
        listViewProgresos.setAdapter(adapter);

        // Acción al seleccionar un elemento (para actualizar)
        listViewProgresos.setOnItemClickListener((parent, view, position, id) -> {
            String item = listaProgresos.get(position);
            String[] datos = item.split(" - "); // Suponiendo que el formato es "Nombre - Progreso"
            String usuarioId = datos[0]; // ID del usuario para actualizar

            Intent i = new Intent(Leer.this, Actualizar.class);
            i.putExtra("id", usuarioId); // Enviar el ID del usuario a la actividad de actualización
            startActivity(i);
        });

        // Acción para eliminar un progreso al hacer una pulsación larga
        listViewProgresos.setOnItemLongClickListener((parent, view, position, id) -> {
            String item = listaProgresos.get(position);
            String[] datos = item.split(" - "); // Obtener ID y nombre
            String usuarioId = datos[0]; // Obtener ID del usuario para eliminar

            eliminarUsuario(usuarioId, position); // Eliminar el progreso de la lista

            return true; // Esto indica que hemos manejado el evento
        });
    }

    // Método para cargar todos los progresos en la lista
    private void cargarProgresos() {
        SQLiteDatabase db = openOrCreateDatabase("GymDB", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT id, nombre, progreso FROM persona", null);

        listaProgresos.clear(); // Limpiar la lista antes de cargar nuevos datos
        while (c.moveToNext()) {
            String id = c.getString(0);
            String nombre = c.getString(1);
            String progreso = c.getString(2);
            listaProgresos.add(id + " - " + nombre + " - " + progreso); // Añadir el progreso a la lista
        }
        c.close(); // Cerrar el cursor después de usarlo
    }

    // Método para eliminar un progreso de la base de datos y la lista
    private void eliminarUsuario(String usuarioId, int position) {
        try {
            SQLiteDatabase db = openOrCreateDatabase("GymDB", Context.MODE_PRIVATE, null);

            // Eliminar el usuario de la base de datos
            String sql = "DELETE FROM persona WHERE id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, usuarioId);
            int rowsAffected = statement.executeUpdateDelete();

            if (rowsAffected > 0) {
                Toast.makeText(this, "Progreso eliminado correctamente.", Toast.LENGTH_LONG).show();
                listaProgresos.remove(position); // Eliminar el progreso de la lista
                adapter.notifyDataSetChanged(); // Notificar al adaptador que la lista ha cambiado
            } else {
                Toast.makeText(this, "No se pudo eliminar el progreso.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al eliminar el progreso.", Toast.LENGTH_LONG).show();
        }
    }
}
