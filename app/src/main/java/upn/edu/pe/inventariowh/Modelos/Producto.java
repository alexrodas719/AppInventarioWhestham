package upn.edu.pe.inventariowh.Modelos;

public class Producto {

    private int idProducto;
    private String nombre;
    private String foto;
    private String sku;
    private int idCategoria;

    private String talla;
    private String color;

    private int stock;
    private double precioCompra;
    private double precioVenta;

    private String descripcion;

    public Producto() {
    }
    public Producto(int idProducto, String nombre, String foto, String sku, int idCategoria, String talla,
                    String color, int stock, double precioCompra,double precioVenta, String descripcion) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.foto = foto;
        this.sku = sku;
        this.idCategoria = idCategoria;
        this.talla = talla;
        this.color = color;
        this.stock = stock;
        this.precioCompra=precioCompra;
        this.precioVenta = precioVenta;
        this.descripcion = descripcion;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString(){
        return nombre + "   [Talla: " +talla +" - "+ color +"]     "+"S/ " +precioVenta;
    }
}