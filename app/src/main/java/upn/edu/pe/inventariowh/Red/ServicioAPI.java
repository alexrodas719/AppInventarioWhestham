package upn.edu.pe.inventariowh.Red;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import upn.edu.pe.inventariowh.Modelos.Proveedor;


public interface ServicioAPI {
    @GET("Proveedor")
    Call<List<Proveedor>>GetProveedores();
    @Multipart
    @POST("Proveedor")
    Call<Proveedor> PostProveedor(
            @Part("Nombre") RequestBody nombre,
            @Part("Telefono") RequestBody telefono,
            @Part("Direccion") RequestBody direccion,
            @Part("Latitud") RequestBody latitud,
            @Part("Longitud") RequestBody longitud
    );
}
