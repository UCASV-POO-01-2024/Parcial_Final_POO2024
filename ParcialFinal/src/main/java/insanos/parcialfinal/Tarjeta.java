package insanos.parcialfinal;

import java.time.LocalDate;

public class Tarjeta {
    private int id;
    private String facilitador;
    private String numTarjeta;
    private LocalDate fechaVencimiento;
    private String tipoTarjeta;

    public Tarjeta(int id, String facilitador, String numTarjeta, LocalDate fechaVencimiento, String tipoTarjeta) {
        this.id = id;
        this.facilitador = facilitador;
        this.numTarjeta = numTarjeta;
        this.fechaVencimiento = fechaVencimiento;
        this.tipoTarjeta = tipoTarjeta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFacilitador() {
        return facilitador;
    }

    public void setFacilitador(String facilitador) {
        this.facilitador = facilitador;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }

    public void setNumTarjeta(String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }
}

