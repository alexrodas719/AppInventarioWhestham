package upn.edu.pe.inventariowh.Modelos;

public class MovimientoInventario {
    private int idMovimiento;
    private String codigo;
    private String tipo;
    private int idProducto;
    private int cantidad;
    private long fecha;
    private double monto;
    private String observacion;

    public MovimientoInventario() {
    }

    public MovimientoInventario(int idMovimiento, String codigo, String tipo, int idProducto, int cantidad,
                                long fecha, double monto, String observacion) {
        this.idMovimiento = idMovimiento;
        this.codigo = codigo;
        this.tipo = tipo;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.monto = monto;
        this.observacion = observacion;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
