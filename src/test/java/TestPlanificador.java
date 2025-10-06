import org.junit.jupiter.api.*;
import sdv.gestorgastos.modelo.Ahorro;
import sdv.gestorgastos.modelo.Objetivo;
import sdv.gestorgastos.modelo.TipoPlazo;
import sdv.gestorgastos.util.Planificador;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlanificador {


    @Test
    public void TestGenerarPlazo(){

        Objetivo objetivo = new Objetivo(new BigDecimal("500"), LocalDate.parse("2025-09-30"));
        objetivo.setFechaCreacion(LocalDate.parse("2025-09-06"));
        Objetivo objetivo2 = new Objetivo(new BigDecimal("1500"), LocalDate.parse("2025-12-30"));
        objetivo2.setFechaCreacion(LocalDate.parse("2025-09-01"));

        Planificador planificador = new Planificador(objetivo);
        planificador.planificarAhorro(TipoPlazo.AUTOMATICO);

        Planificador planificador2 = new Planificador(objetivo2);
        planificador2.planificarAhorro(TipoPlazo.AUTOMATICO);


        assertEquals(TipoPlazo.SEMANAL, planificador.getObjetivo().getPlazo().getTipo());
        assertEquals(3, planificador.getObjetivo().getPlazo().getPeriodos());

        assertEquals(TipoPlazo.MENSUAL, planificador2.getObjetivo().getPlazo().getTipo());
        assertEquals(3, planificador2.getObjetivo().getPlazo().getPeriodos());
    }


    @Test
    public void TestGenerarAhorrosProximos(){
        LocalDate fechaInicio = LocalDate.parse("2025-09-07");
        LocalDate fechaFin = LocalDate.parse("2027-11-08");
        BigDecimal cantidadObjetivo = new BigDecimal("15000.0");

        Objetivo  objetivo = new Objetivo(cantidadObjetivo, fechaFin);
        objetivo.setFechaCreacion(fechaInicio);

        Planificador planificador = new Planificador(objetivo);
        planificador.planificarAhorro(TipoPlazo.AUTOMATICO);

        List<Ahorro> ahorros = planificador.getObjetivo().obtenerProximos();

        //1.- Numero de cuotas
        assertEquals(TipoPlazo.MENSUAL, planificador.getObjetivo().getPlazo().getTipo());
        assertEquals(26, ahorros.size());

        //2.- Primera y ultima fecha
        assertEquals(fechaInicio, ahorros.getFirst().getFechaProgramada());
        assertEquals(fechaInicio.plusMonths(25), ahorros.getLast().getFechaProgramada());

        //3.- Monto de cada cuota individual
        assertEquals(new BigDecimal("576.93"), ahorros.getFirst().getCantidad());

        //4.- Suma de todas las cuotas
        BigDecimal sumaCuotas = ahorros.stream()
                .map(Ahorro::getCantidad)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertTrue(sumaCuotas.compareTo(new BigDecimal(String.valueOf(cantidadObjetivo))) >= 0);
    }


    @Test
    public void planificarModoManual(){
        LocalDate fechaInicio = LocalDate.parse("2025-09-07");
        LocalDate fechaFin = LocalDate.parse("2025-12-30");
        BigDecimal cantidadObjetivo = new BigDecimal("15000.0");

        Objetivo objetivo = new Objetivo(cantidadObjetivo, fechaFin);
        objetivo.setFechaCreacion(fechaInicio);

        Planificador planificador = new Planificador(objetivo);
        planificador.planificarAhorro(TipoPlazo.DIARIO);

    }

}
