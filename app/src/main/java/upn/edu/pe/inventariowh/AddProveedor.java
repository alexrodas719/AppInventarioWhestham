package upn.edu.pe.inventariowh;

import android.Manifest;
import android.app.ProgressDialog;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import upn.edu.pe.inventariowh.Modelos.Proveedor;
import upn.edu.pe.inventariowh.Red.RetrofitCliente;
import upn.edu.pe.inventariowh.Red.ServicioAPI;

public class AddProveedor extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private TextInputEditText etNombre, etTelefono, etDireccion, etLatitud, etLongitud;
    private FloatingActionButton fabSave;
    private GoogleMap oMapa;
    private FusedLocationProviderClient LeerGPSCliente;
    Button btnUbicacionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_proveedor);

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombreProveedor);
        etTelefono = findViewById(R.id.etTelefonoProveedor);
        etDireccion = findViewById(R.id.etDireccionProveedor);
        etLatitud = findViewById(R.id.etLatitud);
        etLongitud = findViewById(R.id.etLongitud);
        fabSave = findViewById(R.id.fabSaveProveedor);
        btnUbicacionActual = findViewById(R.id.btnUbicacion);

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
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new
                    String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},10);
            return;
        }
        LeerGPSCliente.getLastLocation().addOnSuccessListener(this,location
                ->{
            if(location!=null){
                LatLng puntoActual = new LatLng(location.getLatitude(),location.getLongitude());
                BitmapDescriptor icono = BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                oMapa.addMarker(new MarkerOptions()
                        .position(puntoActual)
                        .title("Ubicación Actual")
                        .icon(icono));
                oMapa.moveCamera(CameraUpdateFactory.newLatLngZoom(puntoActual,15f));
                oMapa.setMyLocationEnabled(true);
            }
            else{
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
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
        String telefono = etTelefono.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String lat = etLatitud.getText().toString().trim();
        String lon = etLongitud.getText().toString().trim();
        //validaciones en nombre y ubicacion
        if (nombre.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || lat.isEmpty() || lon.isEmpty()) {
            Toast.makeText(this, "Por favor, completa el nombre y selecciona una ubicación", Toast.LENGTH_SHORT).show();
            return;
        }
        double dLat = Double.parseDouble(lat);
        double dLon = Double.parseDouble(lon);
        // Aquí iría la lógica para guardar en la base de datos
        Proveedor oP = new Proveedor(nombre,telefono,direccion,dLat,dLon);
        EnviarPost(oP);
    }

    private void EnviarPost(Proveedor oP) {
        ProgressDialog oProgreso = new ProgressDialog(this);
        oProgreso.setMessage("Registrando proveedor...");
        oProgreso.setCancelable(false);
        oProgreso.show();
        RequestBody rbNombre = RequestBody.create(MediaType.parse("text/plain"), oP.getRazonSocial());
        RequestBody rbTelefono = RequestBody.create(MediaType.parse("text/plain"), oP.getTelefono());
        RequestBody rbDireccion = RequestBody.create(MediaType.parse("text/plain"), oP.getDireccion());
        // Convertimos el double a String para el RequestBody
        RequestBody rbLatitud = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(oP.getLatitud()));
        RequestBody rbLongitud = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(oP.getLongitud()));

        ServicioAPI apiServicio = RetrofitCliente.getCliente().create(ServicioAPI.class);
        Call<Proveedor> call = apiServicio.PostProveedor(rbNombre, rbTelefono, rbDireccion, rbLatitud, rbLongitud);
        call.enqueue(new Callback<Proveedor>() {
            @Override
            public void onResponse(Call<Proveedor> call, Response<Proveedor> response) {
                oProgreso.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(AddProveedor.this, "Sincronizado con el servidor", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddProveedor.this, "Error al guardar: " + response.code(), Toast.LENGTH_SHORT).show();
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
