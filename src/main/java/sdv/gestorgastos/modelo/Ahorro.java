package sdv.gestorgastos.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Ahorro {

    private BigDecimal cantidad;
    private LocalDate fechaIngreso;
    private LocalDate fechaProgramada;
    private TipoEstado estado;

    public Ahorro() {
    }

    public Ahorro(BigDecimal cantidad, LocalDate fechaProgramada) {
        this.cantidad = cantidad;
        this.fechaProgramada = fechaProgramada;
        this.estado = TipoEstado.PENDIENTE;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(LocalDate fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public TipoEstado getEstado() {
        return estado;
    }

    public void setEstado(TipoEstado estado) {
        this.estado = estado;
    }


    public String ahorroEnLinea(){

        return cantidad + "," +
                ((fechaIngreso != null) ? fechaIngreso.toString() : "No ahorrado") + "," +
                fechaProgramada.toString() + "," +
                estado.toString().toLowerCase();
    }
}
