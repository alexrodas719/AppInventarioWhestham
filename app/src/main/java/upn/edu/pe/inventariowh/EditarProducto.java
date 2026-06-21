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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import upn.edu.pe.inventariowh.AccesoDatos.DAOCategoria;
import upn.edu.pe.inventariowh.Modelos.Categoria;
import upn.edu.pe.inventariowh.Modelos.ProductoAPI;
import upn.edu.pe.inventariowh.Red.RetrofitCliente;
import upn.edu.pe.inventariowh.Red.ServicioAPI;

public class EditarProducto extends AppCompatActivity {

    ImageView imgFoto;
    AutoCompleteTextView autoCompleteCategoria, autoCompleteTalla, autoCompleteColor;
    String[] categorias = {"Ropa", "Calzado", "Accesorios", "Otros"};
    String[] tallas = {"S", "M", "L", "XL", "XXL"};
    String[] colores = {"Negro", "Blanco", "Rojo", "Azul", "Verde"};
    TextInputEditText textInputNombre, textInputStock, textInputSKU;
    TextInputEditText textInputPrecioCompra, textInputPrecioVenta, textInputDescripcion;
    Button buttonActualizar;
    ImageButton btnSalir;
    List<Categoria> listaCategoriasBD;

    ActivityResultLauncher<Intent> lanzadorResultados;
    byte[] bfoto = null;
    int idProducto;
    String rutaImagenOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);

        idProducto = getIntent().getIntExtra("ID_PRODUCTO", -1);
        if (idProducto == -1) {
            Toast.makeText(this, "Error: Producto no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Vincular vistas
        imgFoto = findViewById(R.id.imgFotoEdit);
        autoCompleteCategoria = findViewById(R.id.autoCompleteCategoriaEdit);
        autoCompleteTalla = findViewById(R.id.autoCompleteTallaEdit);
        autoCompleteColor = findViewById(R.id.autoCompleteColorEdit);
        textInputNombre = findViewById(R.id.textInputNombreEdit);
        textInputStock = findViewById(R.id.textInputStockEdit);
        textInputSKU = findViewById(R.id.textInputSKUEdit);
        textInputPrecioCompra = findViewById(R.id.textInputPrecioCompraEdit);
        textInputPrecioVenta = findViewById(R.id.textInputPrecioVentaEdit);
        textInputDescripcion = findViewById(R.id.textInputDescripcionEdit);
        buttonActualizar = findViewById(R.id.buttonActualizarProducto);
        btnSalir = findViewById(R.id.btsalirProv);

        // Adaptadores
        cargarCategorias();
        autoCompleteTalla.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tallas));
        autoCompleteColor.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, colores));

        lanzadorResultados = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imgFoto.setImageURI(result.getData().getData());
                        ProcesarImagen(result.getData().getData());
                    }
                });

        imgFoto.setOnClickListener(v -> AddImage());
        btnSalir.setOnClickListener(v -> finish());
        buttonActualizar.setOnClickListener(v -> ActualizarProducto());

        CargarDatosProducto();
    }

    private void cargarCategorias() {
        DAOCategoria daoCat = new DAOCategoria(this);
        listaCategoriasBD = daoCat.ListarTodos();

        List<String> nombres = new ArrayList<>();
        for (Categoria c : listaCategoriasBD) {
            nombres.add(c.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombres);
        autoCompleteCategoria.setAdapter(adapter);
    }

    private void CargarDatosProducto() {
        ServicioAPI servicio = RetrofitCliente.getCliente().create(ServicioAPI.class);
        servicio.GetProductos().enqueue(new Callback<List<ProductoAPI>>() {
            @Override
            public void onResponse(Call<List<ProductoAPI>> call, Response<List<ProductoAPI>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (ProductoAPI p : response.body()) {
                        if (p.getIdProducto() == idProducto) {
                            LlenarCampos(p);
                            break;
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ProductoAPI>> call, Throwable t) {}
        });
    }

    private void LlenarCampos(ProductoAPI p) {
        textInputNombre.setText(p.getNombre());
        textInputSKU.setText(p.getSKU());
        textInputStock.setText(String.valueOf(p.getStock()));
        textInputPrecioCompra.setText(String.valueOf(p.getPrecioCompra()));
        textInputPrecioVenta.setText(String.valueOf(p.getPrecioVenta()));
        textInputDescripcion.setText(p.getDescripcion());
        autoCompleteTalla.setText(p.getTalla(), false);
        autoCompleteColor.setText(p.getColor(), false);
        rutaImagenOriginal = p.getRuta();

        // Seleccionar categoría por ID desde la BD
        DAOCategoria daoCat = new DAOCategoria(this);
        Categoria cat = daoCat.Buscar(p.getIdCategoria());
        if (cat != null) {
            autoCompleteCategoria.setText(cat.getNombre(), false);
        }

        if (rutaImagenOriginal != null && !rutaImagenOriginal.isEmpty()) {
            Glide.with(this).load(rutaImagenOriginal).into(imgFoto);
        }
    }

    private void ActualizarProducto() {
        String nombre = textInputNombre.getText().toString().trim();
        String sku = textInputSKU.getText().toString().trim();
        String stock = textInputStock.getText().toString().trim();
        String pCompra = textInputPrecioCompra.getText().toString().trim();
        String pVenta = textInputPrecioVenta.getText().toString().trim();
        String desc = textInputDescripcion.getText().toString().trim();
        String talla = autoCompleteTalla.getText().toString();
        String color = autoCompleteColor.getText().toString();
        String nombreCategoria = autoCompleteCategoria.getText().toString();

        if (nombre.isEmpty() || stock.isEmpty() || pCompra.isEmpty() || pVenta.isEmpty()) {
            Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        int idCategoriaSeleccionada = 0;
        DAOCategoria daoCat = new DAOCategoria(this);
        Categoria oC = daoCat.BuscarPorNombre(nombreCategoria);
        if (oC != null) idCategoriaSeleccionada = oC.getIdCategoria();

        AlertDialog oProgreso = new AlertDialog.Builder(this)
                .setTitle("Actualizando...")
                .setCancelable(false)
                .create();
        oProgreso.show();

        RequestBody rbId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idProducto));
        RequestBody rbNombre = RequestBody.create(MediaType.parse("text/plain"), nombre);
        RequestBody rbSku = RequestBody.create(MediaType.parse("text/plain"), sku);
        RequestBody rbStock = RequestBody.create(MediaType.parse("text/plain"), stock);
        RequestBody rbPC = RequestBody.create(MediaType.parse("text/plain"), pCompra);
        RequestBody rbPV = RequestBody.create(MediaType.parse("text/plain"), pVenta);
        RequestBody rbDesc = RequestBody.create(MediaType.parse("text/plain"), desc);
        RequestBody rbTalla = RequestBody.create(MediaType.parse("text/plain"), talla);
        RequestBody rbColor = RequestBody.create(MediaType.parse("text/plain"), color);
        RequestBody rbCat = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idCategoriaSeleccionada));
        RequestBody rbFoto = RequestBody.create(MediaType.parse("text/plain"), "no");
        RequestBody rbRuta = RequestBody.create(MediaType.parse("text/plain"), rutaImagenOriginal != null ? rutaImagenOriginal : "no");

        // --- SOLUCIÓN IMAGEN OPCIONAL ---
        MultipartBody.Part archivoFisico;
        if (bfoto != null) {
            // Si eligió una imagen nueva, la enviamos normal
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bfoto);
            archivoFisico = MultipartBody.Part.createFormData("Archivo", "producto.jpg", requestFile);
        } else {
            // Si NO eligió imagen, enviamos un Multipart vacío para evitar el Error 400
            RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), "");
            archivoFisico = MultipartBody.Part.createFormData("Archivo", "", requestFile);
        }

        ServicioAPI servicio = RetrofitCliente.getCliente().create(ServicioAPI.class);
        servicio.PutProducto(idProducto,rbId, rbNombre, rbFoto, rbRuta, rbSku, rbCat, rbTalla, rbColor, rbStock, rbPC, rbPV, rbDesc, archivoFisico)
                .enqueue(new Callback<ProductoAPI>() {
                    @Override
                    public void onResponse(Call<ProductoAPI> call, Response<ProductoAPI> response) {
                        oProgreso.dismiss();
                        if (response.isSuccessful()) {
                            Toast.makeText(EditarProducto.this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditarProducto.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductoAPI> call, Throwable t) {
                        oProgreso.dismiss();
                        Toast.makeText(EditarProducto.this, "Falla: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void AddImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        lanzadorResultados.launch(Intent.createChooser(intent, "Selecciona una imagen"));
    }

    private void ProcesarImagen(Uri uri) {
        try {
            Bitmap bitmap;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri));
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }
            Bitmap reducida = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
            ByteArrayOutputStream flujo = new ByteArrayOutputStream();
            reducida.compress(Bitmap.CompressFormat.JPEG, 70, flujo);
            bfoto = flujo.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
