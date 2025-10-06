
import org.junit.jupiter.api.*;
import sdv.gestorgastos.modelo.Ahorro;
import sdv.gestorgastos.modelo.Objetivo;
import sdv.gestorgastos.modelo.Plazo;
import sdv.gestorgastos.modelo.TipoPlazo;
import sdv.gestorgastos.util.GestorArchivos;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TestGestorArchivos {


    GestorArchivos gestor;

    @BeforeEach
    void setUp() {
        gestor = new GestorArchivos();
    }

    @Test
    public void ObtenerDatosDeArchivos(){

        gestor.cargarArchivos();
        assertEquals(600.0, gestor.getObjetivo().getCantidadObjetivo());
        assertEquals(TipoPlazo.QUINCENAL, gestor.getObjetivo().getPlazo().getTipo());
        assertFalse(gestor.getObjetivo().obtenerRealizados().isEmpty());

    }


    @Test
    public void crearArchivoObjetivo(){

        Objetivo objetivo = new Objetivo(new BigDecimal("600"), new Plazo(TipoPlazo.QUINCENAL, 3));
        objetivo.setFechaObjetivo(LocalDate.parse("2025-05-13"));
        objetivo.setFechaCreacion(LocalDate.now());

        gestor.guardarObjetivo(objetivo);

        File file = new File("C:\\Users\\sammy\\Desktop\\Archivos\\Objetivo.txt");
        assertTrue(file.exists());

    }

    @Test
    public void crearArchivoHistorial(){

        Objetivo objetivo = new Objetivo(new BigDecimal("1000"), new Plazo(TipoPlazo.MENSUAL, 3));
        objetivo.setFechaObjetivo(LocalDate.parse("2025-05-13"));
        objetivo.setFechaCreacion(LocalDate.now());

        Ahorro ahorro1 = new Ahorro(new BigDecimal("200"), LocalDate.parse("2025-05-13"));
        ahorro1.setFechaIngreso(LocalDate.parse("2025-05-13"));
        Ahorro ahorro2 = new Ahorro(new BigDecimal("200"), LocalDate.parse("2025-06-13"));
        ahorro2.setFechaIngreso(LocalDate.parse("2025-05-12"));
        Ahorro ahorro3 = new Ahorro(new BigDecimal("200"), LocalDate.parse("2025-07-13"));
        ahorro3.setFechaIngreso(LocalDate.parse("2025-05-28"));

        objetivo.agregarAhorro(ahorro1);
        objetivo.agregarAhorro(ahorro2);
        objetivo.agregarAhorro(ahorro3);

        gestor.guardarAhorros(objetivo);

        File file = new File("C:\\Users\\sammy\\Desktop\\Archivos\\Historial.txt");
        assertTrue(file.exists());

    }


    @Test
    public void crearArchivoProximos(){
        Objetivo objetivo = new Objetivo(new BigDecimal("1000"), new Plazo(TipoPlazo.MENSUAL, 3));
        objetivo.setFechaObjetivo(LocalDate.parse("2025-05-13"));
        objetivo.setFechaCreacion(LocalDate.now());

        Ahorro ahorro1 = new Ahorro(new BigDecimal("200"), LocalDate.parse("2025-05-13"));
        ahorro1.setFechaIngreso(LocalDate.parse("2025-05-13"));
        Ahorro ahorro2 = new Ahorro(new BigDecimal("200"), LocalDate.parse("2025-06-13"));
        ahorro2.setFechaIngreso(LocalDate.parse("2025-05-12"));
        Ahorro ahorro3 = new Ahorro(new BigDecimal("200"), LocalDate.parse("2025-07-13"));
        ahorro3.setFechaIngreso(LocalDate.parse("2025-05-28"));

        objetivo.agregarProximo(ahorro1);
        objetivo.agregarProximo(ahorro2);
        objetivo.agregarProximo(ahorro3);

        gestor.guardarProximos(objetivo);

        File file = new File("C:\\Users\\sammy\\Desktop\\Archivos\\Proximos.txt");
        assertTrue(file.exists());
    }

    @Test
    public void eliminarArchivosDeDatos(){

        String ruta = "C:\\Users\\sammy\\Desktop\\Archivos\\";
        gestor.borrarArchivos();

        assertFalse(new File(ruta + "Objetivo.txt").exists());
        assertFalse(new File(ruta + "Proximos.txt").exists());
        assertFalse(new File(ruta + "Historial.txt").exists());
    }

}
