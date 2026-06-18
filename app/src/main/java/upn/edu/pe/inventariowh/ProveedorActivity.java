package upn.edu.pe.inventariowh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import upn.edu.pe.inventariowh.Modelos.Proveedor;
import upn.edu.pe.inventariowh.Red.RetrofitCliente;
import upn.edu.pe.inventariowh.Red.ServicioAPI;

public class ProveedorActivity extends AppCompatActivity {
    //variables para los elementos visuales
    ListView lvProveedores;
    Button btAddProveedor;
    BottomNavigationView bottomNavigationView;

    // Declaramos el adaptador a nivel global para poder actualizarlo
    ArrayAdapter<Proveedor> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedores);
        
        lvProveedores = findViewById(R.id.lvProveedores);
        btAddProveedor = findViewById(R.id.btAddProveedor);
        btAddProveedor.setOnClickListener(v->{
            Intent oIntent = new Intent(ProveedorActivity.this, AddProveedor.class);
            startActivity(oIntent);
        });
        
        // Cargar lista (Ejemplo con datos vacíos por ahora)
        List<Proveedor> listaProveedores = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        lvProveedores.setAdapter(adapter);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_proveedores);//actializa su icono al entrar a esta actividad
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inventario) {
                Intent oIntent = new Intent(ProveedorActivity.this,MainActivity.class);
                startActivity(oIntent);
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_proveedores) {
                return true;
            } else if (id == R.id.nav_movimientos) {
                Intent oIntent = new Intent(ProveedorActivity.this, MovimientoActivity.class);
                startActivity(oIntent);
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        // onResume se ejecuta al abrir la app Y al regresar de AddProveedor.
        // Así nos aseguramos de siempre ver la lista más actualizada del servidor.
        cargarProveedoresDesdeServidor();
    }

    private void cargarProveedoresDesdeServidor() {
        ServicioAPI api = RetrofitCliente.getCliente().create(ServicioAPI.class);

        // Ejecutamos la llamada GET
        api.GetProveedores().enqueue(new Callback<List<Proveedor>>() {
            @Override
            public void onResponse(Call<List<Proveedor>> call, Response<List<Proveedor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Proveedor> listaProveedores = response.body();

                    // Limpiamos la lista anterior y agregamos la nueva de golpe
                    adapter.clear();
                    adapter.addAll(listaProveedores);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ProveedorActivity.this, "Error del servidor: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Proveedor>> call, Throwable t) {
                Toast.makeText(ProveedorActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}