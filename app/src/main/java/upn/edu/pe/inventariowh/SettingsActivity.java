package upn.edu.pe.inventariowh;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.Locale;
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
        btnCategories.setOnClickListener(v -> {
            Intent intent = new Intent(this, ManageCategoriesActivity.class);
            startActivity(intent);
        });
        btnColors.setOnClickListener(v -> Toast.makeText(this, "Función para editar colores", Toast.LENGTH_SHORT).show());
        btnSizes.setOnClickListener(v -> Toast.makeText(this, "Función para editar tallas", Toast.LENGTH_SHORT).show());

        TextView txtLanguage = findViewById(R.id.txtLanguage);
        findViewById(R.id.txtLanguage).setOnClickListener(v -> {
            String current = txtLanguage.getText().toString();
            if (current.equals("Español")) {
                cambiarIdioma("en");
            } else {
                cambiarIdioma("es");
            }
        });

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

    private void cambiarIdioma(String codigoIdioma) {
        Locale locale = new Locale(codigoIdioma);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, dm);

        // Reiniciar la actividad para aplicar cambios
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
