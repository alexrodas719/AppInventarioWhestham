package upn.edu.pe.inventariowh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsActivity extends AppCompatActivity {

    private MaterialSwitch switchDarkMode, switchNotifications;
    private TextView btnCategories, btnColors, btnSizes;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchDarkMode = findViewById(R.id.switchDarkMode);
        switchNotifications = findViewById(R.id.switchNotifications);
        btnCategories = findViewById(R.id.btnManageCategories);
        btnColors = findViewById(R.id.btnManageColors);
        btnSizes = findViewById(R.id.btnManageSizes);
        bottomNav = findViewById(R.id.bottomNavigationView);

        // Modo Oscuro
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Eventos para botones de gestión
        btnCategories.setOnClickListener(v -> Toast.makeText(this, "Función para editar categorías", Toast.LENGTH_SHORT).show());
        btnColors.setOnClickListener(v -> Toast.makeText(this, "Función para editar colores", Toast.LENGTH_SHORT).show());
        btnSizes.setOnClickListener(v -> Toast.makeText(this, "Función para editar tallas", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnLogin).setOnClickListener(v -> Toast.makeText(this, "Ir a Login", Toast.LENGTH_SHORT).show());
        findViewById(R.id.btnLogout).setOnClickListener(v -> Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show());

        // Navegación Inferior
        bottomNav.setSelectedItemId(R.id.nav_configuracion);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inventario) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_movimientos) {
                startActivity(new Intent(this, MovimientoActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_proveedores) {
                startActivity(new Intent(this, ProveedorActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return true;
        });
    }
}
