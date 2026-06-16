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
    //variables para los campos de texto
    EditText txtBuscar, txtCantidad;
    TextView txtProducto, txtStock, txtPrecio, txtTotal;
    Button btnBuscar, btnAgregar, btnRegistrarVenta;
    ListView lstCarrito;

    //varibles de los objetos con los que tiene relacion el objeto VENTA
    DAOProducto daoProducto;
    DAOVenta daoVenta;
    DAODetalleVenta daoDetalle;
    DAOMovimientoInventario daoMovimiento;

    //variables auxiliares para la lógica de la venta
    Producto productoActual;
    //carritoo sera un objeto tipo ArrayList donde contenga en memoria los objetos DETALLEVENTA
    List<DetalleVenta> carrito = new ArrayList<>();
    ArrayAdapter<String> adapter;
    List<String> listaTexto = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta);

        //Referenciar variables a sus componentes en pantalla
        txtBuscar = findViewById(R.id.txtbuscadorVenta);
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

        //eliminar del carrito con click largo
//        lstCarrito.setOnItemLongClickListener((parent, view, position, id) -> {
//            carrito.remove(position);
//            listaTexto.remove(position);
//            adapter.notifyDataSetChanged();
//            actualizarTotal();
//            Toast.makeText(this, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
//            return true;
//        });

        actualizarTotal();

    }
    //debe buscar por nombre o sku
    private void buscarProducto() {

        if (txtBuscar.getText().toString().isEmpty()) {
            return;
        }

        List<Producto> lista = daoProducto.Filtrar(txtBuscar.getText().toString());

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
        if (!Validar()) {
            return;
        }
        int cantidad = Integer.parseInt(txtCantidad.getText().toString());
        double subtotal = cantidad * productoActual.getPrecioVenta();

        DetalleVenta dventa = new DetalleVenta();
        dventa.setIdProducto(productoActual.getIdProducto());
        dventa.setCantidad(cantidad);
        dventa.setPrecioUnitario(productoActual.getPrecioVenta());
        dventa.setSubtotal(subtotal);

        carrito.add(dventa);

        listaTexto.add(productoActual.getNombre() + " x" + cantidad + " = S/ " + subtotal);

        adapter.notifyDataSetChanged();

        actualizarTotal();

        // Limpiar campos después de agregar
        txtBuscar.setText("");
        txtCantidad.setText("");
        txtProducto.setText("Producto: ");
        txtStock.setText("Stock: ");
        txtPrecio.setText("Precio: S/ 0.00");
        productoActual = null;
        txtBuscar.requestFocus();
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

                Producto p = daoProducto.Buscar(d.getIdProducto());

                if (p != null) {

                    int nuevoStock = p.getStock() - d.getCantidad();

                    daoProducto.ActualizarStock(p.getIdProducto(), nuevoStock);
                }

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

    private boolean Validar() {
        if (productoActual == null) {
            Toast.makeText(this, "Busca un producto primero", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtCantidad.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Ingrese una cantidad", Toast.LENGTH_SHORT).show();
            return false;
        }
        int cantidad = Integer.parseInt(txtCantidad.getText().toString());
        if (cantidad <= 0) {
            Toast.makeText(this, "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cantidad > productoActual.getStock()) {
            Toast.makeText(this, "Stock insuficiente", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}