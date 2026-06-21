package upn.edu.pe.inventariowh;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import upn.edu.pe.inventariowh.AccesoDatos.DAOCategoria;
import upn.edu.pe.inventariowh.Modelos.Categoria;

public class ManageCategoriesActivity extends AppCompatActivity {

    private TextInputEditText etName, etDesc;
    private Button btnSave;
    private RecyclerView rvCategories;
    private DAOCategoria dao;
    private List<Categoria> listaCategorias;
    private CategoryAdapter adapter;
    private int idEditando = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        etName = findViewById(R.id.etCategoryName);
        etDesc = findViewById(R.id.etCategoryDesc);
        btnSave = findViewById(R.id.btnSaveCategory);
        rvCategories = findViewById(R.id.rvCategories);

        dao = new DAOCategoria(this);
        listaCategorias = dao.ListarTodos();

        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter();
        rvCategories.setAdapter(adapter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> guardarCategoria());
    }

    private void guardarCategoria() {
        String nombre = etName.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();

        if (nombre.isEmpty()) {
            etName.setError("Requerido");
            return;
        }

        if (idEditando == -1) {
            // Nuevo
            Categoria nueva = new Categoria(0, nombre, desc);
            if (dao.Insertar(nueva)) {
                Toast.makeText(this, "Categoría guardada", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Editar
            Categoria editada = new Categoria(idEditando, nombre, desc);
            if (dao.Actualizar(editada)) {
                Toast.makeText(this, "Categoría actualizada", Toast.LENGTH_SHORT).show();
                idEditando = -1;
                btnSave.setText("Guardar Categoría");
            }
        }

        etName.setText("");
        etDesc.setText("");
        actualizarLista();
    }

    private void actualizarLista() {
        listaCategorias.clear();
        listaCategorias.addAll(dao.ListarTodos());
        adapter.notifyDataSetChanged();
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CatViewHolder> {

        @NonNull
        @Override
        public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_category, parent, false);
            return new CatViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
            Categoria c = listaCategorias.get(position);
            holder.tvName.setText(c.getNombre());
            holder.tvDesc.setText(c.getDescripcion());

            holder.btnEdit.setOnClickListener(v -> {
                idEditando = c.getIdCategoria();
                etName.setText(c.getNombre());
                etDesc.setText(c.getDescripcion());
                btnSave.setText("Actualizar");
            });

            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(ManageCategoriesActivity.this)
                        .setTitle("Eliminar")
                        .setMessage("¿Desea eliminar esta categoría?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            if (dao.Eliminar(c.getIdCategoria())) {
                                actualizarLista();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            });
        }

        @Override
        public int getItemCount() {
            return listaCategorias.size();
        }

        class CatViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvDesc;
            ImageButton btnEdit, btnDelete;

            public CatViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvCatName);
                tvDesc = itemView.findViewById(R.id.tvCatDesc);
                btnEdit = itemView.findViewById(R.id.btnEditCat);
                btnDelete = itemView.findViewById(R.id.btnDeleteCat);
            }
        }
    }
}
