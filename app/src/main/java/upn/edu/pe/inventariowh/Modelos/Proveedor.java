package upn.edu.pe.inventariowh.Modelos;

import com.google.gson.annotations.SerializedName;

public class Proveedor {
    @SerializedName(value = "idProveedor", alternate = {"IdProveedor"})
    private int idProveedor;

    @SerializedName(value = "razonSocial", alternate = {"RazonSocial"})
    private String RazonSocial;

    @SerializedName(value = "ruc", alternate = {"Ruc", "RUC"})
    private String ruc;

    @SerializedName(value = "telefono", alternate = {"Telefono"})
    private String telefono;

    @SerializedName(value = "direccion", alternate = {"Direccion"})
    private String direccion;

    @SerializedName(value = "latitud", alternate = {"Latitud"})
    private double latitud;

    @SerializedName(value = "longitud", alternate = {"Longitud"})
    private double longitud;

    public Proveedor() {
    }

    public Proveedor( String RazonSocial, String ruc, String telefono, String direccion, double latitud, double longitud) {

        this.RazonSocial = RazonSocial;
        this.ruc= ruc;
        this.telefono = telefono;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Getters y Setters
    public int getIdProveedor() {
        return idProveedor;
    }
    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String RazonSocial) {
        this.RazonSocial = RazonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return RazonSocial; // Útil para mostrar en Spinners o Listas
    }
}
