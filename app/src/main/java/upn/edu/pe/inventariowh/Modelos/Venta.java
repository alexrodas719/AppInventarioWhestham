package upn.edu.pe.inventariowh.Modelos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Venta {
    @SerializedName("idVenta")
    private int idVenta;
    @SerializedName("codigo")
    private String codigo;
    @SerializedName("fecha")
    private long fecha;

    @SerializedName("subtotal")
    private double subtotal;
    @SerializedName("descuento")
    private double descuento;
    @SerializedName("total")
    private double total;
    @SerializedName("detalles")
    private List<DetalleVenta> detalles;

    public Venta() {
    }

    public Venta(int idVenta, String codigo, long fecha, double subtotal, double descuento, double total) {
        this.idVenta = idVenta;
        this.codigo = codigo;
        this.fecha = fecha;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
    public List<DetalleVenta> getDetalles() {
        return detalles;
    }
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
}