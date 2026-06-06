package upn.edu.pe.inventariowh;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import upn.edu.pe.inventariowh.AccesoDatos.DAOProducto;
import upn.edu.pe.inventariowh.Modelos.Producto;
import upn.edu.pe.inventariowh.R;

public class ProductoAdapter extends ArrayAdapter<Producto> {

    private Activity context;
    private List<Producto> lista;

    public ProductoAdapter(Activity context, List<Producto> lista) {
        super(context, R.layout.item_producto, lista);
        this.context = context;
        this.lista = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View item = inflater.inflate(
                R.layout.item_producto,
                null,
                true
        );

        ImageView img = item.findViewById(R.id.imgProducto);

        TextView nombre = item.findViewById(R.id.txtNombre);
        TextView sku = item.findViewById(R.id.txtSku);
        TextView talla = item.findViewById(R.id.txtTalla);
        TextView color = item.findViewById(R.id.txtColor);
        TextView stock = item.findViewById(R.id.txtStock);
        TextView precio = item.findViewById(R.id.txtPrecio);

        ImageButton btnEditar = item.findViewById(R.id.btnEditar);
        ImageButton btnEliminar = item.findViewById(R.id.btnEliminar);


        Producto p = lista.get(position);

        // AQUÍ VA EL CÓDIGO
        nombre.setText(p.getNombre());

        sku.setText("SKU: " + p.getSku());

        talla.setText("Talla: " + p.getTalla());

        color.setText("Color: " + p.getColor());

        stock.setText("Stock: " + p.getStock());


        precio.setText("Precio: S/ " + p.getPrecioVenta());

        byte[] foto = p.getFoto();

        if (foto != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(
                    foto,
                    0,
                    foto.length
            );
            img.setImageBitmap(bitmap);
        }
        btnEditar.setOnClickListener(v -> {

            Intent intent = new Intent(context, AgregarProducto.class);

            intent.putExtra(
                    "ID_PRODUCTO",
                    p.getIdProducto()
            );

            context.startActivity(intent);

        });

        btnEliminar.setOnClickListener(v -> {

            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Eliminar Producto")
                    .setMessage("¿Está seguro que desea eliminar " + p.getNombre() + "?")
                    .setPositiveButton("Eliminar", (dialogInterface, which) -> {

                        DAOProducto dao = new DAOProducto(context);

                        if (dao.Eliminar(p.getIdProducto())) {

                            lista.remove(position);
                            notifyDataSetChanged();

                            Toast.makeText(
                                    context,
                                    "Producto eliminado",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .create();

            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(Color.RED);

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.BLUE);

            //colore de texto y titulo
            TextView title = dialog.findViewById(
                    context.getResources().getIdentifier("alertTitle", "id", "android")
            );

            TextView message = dialog.findViewById(
                    android.R.id.message
            );

            if (title != null) {
                title.setTextColor(Color.RED); // título rojo
            }

            if (message != null) {
                message.setTextColor(Color.DKGRAY); // mensaje gris oscuro
            }
        });
        return item;

    }}

