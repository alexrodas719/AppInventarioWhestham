package upn.edu.pe.inventariowh;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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
    Uri foto;
    byte[] bFoto;

    private int idProducto = 0;

    Button buttonGuardarProducto;
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
        btnSalir = findViewById(R.id.btsalir);

        btnSalir.setOnClickListener(v -> {
            Intent intent = new Intent(AgregarProducto.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        foto=null;
        bFoto=null;
        imgFoto = findViewById(R.id.imgFoto);
        imgFoto.setOnClickListener(v -> adjuntarImagen());
        autoCompleteCategoria = findViewById(R.id.autoCompleteCategoria);
        autoCompleteTalla = findViewById(R.id.autoCompleteTalla);
        autoCompleteColor = findViewById(R.id.autoCompleteColor);

        textInputNombre = findViewById(R.id.textInputNombre);
        textInputStock = findViewById(R.id.textInputStock);
        textInputSKU = findViewById(R.id.textInputSKU);
        textInputPrecioCompra = findViewById(R.id.textInputPrecioCompra);
        textInputPrecioVenta = findViewById(R.id.textInputPrecioVenta);
        textInputDescripcion = findViewById(R.id.textInputDescripcion);

        buttonGuardarProducto = findViewById(R.id.buttonGuardarProducto);


        buttonGuardarProducto.setOnClickListener(boton -> { GuardarProducto(); });

        idProducto = getIntent().getIntExtra(
                "ID_PRODUCTO",
                0
        );

        autoCompleteCategoria.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categorias));
        autoCompleteTalla.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tallas));
        autoCompleteColor.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, colores));

        if(idProducto > 0){

            DAOProducto dao = new DAOProducto(this);

            Producto p = dao.Buscar(idProducto);

            if(p != null){

                textInputNombre.setText(p.getNombre());

                textInputSKU.setText(p.getSku());

                textInputStock.setText(
                        String.valueOf(p.getStock())
                );

                textInputPrecioCompra.setText(
                        String.valueOf(p.getPrecioCompra())
                );

                textInputPrecioVenta.setText(
                        String.valueOf(p.getPrecioVenta())
                );

                textInputDescripcion.setText(
                        p.getDescripcion()
                );

                autoCompleteTalla.setText(
                        p.getTalla(),
                        false
                );

                autoCompleteColor.setText(
                        p.getColor(),
                        false
                );

                bFoto = p.getFoto();

                if(bFoto != null){

                    Bitmap bitmap =
                            BitmapFactory.decodeByteArray(
                                    bFoto,
                                    0,
                                    bFoto.length
                            );

                    imgFoto.setImageBitmap(bitmap);
                }

                buttonGuardarProducto.setText(
                        "Actualizar Producto"
                );
            }
        }
    }
    private void adjuntarImagen()
    {
        Intent oIntento = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        oIntento.setType("image/*");
        startActivityIfNeeded(Intent.createChooser(oIntento, "Selecciona una foto"), 10);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10){//Actividad del sistema funcionando
            if (resultCode==RESULT_OK){//Se ha seleccionado una imagen y esta retornando
                foto= data.getData();
                imgFoto.setImageURI(foto);
                imgFoto.buildDrawingCache();
                Bitmap oImagen=imgFoto.getDrawingCache();
                //Flujo de salida

                ByteArrayOutputStream flujo = new ByteArrayOutputStream();
                oImagen.compress(Bitmap.CompressFormat.PNG,0,flujo);
                bFoto=flujo.toByteArray();

            }
        }


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
            oProducto.setFoto(bFoto);
            oProducto.setIdProducto(idProducto);
            ImageButton btnSalir = findViewById(R.id.btsalir);



            DAOProducto daoProducto = new DAOProducto(this);

            if (idProducto == 0) {
                if (daoProducto.Insertar(oProducto)) {

                    new androidx.appcompat.app.AlertDialog.Builder(AgregarProducto.this)
                            .setTitle("Éxito")
                            .setMessage("Producto guardado correctamente")

                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, which) -> {
                                dialog.dismiss();
                                finish(); // ✔ SOLO AQUÍ
                            })
                            .show();
                }

            } else {

                if (daoProducto.Actualizar(oProducto)) {

                    // 🎨 TÍTULO CON COLOR (FORZADO)
                    SpannableString title = new SpannableString("Éxito");
                    title.setSpan(
                            new ForegroundColorSpan(Color.GREEN),
                            0,
                            title.length(),
                            0
                    );

                    AlertDialog dialog = new AlertDialog.Builder(AgregarProducto.this)
                            .setTitle(title)
                            .setMessage("Producto actualizado correctamente...")

                            .setPositiveButton("OK", (d, w) -> {
                                finish();
                            })
                            .create();

                    dialog.show();

                    // 🎨 COLOR DEL BOTÓN
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setTextColor(Color.BLUE);

                    // 🎨 COLOR DEL MENSAJE
                    TextView message = dialog.findViewById(android.R.id.message);
                    if (message != null) {
                        message.setTextColor(Color.DKGRAY);
                    }
                }
            }


        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error en los datos numéricos", Toast.LENGTH_SHORT).show();
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

        if(bFoto==null){
            Toast.makeText(this, "seleccionar una foto de Producto", Toast.LENGTH_SHORT).show();
            return false;
        }

        return esValido;
    }
}