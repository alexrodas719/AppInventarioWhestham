package upn.edu.pe.inventariowh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import upn.edu.pe.inventariowh.Modelos.Proveedor;

public class ProveedorActivity extends AppCompatActivity {
    //variables para los elementos visuales
    ListView lvProveedores;
    Button btAddProveedor;
    BottomNavigationView bottomNavigationView;

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
        ArrayAdapter<Proveedor> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaProveedores);
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

   }