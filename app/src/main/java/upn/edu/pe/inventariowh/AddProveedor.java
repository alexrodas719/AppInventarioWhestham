package upn.edu.pe.inventariowh;

import androidx.appcompat.app.AlertDialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import upn.edu.pe.inventariowh.Modelos.Proveedor;
import upn.edu.pe.inventariowh.Red.RetrofitCliente;
import upn.edu.pe.inventariowh.Red.ServicioAPI;
import upn.edu.pe.inventariowh.util.ProveedorAdapter;

public class AddProveedor extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private TextInputEditText etNombre, etRuc, etTelefono, etDireccion, etLatitud, etLongitud;
    private FloatingActionButton fabSave;
    private GoogleMap oMapa;
    private FusedLocationProviderClient LeerGPSCliente;
    Button btnUbicacionActual;
    ImageButton salir;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_proveedor);

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombreProveedor);
        etRuc = findViewById(R.id.etRucProveedor);
        etTelefono = findViewById(R.id.etTelefonoProveedor);
        etDireccion = findViewById(R.id.etDireccionProveedor);
        etLatitud = findViewById(R.id.etLatitud);
        etLongitud = findViewById(R.id.etLongitud);
        fabSave = findViewById(R.id.fabSaveProveedor);
        btnUbicacionActual = findViewById(R.id.btnUbicacion);
        salir = findViewById(R.id.btsalirProv);

        salir.setOnClickListener(v -> {Intent intent = new Intent(AddProveedor.this, ProveedorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();});

        //  ubicación del usuario
        LeerGPSCliente = LocationServices.getFusedLocationProviderClient(this);
        btnUbicacionActual.setOnClickListener(v -> getUbicacionActual());

        // Configurar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapContainer);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fabSave.setOnClickListener(v -> {
            OnClickguardarProveedor();
        });
    }
    private void getUbicacionActual() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    10);

            return;
        }

        LeerGPSCliente.getLastLocation()
                .addOnSuccessListener(this, location -> {

                    if (location != null) {

                        double lat = location.getLatitude();
                        double lon = location.getLongitude();

                        // Llenar campos
                        etLatitud.setText(String.valueOf(lat));
                        etLongitud.setText(String.valueOf(lon));

                        // Obtener dirección automáticamente
                        try {

                            android.location.Geocoder geocoder =
                                    new android.location.Geocoder(
                                            this,
                                            java.util.Locale.getDefault());

                            java.util.List<android.location.Address> direcciones =
                                    geocoder.getFromLocation(
                                            lat,
                                            lon,
                                            1);

                            if (direcciones != null &&
                                    !direcciones.isEmpty()) {

                                String direccion =
                                        direcciones.get(0)
                                                .getAddressLine(0);

                                etDireccion.setText(direccion);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Mostrar ubicación en mapa
                        LatLng puntoActual =
                                new LatLng(lat, lon);

                        oMapa.clear();

                        BitmapDescriptor icono =
                                BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_AZURE);

                        oMapa.addMarker(
                                new MarkerOptions()
                                        .position(puntoActual)
                                        .title("Ubicación Actual")
                                        .icon(icono));

                        oMapa.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                        puntoActual,
                                        16f));

                        oMapa.setMyLocationEnabled(true);

                        Toast.makeText(
                                this,
                                "Ubicación obtenida correctamente",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        Toast.makeText(
                                this,
                                "No se pudo obtener la ubicación",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        oMapa = googleMap;
        // Ubicación por defecto cajamarca
        LatLng puntoUPN = new LatLng(-12.046374, -77.042793);
        //configuracion del mapa
        oMapa.getUiSettings().setZoomControlsEnabled(true);
        oMapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        oMapa.moveCamera(CameraUpdateFactory.newLatLngZoom(puntoUPN, 12));
        oMapa.addMarker(new MarkerOptions().position(puntoUPN).title("Punto UPN"));
        // Evento de clic en el mapa para capturar coordenadas
        oMapa.setOnMapClickListener(this);
    }
    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        oMapa.clear();
        oMapa.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada"));

        etLatitud.setText(String.valueOf(latLng.latitude));
        etLongitud.setText(String.valueOf(latLng.longitude));
    }
    private void OnClickguardarProveedor() {
        String nombre = etNombre.getText().toString().trim();
        String ruc = etRuc.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String lat = etLatitud.getText().toString().trim();
        String lon = etLongitud.getText().toString().trim();
        //validaciones en nombre y ubicacion
        if (nombre.isEmpty() || ruc.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || lat.isEmpty() || lon.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos y selecciona una ubicación", Toast.LENGTH_SHORT).show();
            return;
        }
        double dLat = Double.parseDouble(lat);
        double dLon = Double.parseDouble(lon);
        // Aquí iría la lógica para guardar en la base de datos
        Proveedor oP = new Proveedor(0,nombre, ruc, telefono, direccion, dLat, dLon);
        EnviarPost(oP);
    }

    private void EnviarPost(Proveedor oP) {
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setPadding(40, 40, 40, 40);

        AlertDialog oProgreso = new AlertDialog.Builder(this)
                .setTitle("Registrando proveedor...")
                .setView(progressBar)
                .setCancelable(false)
                .create();
        oProgreso.show();

        ServicioAPI apiServicio = RetrofitCliente.getCliente().create(ServicioAPI.class);
        apiServicio.PostProveedor(oP).enqueue(new Callback<Proveedor>() {
            @Override
            public void onResponse(Call<Proveedor> call, Response<Proveedor> response) {
                oProgreso.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(AddProveedor.this, "Sincronizado con el servidor", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String errorExacto = response.errorBody().string();

                        // ESTA ES LA LÍNEA NUEVA (Log.e en lugar de System.out)
                        // Esto nos dirá el número real (ej: 400, 404, 415, 500)
                        Log.e("ERROR_API", "CÓDIGO REAL: " + response.code() + " | MENSAJE: " + response.message());

                        Toast.makeText(AddProveedor.this, "Error 400: Revisa el Logcat", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        // Esto nos dirá el número real (ej: 400, 404, 415, 500)
                        Log.e("ERROR_API", "CÓDIGO REAL: " + response.code() + " | MENSAJE: " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<Proveedor> call, Throwable t) {
                oProgreso.dismiss();
                Toast.makeText(AddProveedor.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
