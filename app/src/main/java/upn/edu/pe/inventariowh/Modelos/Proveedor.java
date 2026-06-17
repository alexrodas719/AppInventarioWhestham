package upn.edu.pe.inventariowh.Modelos;

public class Proveedor {
    private int idProveedor;
    private String RazonSocial;
    private String telefono;
    private String direccion;
    private double latitud;
    private double longitud;

    public Proveedor() {
    }

    public Proveedor( String RazonSocial, String telefono, String direccion, double latitud, double longitud) {

        this.RazonSocial = RazonSocial;
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
