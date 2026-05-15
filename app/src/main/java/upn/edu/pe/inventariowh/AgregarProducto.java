package upn.edu.pe.inventariowh;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AgregarProducto extends AppCompatActivity {

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

        // Configurar dropdown de Categoría
        String[] categorias = {"Ropa", "Calzado", "Accesorios", "Otros"};
        ArrayAdapter<String> adapterCategorias = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categorias);
        AutoCompleteTextView autoCompleteCategoria = findViewById(R.id.autoCompleteCategoria);
        autoCompleteCategoria.setAdapter(adapterCategorias);

        // Configurar dropdown de Talla
        String[] tallas = {"S", "M", "L", "XL", "XXL"};
        ArrayAdapter<String> adapterTallas = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, tallas);
        AutoCompleteTextView autoCompleteTalla = findViewById(R.id.autoCompleteTalla);
        autoCompleteTalla.setAdapter(adapterTallas);

        // Configurar dropdown de Color
        String[] colores = {"Negro", "Blanco", "Rojo", "Azul", "Verde"};
        ArrayAdapter<String> adapterColores = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, colores);
        AutoCompleteTextView autoCompleteColor = findViewById(R.id.autoCompleteColor);
        autoCompleteColor.setAdapter(adapterColores);

        ImageButton btsalir = findViewById(R.id.btsalir);
        btsalir.setOnClickListener(v -> {
            finish(); // Cierra la actividad y vuelve a la anterior
        });
    }
}