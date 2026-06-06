package upn.edu.pe.inventariowh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import upn.edu.pe.inventariowh.AccesoDatos.DAODetalleVenta;
import upn.edu.pe.inventariowh.AccesoDatos.DAOProducto;
import upn.edu.pe.inventariowh.AccesoDatos.DAOMovimientoInventario;
import upn.edu.pe.inventariowh.AccesoDatos.DAOVenta;
import upn.edu.pe.inventariowh.Modelos.*;

public class VentaActivity extends AppCompatActivity {

    EditText txtCodigo, txtCantidad;
    TextView txtProducto, txtStock, txtPrecio, txtTotal;
    Button btnBuscar, btnAgregar, btnRegistrarVenta;
    ListView lstCarrito;

    DAOProducto daoProducto;
    DAOVenta daoVenta;
    DAODetalleVenta daoDetalle;
    DAOMovimientoInventario daoMovimiento;

    Producto productoActual;

    List<DetalleVenta> carrito = new ArrayList<>();

    ArrayAdapter<String> adapter;

    List<String> listaTexto = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta);



        txtCodigo = findViewById(R.id.txtCodigo);
        txtCantidad = findViewById(R.id.txtCantidad);

        txtProducto = findViewById(R.id.txtProducto);
        txtStock = findViewById(R.id.txtStock);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtTotal = findViewById(R.id.txtTotal);

        btnBuscar = findViewById(R.id.btnBuscar);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnRegistrarVenta = findViewById(R.id.btnRegistrarVenta);

        lstCarrito = findViewById(R.id.lstCarrito);

        daoProducto = new DAOProducto(this);
        daoVenta = new DAOVenta(this);
        daoDetalle = new DAODetalleVenta(this);
        daoMovimiento = new DAOMovimientoInventario(this);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaTexto);
        lstCarrito.setAdapter(adapter);

        btnBuscar.setOnClickListener(v -> buscarProducto());
        btnAgregar.setOnClickListener(v -> agregarCarrito());
        btnRegistrarVenta.setOnClickListener(v -> registrarVenta());

        actualizarTotal();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_movimientos) {
                startActivity(new Intent(VentaActivity.this, MovimientoActivity.class));
                return true;
            }

            if (id == R.id.nav_inventario) {
                startActivity(new Intent(VentaActivity.this, MainActivity.class));
                return true;
            }

            return false;
        });
    }


    private void buscarProducto() {

        String codigo = txtCodigo.getText().toString();

        if (codigo.isEmpty()) return;

        List<Producto> lista = daoProducto.Filtrar(codigo);

        if (!lista.isEmpty()) {
            productoActual = lista.get(0);

            txtProducto.setText("Producto: " + productoActual.getNombre());
            txtStock.setText("Stock: " + productoActual.getStock());
            txtPrecio.setText("Precio: S/ " + productoActual.getPrecioVenta());
        } else {
            Toast.makeText(this, "No encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void agregarCarrito() {

        if (productoActual == null) {
            Toast.makeText(this, "Busca un producto primero", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(txtCantidad.getText().toString());

        if (cantidad > productoActual.getStock()) {
            Toast.makeText(this, "Stock insuficiente", Toast.LENGTH_SHORT).show();
            return;
        }

        double subtotal = cantidad * productoActual.getPrecioVenta();

        DetalleVenta d = new DetalleVenta();
        d.setIdProducto(productoActual.getIdProducto());
        d.setCantidad(cantidad);
        d.setPrecioUnitario(productoActual.getPrecioVenta());
        d.setSubtotal(subtotal);

        carrito.add(d);

        listaTexto.add(productoActual.getNombre() + " x" + cantidad + " = S/ " + subtotal);

        adapter.notifyDataSetChanged();

        actualizarTotal();
    }

    private void actualizarTotal() {

        double total = 0;

        for (DetalleVenta d : carrito) {
            total += d.getSubtotal();
        }

        txtTotal.setText("S/ " + total);
    }

    private void registrarVenta() {

        if (carrito.isEmpty()) {
            Toast.makeText(this, "Carrito vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        double total = 0;

        for (DetalleVenta d : carrito) {
            total += d.getSubtotal();
        }

        Venta v = new Venta();
        v.setCodigo("V-" + System.currentTimeMillis());
        v.setFecha(System.currentTimeMillis());
        v.setSubtotal(total);
        v.setDescuento(0);
        v.setTotal(total);

        long idVenta = daoVenta.Insertar(v);

        if (idVenta > 0) {

            for (DetalleVenta d : carrito) {

                d.setIdVenta((int) idVenta);
                daoDetalle.Insertar(d);

                // 🔥 1. BUSCAR PRODUCTO ACTUALIZADO
                Producto p = daoProducto.Buscar(d.getIdProducto());

                if (p != null) {

                    int nuevoStock = p.getStock() - d.getCantidad();

                    // 🔥 2. ACTUALIZAR STOCK EN BD
                    daoProducto.ActualizarStock(p.getIdProducto(), nuevoStock);
                }

                // 🔥 MOVIMIENTO INVENTARIO
                MovimientoInventario m = new MovimientoInventario();
                m.setCodigo("MOV-" + System.currentTimeMillis());
                m.setTipo("SALIDA");
                m.setIdProducto(d.getIdProducto());
                m.setCantidad(d.getCantidad());
                m.setFecha(System.currentTimeMillis());
                m.setMonto(d.getSubtotal());
                m.setObservacion("Venta");

                daoMovimiento.Insertar(m);
            }

            Toast.makeText(this, "Venta registrada", Toast.LENGTH_SHORT).show();

            carrito.clear();
            listaTexto.clear();
            adapter.notifyDataSetChanged();

            actualizarTotal();
        }
    }
}