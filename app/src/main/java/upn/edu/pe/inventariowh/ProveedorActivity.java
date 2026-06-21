package upn.edu.pe.inventariowh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import upn.edu.pe.inventariowh.Modelos.Proveedor;
import upn.edu.pe.inventariowh.Red.RetrofitCliente;
import upn.edu.pe.inventariowh.Red.ServicioAPI;
import upn.edu.pe.inventariowh.util.ProveedorAdapter;

public class ProveedorActivity extends AppCompatActivity {

    // Elementos visuales
    RecyclerView rvProveedores;
    Button btAddProveedor;
    BottomNavigationView bottomNavigationView;

    ProveedorAdapter adaptador;

    // Lista global que alimenta al adaptador de forma correcta
    List<Proveedor> listaProveedores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedores);

        rvProveedores = findViewById(R.id.rvProveedores);
        btAddProveedor = findViewById(R.id.btAddProveedor);

        btAddProveedor.setOnClickListener(v -> {
            Intent oIntent = new Intent(ProveedorActivity.this, AddProveedor.class);
            startActivity(oIntent);
        });

        rvProveedores.setLayoutManager(new LinearLayoutManager(this));

        // Inicializamos el adaptador con la lista global
        adaptador = new ProveedorAdapter(listaProveedores);
        rvProveedores.setAdapter(adaptador);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_proveedores);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inventario) {
                Intent oIntent = new Intent(ProveedorActivity.this, MainActivity.class);
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
            } else if (id == R.id.nav_configuracion) {
                Intent oIntent = new Intent(ProveedorActivity.this, SettingsActivity.class);
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
        cargarProveedoresDesdeServidor();
    }

    private void cargarProveedoresDesdeServidor() {
        ServicioAPI api = RetrofitCliente.getCliente().create(ServicioAPI.class);

        api.GetProveedores().enqueue(new Callback<List<Proveedor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Proveedor>> call, @NonNull Response<List<Proveedor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Guardamos la respuesta con un nombre diferente para evitar conflictos
                    List<Proveedor> datosServidor = response.body();

                    // 1. Limpiamos la lista global que utiliza el RecyclerView
                    listaProveedores.clear();

                    // 2. Insertamos los datos nuevos que llegaron del servidor
                    listaProveedores.addAll(datosServidor);

                    // 3. Notificamos al adaptador para que pinte las tarjetas de inmediato
                    adaptador.notifyDataSetChanged();
                } else {
                    Toast.makeText(ProveedorActivity.this, "Error del servidor: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Proveedor>> call, @NonNull Throwable t) {
                Toast.makeText(ProveedorActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
