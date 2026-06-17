package upn.edu.pe.inventariowh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import upn.edu.pe.inventariowh.AccesoDatos.DAOProducto;
import upn.edu.pe.inventariowh.Modelos.Producto;

public class MainActivity extends AppCompatActivity {
    ListView lvListar;
    DAOProducto daoProducto;
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

        lvListar = findViewById(R.id.listviewProductos);
        lbContarProductos = findViewById(R.id.lbContarProductos);
        daoProducto = new DAOProducto(this);

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

            return true;
        });
        actualizarLista("");
        //configurar el buscador 
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                actualizarLista(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                actualizarLista(newText);
                return false;
            }
        });
    }

    private void actualizarLista(String texto) {
        List<Producto> listaFiltrada = daoProducto.Filtrar(texto);
        ProductoAdapter adapter =
                new ProductoAdapter(this, listaFiltrada);
        lvListar.setAdapter(adapter);

        // Actualizar el contador de productos
        lbContarProductos.setText(listaFiltrada.size() + " Productos");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar la lista al volver de "Agregar Producto"
        actualizarLista("");
    }
}