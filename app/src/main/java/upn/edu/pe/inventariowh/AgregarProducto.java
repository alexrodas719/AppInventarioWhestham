package upn.edu.pe.inventariowh;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import upn.edu.pe.inventariowh.AccesoDatos.DAOCategoria;
import upn.edu.pe.inventariowh.AccesoDatos.DAOProducto;
import upn.edu.pe.inventariowh.Modelos.Categoria;
import upn.edu.pe.inventariowh.Modelos.Producto;

public class AgregarProducto extends AppCompatActivity {

    ImageView imgFoto;
    AutoCompleteTextView autoCompleteCategoria;
    AutoCompleteTextView autoCompleteTalla;
    AutoCompleteTextView autoCompleteColor;

    String[] categorias = {"Ropa", "Calzado", "Accesorios", "Otros"};
    String[] tallas = {"S", "M", "L", "XL", "XXL"};
    String[] colores = {"Negro", "Blanco", "Rojo", "Azul", "Verde"};

    TextInputEditText textInputNombre;
    TextInputEditText textInputStock;
    TextInputEditText textInputSKU;
    TextInputEditText textInputPrecioCompra;
    TextInputEditText textInputPrecioVenta;
    TextInputEditText textInputDescripcion;

    Button buttonGuradarProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_producto);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgFoto = findViewById(R.id.imgFoto);
        autoCompleteCategoria = findViewById(R.id.autoCompleteCategoria);
        autoCompleteTalla = findViewById(R.id.autoCompleteTalla);
        autoCompleteColor = findViewById(R.id.autoCompleteColor);

        textInputNombre = findViewById(R.id.textInputNombre);
        textInputStock = findViewById(R.id.textInputStock);
        textInputSKU = findViewById(R.id.textInputSKU);
        textInputPrecioCompra = findViewById(R.id.textInputPrecioCompra);
        textInputPrecioVenta = findViewById(R.id.textInputPrecioVenta);
        textInputDescripcion = findViewById(R.id.textInputDescripcion);

        buttonGuradarProducto = findViewById(R.id.buttonGuardarProducto);

        buttonGuradarProducto.setOnClickListener(boton -> { GuardarProducto(); });

        autoCompleteCategoria.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categorias));
        autoCompleteTalla.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tallas));
        autoCompleteColor.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, colores));
    }

    public void GuardarProducto() {

        if (!validar()) return;

        String talla = autoCompleteTalla.getText().toString();
        String color = autoCompleteColor.getText().toString();
        String nombreCategoria = autoCompleteCategoria.getText().toString();

        String nombreProducto = textInputNombre.getText().toString().trim();
        String SKU = textInputSKU.getText().toString().trim();
        String descripcion = textInputDescripcion.getText().toString().trim();

        try {

            int stock = Integer.parseInt(textInputStock.getText().toString().trim());
            double precioCompra = Double.parseDouble(textInputPrecioCompra.getText().toString().trim());
            double precioVenta = Double.parseDouble(textInputPrecioVenta.getText().toString().trim());

            DAOCategoria daoCategoria = new DAOCategoria(this);
            Categoria oC = daoCategoria.BuscarPorNombre(nombreCategoria);

            int idCategoria = 0;
            if (oC != null) {
                idCategoria = oC.getIdCategoria();
            }

            Producto oProducto = new Producto();
            oProducto.setNombre(nombreProducto);
            oProducto.setSku(SKU);
            oProducto.setIdCategoria(idCategoria);
            oProducto.setTalla(talla);
            oProducto.setColor(color);
            oProducto.setStock(stock);
            oProducto.setPrecioCompra(precioCompra);
            oProducto.setPrecioVenta(precioVenta);
            oProducto.setDescripcion(descripcion);

            DAOProducto daoProducto = new DAOProducto(this);

            if (daoProducto.Insertar(oProducto)) {
                Toast.makeText(this, "Producto guardado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }

        } catch (NumberFormatException e) {
        }
    }

    public boolean validar() {

        boolean esValido = true;

        if (textInputNombre.getText().toString().trim().isEmpty()) {
            textInputNombre.setError("Nombre requerido");
            esValido = false;
        }
        if (textInputStock.getText().toString().trim().isEmpty()) {
            textInputStock.setError("Stock requerido");
            esValido = false;
        }
        if (textInputPrecioCompra.getText().toString().trim().isEmpty()) {
            textInputPrecioCompra.setError("Precio requerido");
            esValido = false;
        }
        if (textInputPrecioVenta.getText().toString().trim().isEmpty()) {
            textInputPrecioVenta.setError("Precio requerido");
            esValido = false;
        }

        return esValido;
    }
}