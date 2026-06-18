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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


import upn.edu.pe.inventariowh.AccesoDatos.DAOProducto;
import upn.edu.pe.inventariowh.Modelos.ProductoAPI;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private Activity context;
    private List<ProductoAPI> lista;

    public ProductoAdapter(Activity context, List<ProductoAPI> lista) {
        this.context = context;
        this.lista = lista;
    }
    @NonNull
    @Override
    public ProductoAdapter.ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter.ProductoViewHolder holder, int position) {
        ProductoAPI p = lista.get(position);

        holder.nombre.setText(p.getNombre());
        holder.sku.setText("SKU: " + p.getSKU());
        holder.talla.setText("Talla " + p.getTalla());
        holder.color.setText("Color " + p.getColor());
        holder.stock.setText("Stock: " + p.getStock());
        holder.precio.setText("Precio S/ " + p.getPrecioVenta());

        // 2. CARGAMOS LA IMAGEN CON GLIDE DESDE LA URL DEL SERVIDOR
        String rutaImagen = p.getRuta(); // O p.getFoto(), dependiendo de cómo lo llames en tu ProductoAPI

        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            Glide.with(context)
                    .load(rutaImagen)
                    .placeholder(R.drawable.ic_launcher_background) // Imagen temporal mientras carga
                    .into(holder.img);
        } else {
            holder.img.setImageBitmap(null);
        }
        // EVENTO EDITAR
        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(context, AgregarProducto.class);
            intent.putExtra("ID_PRODUCTO", p.getIdProducto());
            context.startActivity(intent);
        });

        // EVENTO ELIMINAR
        holder.btnEliminar.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Eliminar Producto")
                    .setMessage("¿Está seguro que desea eliminar " + p.getNombre() + "?")
                    .setPositiveButton("Eliminar", (dialogInterface, which) -> {

                        DAOProducto dao = new DAOProducto(context);

                        if (dao.Eliminar(p.getIdProducto())) {
                            // IMPORTANTE: Obtener la posición actual de la vista en el Recycler
                            int posicionActual = holder.getAdapterPosition();

                            if (posicionActual != RecyclerView.NO_POSITION) {
                                lista.remove(posicionActual);

                                // Animación nativa de RecyclerView al eliminar
                                notifyItemRemoved(posicionActual);
                                notifyItemRangeChanged(posicionActual, lista.size());

                                Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .create();

            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE);

            TextView title = dialog.findViewById(context.getResources().getIdentifier("alertTitle", "id", "android"));
            TextView message = dialog.findViewById(android.R.id.message);

            if (title != null) title.setTextColor(Color.RED);
            if (message != null) message.setTextColor(Color.DKGRAY);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    // Se encarga de hacer los findViewById una sola vez por cada ítem
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView nombre, sku, talla, color, stock, precio;
        ImageButton btnEditar, btnEliminar;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgProducto);
            nombre = itemView.findViewById(R.id.txtNombre);
            sku = itemView.findViewById(R.id.txtSku);
            talla = itemView.findViewById(R.id.txtTalla);
            color = itemView.findViewById(R.id.txtColor);
            stock = itemView.findViewById(R.id.txtStock);
            precio = itemView.findViewById(R.id.txtPrecio);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}

