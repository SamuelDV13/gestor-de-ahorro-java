package sdv.gestorgastos.util;

import sdv.gestorgastos.modelo.Ahorro;
import sdv.gestorgastos.modelo.Objetivo;
import sdv.gestorgastos.modelo.Plazo;
import sdv.gestorgastos.modelo.TipoPlazo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Planificador {

    private final Objetivo objetivo;

    public Planificador(Objetivo objetivo) {
        this.objetivo = objetivo;
    }


    public void planificarAhorro(TipoPlazo tipoPlazo){
        Plazo plazo = generarPlazos(tipoPlazo);
        objetivo.setPlazo(plazo);
        generarAhorros();
        //System.out.println(objetivo.mostrarProximos());
    }


    private Plazo generarPlazos(TipoPlazo tipoPlazo){
        if(tipoPlazo == TipoPlazo.AUTOMATICO){
            return generarPlazosAutomatico();
        } else{
            return generarPlazosManual(tipoPlazo);
        }
    }


    private Plazo generarPlazosAutomatico() {
        long dias = ChronoUnit.DAYS.between(objetivo.getFechaCreacion(), objetivo.getFechaObjetivo());
        if (dias <= 10) return new Plazo(TipoPlazo.DIARIO, (int) dias);
        if (dias <= 35) return new Plazo(TipoPlazo.SEMANAL, (int) ChronoUnit.WEEKS.between(objetivo.getFechaCreacion(), objetivo.getFechaObjetivo()));
        if (dias <= 100) return new Plazo(TipoPlazo.QUINCENAL, (int) (dias / 15));
        return new Plazo(TipoPlazo.MENSUAL, (int) ChronoUnit.MONTHS.between(objetivo.getFechaCreacion(), objetivo.getFechaObjetivo()));
    }

    private Plazo generarPlazosManual(TipoPlazo tipo) {
        long periodos = 0;
        long dias = ChronoUnit.DAYS.between(objetivo.getFechaCreacion(), objetivo.getFechaObjetivo());
        switch (tipo) {
            case DIARIO -> periodos = dias;
            case SEMANAL -> periodos = ChronoUnit.WEEKS.between(objetivo.getFechaCreacion(), objetivo.getFechaObjetivo());
            case QUINCENAL -> periodos = dias / 15;
            case MENSUAL -> periodos = ChronoUnit.MONTHS.between(objetivo.getFechaCreacion(), objetivo.getFechaObjetivo());
        }
        return new Plazo(tipo, (int) periodos);
    }


    private void generarAhorros() {

        BigDecimal cantidadAhorro = objetivo.getCantidadObjetivo().divide(new BigDecimal(objetivo.getPlazo().getPeriodos()), 2, RoundingMode.CEILING);
        LocalDate fechaCalculada = objetivo.getFechaCreacion();

        for (int i = 1; i <= objetivo.getPlazo().getPeriodos(); i++) {

            objetivo.agregarProximo(new Ahorro(cantidadAhorro, fechaCalculada));

            switch (objetivo.getPlazo().getTipo()){
                case DIARIO -> fechaCalculada = fechaCalculada.plusDays(1);
                case SEMANAL -> fechaCalculada = fechaCalculada.plusWeeks(1);
                case QUINCENAL -> fechaCalculada = fechaCalculada.plusDays(15);
                case MENSUAL -> fechaCalculada = fechaCalculada.plusMonths(1);
            }

        }
    }



    public Objetivo getObjetivo() {
        return objetivo;
    }
}
