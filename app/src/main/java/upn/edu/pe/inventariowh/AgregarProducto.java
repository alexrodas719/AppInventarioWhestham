package upn.edu.pe.inventariowh;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;

import upn.edu.pe.inventariowh.AccesoDatos.DAOCategoria;
import upn.edu.pe.inventariowh.AccesoDatos.DAOProducto;
import upn.edu.pe.inventariowh.Modelos.Categoria;
import upn.edu.pe.inventariowh.Modelos.Producto;

public class AgregarProducto extends AppCompatActivity {
    //variables para los componentes visuales
    ImageView imgFoto; AutoCompleteTextView autoCompleteCategoria;  AutoCompleteTextView autoCompleteTalla; AutoCompleteTextView autoCompleteColor;
    String[] categorias = {"Ropa", "Calzado", "Accesorios", "Otros"};   String[] tallas = {"S", "M", "L", "XL", "XXL"}; String[] colores = {"Negro", "Blanco", "Rojo", "Azul", "Verde"};
    TextInputEditText textInputNombre;  TextInputEditText textInputStock;   TextInputEditText textInputSKU;
    TextInputEditText textInputPrecioCompra;    TextInputEditText textInputPrecioVenta; TextInputEditText textInputDescripcion;
    Button buttonGuradarProducto;

    ActivityResultLauncher<Intent> lanzadorResultados;
    Uri foto;
    byte[] bfoto;
    ImageButton btnSalir;

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
        //vincular variables con sus componentes visuales
        btnSalir = findViewById(R.id.btsalir);
        btnSalir.setOnClickListener(v -> {
            Intent intent = new Intent(AgregarProducto.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
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
        buttonGuradarProducto.setOnClickListener(boton->{GuardarProducto();});
        lanzadorResultados = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imgFoto.setImageURI(result.getData().getData());
                        ProcesarImagen(result.getData().getData());
                    }
                });
        bfoto = null;
        foto = null;

        autoCompleteCategoria.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,categorias));
        autoCompleteTalla.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,tallas));
        autoCompleteColor.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,colores));
        imgFoto.setOnClickListener(
                v->AddImage()
        );

    }

    public void GuardarProducto() {
        if (!validar()) return;

        // Almacenar datos obtenidos de los componentes
        String talla = autoCompleteTalla.getText().toString();
        String color = autoCompleteColor.getText().toString();
        String nombreCategoria = autoCompleteCategoria.getText().toString();
        String nombreProducto = textInputNombre.getText().toString().trim();
        String SKU = textInputSKU.getText().toString().trim();
        String descripcion = textInputDescripcion.getText().toString().trim();

        // Conversión de String a int y double
        try {
            int stock = Integer.parseInt(textInputStock.getText().toString().trim());
            double precioCompra = Double.parseDouble(textInputPrecioCompra.getText().toString().trim());
            double precioVenta = Double.parseDouble(textInputPrecioVenta.getText().toString().trim());

            // 1. Obtener el ID de la categoría seleccionada
            DAOCategoria daoCategoria = new DAOCategoria(this);
            Categoria oC = daoCategoria.BuscarPorNombre(nombreCategoria);
            
            int idCategoria = 0;
            if (oC != null) {
                idCategoria = oC.getIdCategoria();
            }

            // 2y. Crear objeto Producto usando el constructor vacío  setters
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
            oProducto.setFoto(bfoto);
            // La foto puede quedar como null o vacía por ahora

            // 3. Guardar en la base de datos
            DAOProducto daoProducto = new DAOProducto(this);
            if (daoProducto.Insertar(oProducto)) {
                Toast.makeText(this, "Producto guardado correctamente", Toast.LENGTH_SHORT).show();
                finish(); // Opcional: cerrar actividad al guardar
            } else {
                Toast.makeText(this, "Error: El SKU ya existe o faltan datos", Toast.LENGTH_LONG).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error en formato de números (Stock/Precio)", Toast.LENGTH_SHORT).show();        }
    }
    public void AddImage(){
        Intent oIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        oIntent.setType("image/*");
        lanzadorResultados.launch(Intent.createChooser(oIntent, "selecionemos imagenes xd"));
    }
    public void ProcesarImagen(Uri uri){
        try {
            Bitmap oImagen;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // Forma moderna para Android 9+ (incluye Android 12)
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), uri);
                oImagen = ImageDecoder.decodeBitmap(source);
            } else {
                // Forma antigua para versiones anteriores
                oImagen = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            }

            // Redimensionar para no saturar la BD
            Bitmap reducida = Bitmap.createScaledBitmap(oImagen, 500, 500, true);
            ByteArrayOutputStream flujo = new ByteArrayOutputStream();
            reducida.compress(Bitmap.CompressFormat.JPEG, 70, flujo);
            bfoto = flujo.toByteArray();
        } catch (Exception e) {
            Toast.makeText(this, "Error al procesar imagen", Toast.LENGTH_SHORT).show();
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
        //validar precio de venta mayor a precio de compra
        if (!textInputPrecioCompra.getText().toString().trim().isEmpty() &&
            !textInputPrecioVenta.getText().toString().trim().isEmpty()) {
            double pCompra = Double.parseDouble(textInputPrecioCompra.getText().toString().trim());
            double pVenta = Double.parseDouble(textInputPrecioVenta.getText().toString().trim());
            if (pVenta <= pCompra) {
                textInputPrecioVenta.setError("El precio de venta debe ser mayor al de compra");
                esValido = false;
            }
        }
        return esValido;
    }
}