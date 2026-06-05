package upn.edu.pe.inventariowh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import upn.edu.pe.inventariowh.AccesoDatos.DAOProducto;
import upn.edu.pe.inventariowh.Modelos.Producto;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ListView lvListar;
    DAOProducto daoProducto;
    TextView lbContarProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnAgregar = findViewById(R.id.button);
        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AgregarProducto.class);
            startActivity(intent);
        });

        lvListar = findViewById(R.id.listviewProductos);
        lbContarProductos = findViewById(R.id.lbContarProductos);

        daoProducto = new DAOProducto(this);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_movimientos) {

                Intent intent = new Intent(MainActivity.this, MovimientoActivity.class);
                startActivity(intent);
                return true;
            }

            return true;
        });

        actualizarLista("");

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

        ArrayAdapter<Producto> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaFiltrada
        );

        lvListar.setAdapter(adapter);

        lbContarProductos.setText(listaFiltrada.size() + " Productos");
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarLista("");
    }
}