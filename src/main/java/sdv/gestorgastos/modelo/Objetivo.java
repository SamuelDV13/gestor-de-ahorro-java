package sdv.gestorgastos.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Objetivo {

    private BigDecimal cantidadObjetivo;
    private LocalDate fechaObjetivo;
    private LocalDate fechaCreacion;
    private Plazo plazo;
    private List<Ahorro> realizados;
    private List<Ahorro> proximos;


    public Objetivo() {
        this.realizados = new ArrayList<>();
        this.proximos = new ArrayList<>();
        this.fechaCreacion = LocalDate.now();
    }


    public Objetivo(BigDecimal cantidadObjetivo, LocalDate fechaObjetivo){
        this();
        this.cantidadObjetivo = cantidadObjetivo;
        this.fechaObjetivo = fechaObjetivo;
    }

    public Objetivo(BigDecimal cantidadObjetivo, Plazo plazo) {
        this();
        this.cantidadObjetivo = cantidadObjetivo;
        this.plazo = plazo;
    }

    public LocalDate getFechaObjetivo() {
        return fechaObjetivo;
    }

    public void setFechaObjetivo(LocalDate fechaObjetivo) {
        this.fechaObjetivo = fechaObjetivo;
    }

    public BigDecimal getCantidadObjetivo() {
        return cantidadObjetivo;
    }

    public void setCantidadObjetivo(BigDecimal cantidadObjetivo) {
        this.cantidadObjetivo = cantidadObjetivo;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Plazo getPlazo() {
        return plazo;
    }

    public void setPlazo(Plazo plazo) {
        this.plazo = plazo;
    }



    public BigDecimal verProgreso(){
        return this.realizados.stream()
                .map(Ahorro::getCantidad)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Ahorro> obtenerProximos() {
        return proximos;
    }

    public List<Ahorro> obtenerRealizados() {
        return realizados;
    }


    public void agregarAhorro(Ahorro a){
        a.setFechaIngreso(LocalDate.now());
        a.setEstado(determinarEstado(a));
        realizados.add(a);
    }

    public void agregarAhorro(){
        Ahorro a = proximos.removeFirst();
        a.setFechaIngreso(LocalDate.now());
        a.setEstado(determinarEstado(a));
        realizados.add(a);
    }

    public void agregarProximo(Ahorro a){
        this.proximos.add(a);
    }


    private TipoEstado determinarEstado(Ahorro a){

        if(a.getFechaIngreso().isAfter(a.getFechaProgramada())){
            return TipoEstado.ATRASADO;
        } else{
            return TipoEstado.A_TIEMPO;
        }
    }


    public String objetivoEnLinea(){

        return cantidadObjetivo + "," +
                fechaObjetivo + "," +
                fechaCreacion + "," +
                plazo.getTipo().toString() + "," +
                plazo.getPeriodos();
    }

    //Metodos de prueba
    public String mostrarProximos(){
        StringBuilder sb = new StringBuilder("Plazo: " + plazo.getTipo().toString() + "\n");
        for (Ahorro a : proximos){
            sb.append(a.ahorroEnLinea()).append("\n");
        }

        return sb.toString();
    }


    public void resetearObjetivo(){
        this.setCantidadObjetivo(null);
        this.setFechaObjetivo(null);
        this.setFechaCreacion(null);
        this.setPlazo(null);
        this.obtenerProximos().clear();
        this.obtenerRealizados().clear();
    }


    public boolean verificarObjetivo(){
        return this.verProgreso().compareTo(this.getCantidadObjetivo()) >= 0;
    }

}
