package upn.edu.pe.inventariowh.Red;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import upn.edu.pe.inventariowh.Modelos.ProductoAPI;
import upn.edu.pe.inventariowh.Modelos.Proveedor;
import upn.edu.pe.inventariowh.Modelos.Venta;


public interface ServicioAPI {
    // --- PROVEEDORES ---
    @GET("Proveedores")
    Call<List<Proveedor>> GetProveedores();

    @POST("Proveedores")
    Call<Proveedor> PostProveedor(@Body Proveedor proveedor);

    @PUT("Proveedores/{id}")
    Call<Proveedor> PutProveedor(
            @Path("id") int id,
            @Body Proveedor proveedor
    );
    @DELETE("Proveedores/{id}")
    Call<Void> DeleteProveedor(
            @Path("id") int id
    );
    // --- PRODUCTOS ---
    @GET("Producto")
    Call<List<ProductoAPI>> GetProductos();

    @GET("Producto/{id}")
    Call<ProductoAPI> GetProductoById(@Path("id") int id);

    @Multipart
    @POST("Producto")
    Call<ProductoAPI> PostProducto(
            @Part("Nombre") RequestBody Nombre,
            @Part("Foto") RequestBody Foto,
            @Part("Ruta") RequestBody Ruta,
            @Part("SKU") RequestBody SKU,
            @Part("IdCategoria") RequestBody IdCategoria,
            @Part("Talla") RequestBody Talla,
            @Part("Color") RequestBody Color,
            @Part("Stock") RequestBody Stock,
            @Part("PrecioCompra") RequestBody PrecioCompra,
            @Part("PrecioVenta") RequestBody PrecioVenta,
            @Part("Descripcion") RequestBody Descripcion,
            @Part MultipartBody.Part Archivo // El nombre debe coincidir con la API
    );

    @Multipart
    @PUT("Producto/{id}")
    Call<ProductoAPI> PutProducto(
            @Path("id") int id,
            @Part("IdProducto") RequestBody idFormulario, // ¡Esta línea es la clave!
            @Part("Nombre") RequestBody Nombre,
            @Part("Foto") RequestBody Foto,
            @Part("Ruta") RequestBody Ruta,
            @Part("SKU") RequestBody SKU,
            @Part("IdCategoria") RequestBody IdCategoria,
            @Part("Talla") RequestBody Talla,
            @Part("Color") RequestBody Color,
            @Part("Stock") RequestBody Stock,
            @Part("PrecioCompra") RequestBody PrecioCompra,
            @Part("PrecioVenta") RequestBody PrecioVenta,
            @Part("Descripcion") RequestBody Descripcion,
            @Part MultipartBody.Part Archivo
    );

    @DELETE("Producto/{id}")
    Call<Void> DeleteProducto(@Path("id") int id);
    @POST("Ventas")
    Call<Venta> PostVenta(@Body Venta venta);
    @GET("Ventas")
    Call<List<Venta>> GetVentas();
    @PUT("Ventas/{id}")
    Call<Venta> PutVenta(
            @Path("id") int id,
            @Body Venta venta
    );
    @DELETE("Ventas/{id}")
    Call<Void> DeleteVenta(
            @Path("id") int id
    );

}
