package upn.edu.pe.inventariowh.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import upn.edu.pe.inventariowh.Modelos.Proveedor;
import upn.edu.pe.inventariowh.R;

public class ProveedorAdapter extends RecyclerView.Adapter<ProveedorAdapter.ProveedorViewHolder> {

    private final List<Proveedor> listaProveedores;

    // Constructor
    public ProveedorAdapter(List<Proveedor> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    @NonNull
    @Override
    public ProveedorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_proveedor, parent, false);
        return new ProveedorViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProveedorViewHolder holder, int position) {
        Proveedor proveedor = listaProveedores.get(position);

        // Mapeo de datos usando los métodos de tu modelo
        holder.tvNombre.setText(proveedor.getRazonSocial());
        holder.tvRuc.setText("RUC: " + proveedor.getRuc());
        holder.tvTelefono.setText("Tel: " + proveedor.getTelefono());
        holder.tvDireccion.setText(proveedor.getDireccion());
    }

    @Override
    public int getItemCount() {
        return listaProveedores != null ? listaProveedores.size() : 0;
    }

    // ViewHolder que conecta únicamente con los elementos de texto
    public static class ProveedorViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvRuc, tvTelefono, tvDireccion;

        public ProveedorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProveedor);
            tvRuc = itemView.findViewById(R.id.tvRucProveedor);
            tvTelefono = itemView.findViewById(R.id.tvTelefonoProveedor);
            tvDireccion = itemView.findViewById(R.id.tvDireccionProveedor);
        }
    }
}
