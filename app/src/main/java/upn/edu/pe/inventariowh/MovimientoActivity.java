package upn.edu.pe.inventariowh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import upn.edu.pe.inventariowh.Modelos.DetalleVenta;
import upn.edu.pe.inventariowh.Modelos.MovimientoInventario;
import upn.edu.pe.inventariowh.Modelos.Venta;
import upn.edu.pe.inventariowh.Red.RetrofitCliente;
import upn.edu.pe.inventariowh.Red.ServicioAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovimientoActivity extends AppCompatActivity {

    TextView txtVentasTotales, txtVentasDia, txtVentasMes, txtGanancia;
    ListView lstMovimientos;
    Button btnRealizarVenta;

    BottomNavigationView bottomNav;
    ServicioAPI apiServicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimiento);

        txtVentasTotales = findViewById(R.id.txtVentasTotales);
        txtVentasDia = findViewById(R.id.txtVentasDia);
        txtVentasMes = findViewById(R.id.txtVentasMes);
        txtGanancia = findViewById(R.id.txtGanancia);

        lstMovimientos = findViewById(R.id.lstMovimientos);
        btnRealizarVenta = findViewById(R.id.btnRealizarVenta);

        apiServicio = RetrofitCliente.getCliente().create(ServicioAPI.class);
        
        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.nav_movimientos);
        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_movimientos) {
                return true;
            }
            else if (id == R.id.nav_inventario) {
                Intent oIntent = new Intent(MovimientoActivity.this,MainActivity.class);
                startActivity(oIntent);
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.nav_proveedores) {
                Intent oIntent = new Intent(MovimientoActivity.this, ProveedorActivity.class);
                startActivity(oIntent);
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.nav_configuracion) {
                Intent oIntent = new Intent(MovimientoActivity.this, SettingsActivity.class);
                startActivity(oIntent);
                overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });

        btnRealizarVenta.setOnClickListener(v ->
                startActivity(new Intent(this, VentaActivity.class))
        );

        cargarDatos();
    }

    private void cargarDatos() {
        Call<List<Venta>> call = apiServicio.GetVentas();
        call.enqueue(new Callback<List<Venta>>() {
            @Override
            public void onResponse(Call<List<Venta>> call, Response<List<Venta>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    procesarVentas(response.body());
                } else {
                    Toast.makeText(MovimientoActivity.this, "Error al cargar movimientos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Venta>> call, Throwable t) {
                Toast.makeText(MovimientoActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void procesarVentas(List<Venta> listaVentas) {
        double total = 0;
        double hoy = 0;
        double mes = 0;
        double ganancia = 0; // Nota: La ganancia requiere precios de compra que no siempre están en la venta

        Calendar c = Calendar.getInstance();
        int d = c.get(Calendar.DAY_OF_YEAR);
        int m = c.get(Calendar.MONTH);
        int a = c.get(Calendar.YEAR);

        for (Venta v : listaVentas) {
            total += v.getTotal();

            Calendar f = Calendar.getInstance();
            f.setTimeInMillis(v.getFecha());

            if (f.get(Calendar.DAY_OF_YEAR) == d && f.get(Calendar.YEAR) == a)
                hoy += v.getTotal();

            if (f.get(Calendar.MONTH) == m && f.get(Calendar.YEAR) == a)
                mes += v.getTotal();
        }

        txtVentasTotales.setText("S/ " + String.format("%.2f", total));
        txtVentasDia.setText("S/ " + String.format("%.2f", hoy));
        txtVentasMes.setText("S/ " + String.format("%.2f", mes));
        txtGanancia.setText("S/ " + String.format("%.2f", total * 0.2)); // Estimado del 20% si no hay costos

        // Adaptar la lista de ventas al formato de movimientos para el ListView
        List<MovimientoInventario> listaMovimientos = new ArrayList<>();
        for (Venta v : listaVentas) {
            MovimientoInventario mInv = new MovimientoInventario();
            mInv.setCodigo(v.getCodigo());
            mInv.setTipo("SALIDA");
            mInv.setMonto(v.getTotal());
            mInv.setFecha(v.getFecha());
            mInv.setObservacion("Venta realizada desde API");
            int sumaCantidades = 0;
            if (v.getDetalles() != null) {
                for (DetalleVenta de : v.getDetalles()) {
                    sumaCantidades += de.getCantidad();
                }
            }

            mInv.setCantidad(sumaCantidades);
            listaMovimientos.add(mInv);
        }

        MovimientoAdapter adapter = new MovimientoAdapter(this, listaMovimientos);
        lstMovimientos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
    }
}