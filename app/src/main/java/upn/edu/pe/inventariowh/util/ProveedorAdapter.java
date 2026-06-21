package upn.edu.pe.inventariowh.util;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import upn.edu.pe.inventariowh.EditarProveedor;
import upn.edu.pe.inventariowh.Modelos.Proveedor;
import upn.edu.pe.inventariowh.R;
import upn.edu.pe.inventariowh.Red.RetrofitCliente;
import upn.edu.pe.inventariowh.Red.ServicioAPI;

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

        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditarProveedor.class);
            intent.putExtra("ID_PROVEEDOR", proveedor.getIdProveedor());
            v.getContext().startActivity(intent);
        });

        holder.btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Eliminar Proveedor")
                    .setMessage("¿Estás seguro de eliminar a " + proveedor.getRazonSocial() + "?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        EliminarProveedor(v.getContext(), proveedor, position);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void EliminarProveedor(android.content.Context context, Proveedor p, int position) {
        ServicioAPI api = RetrofitCliente.getCliente().create(ServicioAPI.class);
        api.DeleteProveedor(p.getIdProveedor()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listaProveedores.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, listaProveedores.size());
                    Toast.makeText(context, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProveedores != null ? listaProveedores.size() : 0;
    }

    // ViewHolder que conecta únicamente con los elementos de texto
    public static class ProveedorViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvRuc, tvTelefono, tvDireccion;
        Button btnEditar, btnEliminar;

        public ProveedorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProveedor);
            tvRuc = itemView.findViewById(R.id.tvRucProveedor);
            tvTelefono = itemView.findViewById(R.id.tvTelefonoProveedor);
            tvDireccion = itemView.findViewById(R.id.tvDireccionProveedor);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
