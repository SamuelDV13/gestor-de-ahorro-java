package sdv.gestorgastos.controladores;

import sdv.gestorgastos.modelo.Objetivo;
import sdv.gestorgastos.modelo.TipoPlazo;
import sdv.gestorgastos.util.GestorArchivos;
import sdv.gestorgastos.util.Mensajes;
import sdv.gestorgastos.util.Planificador;
import sdv.gestorgastos.vistas.ventanaPrincipal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ObjetivoControlador {

    private final GestorArchivos gestorArchivos = new GestorArchivos();
    private final Objetivo objetivo;
    private final ventanaPrincipal ventanaPrincipal;

    public ObjetivoControlador(Objetivo objetivo, ventanaPrincipal vista) {
        this.objetivo = objetivo;
        this.ventanaPrincipal = vista;
    }

    public void crearPlanAhorro(String cantidadObjetivo, String fechaObjetivo, TipoPlazo tipoPlazo) {

        BigDecimal montoObjetivo;
        LocalDate fechaLocalD;

        try{
            montoObjetivo = new BigDecimal(cantidadObjetivo);
            fechaLocalD = LocalDate.parse(fechaObjetivo);
        } catch (NumberFormatException e){
            Mensajes.mostrarError("Ingresa una cantidad valida.");
            return;
        } catch (DateTimeParseException e){
            Mensajes.mostrarError("Ingresa una fecha valida.");
            return;
        }

        if(fechaLocalD.isBefore(LocalDate.now())){
            Mensajes.mostrarError("La fecha no puede ser pasada.");
            return;
        }

        if(montoObjetivo.compareTo(BigDecimal.ZERO) <= 0){
            Mensajes.mostrarError("Ingresa una cantidad mayor a $0.");
            return;
        }

        this.objetivo.setCantidadObjetivo(montoObjetivo);
        this.objetivo.setFechaObjetivo(fechaLocalD);
        this.objetivo.setFechaCreacion(LocalDate.now());

        Planificador planificador = new Planificador(this.objetivo);
        planificador.planificarAhorro(tipoPlazo);

        gestorArchivos.guardarObjetivo(this.objetivo);
        gestorArchivos.guardarProximos(this.objetivo);
        gestorArchivos.guardarAhorros(this.objetivo);

        ventanaPrincipal.actualizarVista();

    }


    public void MarcarAhorro(){
        objetivo.agregarAhorro();
        gestorArchivos.guardarAhorros(objetivo);
        gestorArchivos.guardarProximos(objetivo);

        if(objetivo.verificarObjetivo()){
            ventanaPrincipal.actualizarVista();
            Mensajes.mostrarInfo("¡¡Felicidades cumpliste tu objetivo!!");
            gestorArchivos.borrarArchivos();
            objetivo.resetearObjetivo();
        }

        ventanaPrincipal.actualizarVista();
    }


    public void eliminarObjetivo(){
        gestorArchivos.borrarArchivos();
        objetivo.resetearObjetivo();
        ventanaPrincipal.actualizarVista();
    }

}
