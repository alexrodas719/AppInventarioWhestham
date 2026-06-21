package upn.edu.pe.inventariowh;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import upn.edu.pe.inventariowh.Modelos.Proveedor;
import upn.edu.pe.inventariowh.Red.RetrofitCliente;
import upn.edu.pe.inventariowh.Red.ServicioAPI;

public class EditarProveedor extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private TextInputEditText etNombre, etRuc, etTelefono, etDireccion, etLatitud, etLongitud;
    private FloatingActionButton fabUpdate;
    private GoogleMap oMapa;
    private FusedLocationProviderClient LeerGPSCliente;
    private Button btnUbicacion;
    private int idProveedor;
    private Proveedor proveedorActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_proveedor);

        idProveedor = getIntent().getIntExtra("ID_PROVEEDOR", -1);
        if (idProveedor == -1) {
            Toast.makeText(this, "Error: Proveedor no identificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombreEdit);
        etRuc = findViewById(R.id.etRucEdit);
        etTelefono = findViewById(R.id.etTelefonoEdit);
        etDireccion = findViewById(R.id.etDireccionEdit);
        etLatitud = findViewById(R.id.etLatitudEdit);
        etLongitud = findViewById(R.id.etLongitudEdit);
        fabUpdate = findViewById(R.id.fabUpdateProveedor);
        btnUbicacion = findViewById(R.id.btnUbicacionEdit);

        LeerGPSCliente = LocationServices.getFusedLocationProviderClient(this);
        btnUbicacion.setOnClickListener(v -> getUbicacionActual());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapContainerEdit);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        findViewById(R.id.btsalirProv).setOnClickListener(v -> finish());
        fabUpdate.setOnClickListener(v -> ActualizarProveedor());

        CargarDatosProveedor();
    }

    private void CargarDatosProveedor() {
        ServicioAPI api = RetrofitCliente.getCliente().create(ServicioAPI.class);
        api.GetProveedores().enqueue(new Callback<List<Proveedor>>() {
            @Override
            public void onResponse(Call<List<Proveedor>> call, Response<List<Proveedor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Proveedor p : response.body()) {
                        if (p.getIdProveedor() == idProveedor) {
                            proveedorActual = p;
                            LlenarCampos(p);
                            break;
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Proveedor>> call, Throwable t) {
                Toast.makeText(EditarProveedor.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LlenarCampos(Proveedor p) {
        etNombre.setText(p.getRazonSocial());
        etRuc.setText(p.getRuc());
        etTelefono.setText(p.getTelefono());
        etDireccion.setText(p.getDireccion());
        etLatitud.setText(String.valueOf(p.getLatitud()));
        etLongitud.setText(String.valueOf(p.getLongitud()));

        if (oMapa != null) {
            LatLng pos = new LatLng(p.getLatitud(), p.getLongitud());
            oMapa.addMarker(new MarkerOptions().position(pos).title(p.getRazonSocial()));
            oMapa.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f));
        }
    }

    private void ActualizarProveedor() {
        String nombre = etNombre.getText().toString().trim();
        String ruc = etRuc.getText().toString().trim();
        String telf = etTelefono.getText().toString().trim();
        String dir = etDireccion.getText().toString().trim();
        String latStr = etLatitud.getText().toString().trim();
        String lonStr = etLongitud.getText().toString().trim();

        if (nombre.isEmpty() || ruc.isEmpty() || telf.isEmpty() || dir.isEmpty() || latStr.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        proveedorActual.setRazonSocial(nombre);
        proveedorActual.setRuc(ruc);
        proveedorActual.setTelefono(telf);
        proveedorActual.setDireccion(dir);
        proveedorActual.setLatitud(Double.parseDouble(latStr));
        proveedorActual.setLongitud(Double.parseDouble(lonStr));

        ServicioAPI api = RetrofitCliente.getCliente().create(ServicioAPI.class);
        api.PutProveedor(idProveedor, proveedorActual).enqueue(new Callback<Proveedor>() {
            @Override
            public void onResponse(Call<Proveedor> call, Response<Proveedor> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditarProveedor.this, "Proveedor actualizado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditarProveedor.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Proveedor> call, Throwable t) {
                Toast.makeText(EditarProveedor.this, "Falla de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUbicacionActual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
            return;
        }
        LeerGPSCliente.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
                oMapa.clear();
                oMapa.addMarker(new MarkerOptions().position(pos).title("Mi ubicación"));
                oMapa.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f));
                etLatitud.setText(String.valueOf(pos.latitude));
                etLongitud.setText(String.valueOf(pos.longitude));
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        oMapa = googleMap;
        oMapa.setOnMapClickListener(this);
        if (proveedorActual != null) {
            LlenarCampos(proveedorActual);
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        oMapa.clear();
        oMapa.addMarker(new MarkerOptions().position(latLng).title("Nueva ubicación"));
        etLatitud.setText(String.valueOf(latLng.latitude));
        etLongitud.setText(String.valueOf(latLng.longitude));
    }
}
