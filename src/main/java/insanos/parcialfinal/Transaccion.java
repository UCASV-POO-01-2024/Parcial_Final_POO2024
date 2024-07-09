package insanos.parcialfinal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaccion {
    private int IDtransaccion;
    private LocalDateTime fecha;
    private BigDecimal cantidad;
    private String descripcion;
    private String numTarjeta;

    public Transaccion(int IDtransaccion, LocalDateTime fecha, BigDecimal cantidad, String descripcion, String numTarjeta) {
        this.IDtransaccion = IDtransaccion;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.numTarjeta = numTarjeta;
    }

    public int getIDtransaccion() {
        return IDtransaccion;
    }

    public void setIDtransaccion(int IDtransaccion) {
        this.IDtransaccion = IDtransaccion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }

    public void setNumTarjeta(String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }
}
