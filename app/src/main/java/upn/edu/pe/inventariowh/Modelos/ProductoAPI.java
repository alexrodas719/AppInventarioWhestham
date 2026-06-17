package upn.edu.pe.inventariowh.Modelos;

public class ProductoAPI
{
    private int IdProducto;
    private String Nombre;
    private String Foto;
    private String Ruta;
    private String SKU;
    private int IdCategoria;

    private String Talla;
    private String Color;

    private int Stock;
    private double PrecioCompra;
    private double PrecioVenta;

    private String Descripcion;
    public ProductoAPI() {
    }

    public ProductoAPI(int IdProducto, String Nombre, String Foto, String Ruta, String SKU, int IdCategoria, String Talla, String Color, int Stock, double PrecioCompra, double PrecioVenta, String Descripcion) {
        this.IdProducto = IdProducto;
        this.Nombre = Nombre;
        this.Foto = Foto;
        this.Ruta = Ruta;
        this.SKU = SKU;
        this.IdCategoria = IdCategoria;
        this.Talla = Talla;
        this.Color = Color;
        this.Stock = Stock;
        this.PrecioCompra = PrecioCompra;
        this.PrecioVenta = PrecioVenta;
        this.Descripcion = Descripcion;
    }

    public int getIdProducto() {
        return IdProducto;
    }

    public void setIdProducto(int IdProducto) {
        this.IdProducto = IdProducto;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String Foto) {
        this.Foto = Foto;
    }

    public String getRuta() {
        return Ruta;
    }

    public void setRuta(String Ruta) {
        this.Ruta = Ruta;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public int getIdCategoria() {
        return IdCategoria;
    }

    public void setIdCategoria(int IdCategoria) {
        this.IdCategoria = IdCategoria;
    }

    public String getTalla() {
        return Talla;
    }

    public void setTalla(String Talla) {
        this.Talla = Talla;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String Color) {
        this.Color = Color;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int Stock) {
        this.Stock = Stock;
    }

    public double getPrecioCompra() {
        return PrecioCompra;
    }

    public void setPrecioCompra(double PrecioCompra) {
        this.PrecioCompra = PrecioCompra;
    }

    public double getPrecioVenta() {
        return PrecioVenta;
    }

    public void setPrecioVenta(double PrecioVenta) {
        this.PrecioVenta = PrecioVenta;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }
}
