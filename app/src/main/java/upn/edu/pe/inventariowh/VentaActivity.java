package upn.edu.pe.inventariowh;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import upn.edu.pe.inventariowh.Modelos.DetalleVenta;
import upn.edu.pe.inventariowh.Modelos.MovimientoInventario;
import upn.edu.pe.inventariowh.Modelos.Producto;
import upn.edu.pe.inventariowh.Modelos.ProductoAPI;
import upn.edu.pe.inventariowh.Modelos.Venta;
import upn.edu.pe.inventariowh.Red.RetrofitCliente;
import upn.edu.pe.inventariowh.Red.ServicioAPI;

public class VentaActivity extends AppCompatActivity {
    //variables para los campos de texto
    EditText txtBuscar, txtCantidad;
    TextView txtProducto, txtStock, txtPrecio, txtTotal;
    Button btnBuscar, btnAgregar, btnRegistrarVenta;
    ListView lstCarrito;

    //variables auxiliares para la lógica de la venta
    ProductoAPI productoActual;
    //carritoo sera un objeto tipo ArrayList donde contenga en memoria los objetos DETALLEVENTA
    List<DetalleVenta> carrito = new ArrayList<>();
    ArrayAdapter<String> adapter;
    List<String> listaTexto = new ArrayList<>();
    // Catálogo descargado del servidor
    List<ProductoAPI> catalogoServidor = new ArrayList<>();

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
        // Descargamos el catálogo al entrar
        descargarCatalogo();
    }
    private void descargarCatalogo() {
        ServicioAPI api = RetrofitCliente.getCliente().create(ServicioAPI.class);
        api.GetProductos().enqueue(new Callback<List<ProductoAPI>>() {
            @Override
            public void onResponse(Call<List<ProductoAPI>> call, Response<List<ProductoAPI>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    catalogoServidor = response.body();
                }
            }
            @Override
            public void onFailure(Call<List<ProductoAPI>> call, Throwable t) {
                Toast.makeText(VentaActivity.this, "Error al cargar catálogo", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //debe buscar por nombre o sku
    private void buscarProducto() {

        String query = txtBuscar.getText().toString().trim().toLowerCase();
        if (query.isEmpty() || catalogoServidor.isEmpty()) return;

        productoActual = null;

        for (ProductoAPI p : catalogoServidor) {
            // Asumiendo que tu clase Producto tiene getSku()
            if (p.getNombre().toLowerCase().contains(query) || p.getSKU().toLowerCase().contains(query)) {
                productoActual = p;
                break;
            }
        }

        if (productoActual != null) {
            txtProducto.setText("Producto: " + productoActual.getNombre());
            txtStock.setText("Stock: " + productoActual.getStock());
            txtPrecio.setText("Precio: S/ " + productoActual.getPrecioVenta());
        } else {
            Toast.makeText(this, "No encontrado en el servidor", Toast.LENGTH_SHORT).show();
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
        v.setDetalles(carrito);
        ServicioAPI api = RetrofitCliente.getCliente().create(ServicioAPI.class);
        api.PostVenta(v).enqueue(new Callback<Venta>() {
            @Override
            public void onResponse(Call<Venta> call, Response<Venta> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(VentaActivity.this, "Venta registrada en la nube", Toast.LENGTH_SHORT).show();
                    carrito.clear();
                    listaTexto.clear();
                    adapter.notifyDataSetChanged();
                    actualizarTotal();

                    // Recargamos el catálogo para obtener el nuevo stock descontado por C#
                    descargarCatalogo();
                } else {
                    Toast.makeText(VentaActivity.this, "Error al registrar: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Venta> call, Throwable t) {
                Toast.makeText(VentaActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
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