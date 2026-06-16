package upn.edu.pe.inventariowh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.List;

import upn.edu.pe.inventariowh.AccesoDatos.DAOMovimientoInventario;
import upn.edu.pe.inventariowh.AccesoDatos.DAOProducto;
import upn.edu.pe.inventariowh.Modelos.MovimientoInventario;
import upn.edu.pe.inventariowh.Modelos.Producto;

public class MovimientoActivity extends AppCompatActivity {

    TextView txtVentasTotales, txtVentasDia, txtVentasMes, txtGanancia;
    ListView lstMovimientos;
    Button btnRealizarVenta;

    DAOMovimientoInventario dao;
    DAOProducto daoProducto;

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

        dao = new DAOMovimientoInventario(this);
        daoProducto = new DAOProducto(this);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_movimientos) {
                return true;
            }

            else if (id == R.id.nav_inventario) {
                Intent intent = new Intent(MovimientoActivity.this, MainActivity.class);
                startActivity(intent);
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


        List<MovimientoInventario> lista = dao.ListarTodos();

        double total = 0;
        double hoy = 0;
        double mes = 0;
        double ganancia = 0;

        Calendar c = Calendar.getInstance();
        int d = c.get(Calendar.DAY_OF_YEAR);
        int m = c.get(Calendar.MONTH);
        int a = c.get(Calendar.YEAR);

        for (MovimientoInventario x : lista) {

            if (x.getTipo().equals("SALIDA")) {

                total += x.getMonto();

                Calendar f = Calendar.getInstance();
                f.setTimeInMillis(x.getFecha());

                if (f.get(Calendar.DAY_OF_YEAR) == d && f.get(Calendar.YEAR) == a)
                    hoy += x.getMonto();

                if (f.get(Calendar.MONTH) == m && f.get(Calendar.YEAR) == a)
                    mes += x.getMonto();

                // 🔥 GANANCIA REAL
                Producto p = daoProducto.Buscar(x.getIdProducto());

                if (p != null) {
                    double gananciaUnidad = p.getPrecioVenta() - p.getPrecioCompra();
                    ganancia += gananciaUnidad * x.getCantidad();
                }
            }
        }

        txtVentasTotales.setText("S/ " + total);
        txtVentasDia.setText("S/ " + hoy);
        txtVentasMes.setText("S/ " + mes);
        txtGanancia.setText("S/ " + ganancia);

     /*   lstMovimientos.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        lista)
        );*/
        MovimientoAdapter adapter = new MovimientoAdapter(this, lista);
        lstMovimientos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
    }
}