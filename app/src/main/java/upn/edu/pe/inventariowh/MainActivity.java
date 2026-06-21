package upn.edu.pe.inventariowh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import upn.edu.pe.inventariowh.Modelos.ProductoAPI;
import upn.edu.pe.inventariowh.Red.RetrofitCliente;
import upn.edu.pe.inventariowh.Red.ServicioAPI;
import upn.edu.pe.inventariowh.util.ProductoAdapter;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvproductos;
    private List<ProductoAPI> listaProductosServidor = new ArrayList<>();
    private ProductoAdapter adapter;
    TextView lbContarProductos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configuración del botón para agregar producto
        Button btnAgregar = findViewById(R.id.button);
        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AgregarProducto.class
            );
            startActivity(intent);
        });

        rvproductos = findViewById(R.id.rvProductos);
        rvproductos.setLayoutManager(new LinearLayoutManager(this));
        lbContarProductos = findViewById(R.id.lbContarProductos);

        //navegar a movimientos
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_movimientos) {
                Intent oIntent = new Intent(MainActivity.this, MovimientoActivity.class);
                startActivity(oIntent);
                overridePendingTransition(0, 0);
                return true;
            }
            else if ( id == R.id.nav_proveedores){
                Intent oIntent = new Intent(MainActivity.this,ProveedorActivity.class);
                startActivity(oIntent);
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.nav_configuracion) {
                Intent oIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(oIntent);
                overridePendingTransition(0, 0);
                return true;
            }

            return true;
        });
        actualizarLista("");

        //CONFIGURAR EL BUSCADOR
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String consulta) {
                actualizarLista(consulta);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nuevoTexto) {
                actualizarLista(nuevoTexto);
                return false;
            }
        });
    }
    // Adaptamos el metodo de filtrado para buscar en la lista descargada
    private void actualizarLista(String texto) {
        if (listaProductosServidor == null || listaProductosServidor.isEmpty()) return;

        List<ProductoAPI> listaFiltrada = new ArrayList<>();

        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaProductosServidor);
        } else {
            String textoMinuscula = texto.toLowerCase();
            for (ProductoAPI p : listaProductosServidor) {
                // Filtramos por nombre o SKU
                if (p.getNombre().toLowerCase().contains(textoMinuscula) ||
                        p.getSKU().toLowerCase().contains(textoMinuscula)) {
                    listaFiltrada.add(p);
                }
            }
        }

        // Actualizamos el adaptador con la nueva lista filtrada
        adapter = new ProductoAdapter(MainActivity.this, listaFiltrada);
        rvproductos.setAdapter(adapter);
        lbContarProductos.setText(listaFiltrada.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar la lista al volver de "Agregar Producto"
        obtenerProductosDelServidor();
    }
    private void obtenerProductosDelServidor() {
        ServicioAPI servicio = RetrofitCliente.getCliente().create(ServicioAPI.class);
        Call<List<ProductoAPI>> call = servicio.GetProductos();

        call.enqueue(new Callback<List<ProductoAPI>>() {
            @Override
            public void onResponse(Call<List<ProductoAPI>> call, Response<List<ProductoAPI>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 1. Guardamos la lista completa descargada
                    listaProductosServidor = response.body();

                    // 2. Actualizamos el RecyclerView
                    adapter = new ProductoAdapter(MainActivity.this, listaProductosServidor);
                    rvproductos.setAdapter(adapter);

                    // 3. Actualizamos el texto
                    lbContarProductos.setText(listaProductosServidor.size() + "");
                } else {
                    Toast.makeText(MainActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductoAPI>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Falla de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}