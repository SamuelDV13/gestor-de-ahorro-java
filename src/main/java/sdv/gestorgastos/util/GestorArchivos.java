package sdv.gestorgastos.util;

import sdv.gestorgastos.excepciones.ArchivoCorruptoException;
import sdv.gestorgastos.modelo.*;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class GestorArchivos {

    private final Objetivo objetivo = new Objetivo();
    private final Path rutaBase;


    public GestorArchivos() {
        this.rutaBase = obtenerOcrearCarpetaDeDatos();
    }


    private Path obtenerOcrearCarpetaDeDatos() {
        String userHome = System.getProperty("user.home");
        Path ruta = Paths.get(userHome, ".gestorDeAhorro");

        // Si la carpeta no existe, la crea.
        if (!Files.exists(ruta)) {
            try {
                Files.createDirectories(ruta);
            } catch (IOException e) {
                Mensajes.mostrarError("Error crítico: no se pudo crear el directorio de datos.");
                System.exit(1);
            }
        }
        return ruta;
    }


    public boolean cargarArchivos(){

        boolean bandera = false;
        boolean bandera1 = false;
        boolean bandera2 = false;

        if(cargarObjetivo()){bandera = true;
            System.out.println("Objetivo cargado correctamente");}
        if(cargarAhorros()){bandera1 = true;
            System.out.println("Ahorros cargado correctamente");}
        if(cargarProximos()){bandera2 = true;
            System.out.println("Proximos cargado correctamente");}

        return bandera1 && bandera2 && bandera;
    }


    private boolean cargarObjetivo(){

        Path rutaArchivo = this.rutaBase.resolve("Objetivo.txt");

        if(!Files.exists(rutaArchivo)){
            return true;
        }

        try(BufferedReader bufferedReader = Files.newBufferedReader(rutaArchivo)) {

            String linea = bufferedReader.readLine();

            String[] datos  = linea.split(",");

            objetivo.setCantidadObjetivo(new BigDecimal(datos[0]));
            objetivo.setFechaObjetivo(LocalDate.parse(datos[1]));
            objetivo.setFechaCreacion(LocalDate.parse(datos[2]));

            TipoPlazo tipo = switch (datos[3].trim()){
                case "AUTOMATICO" -> TipoPlazo.AUTOMATICO;
                case "DIARIO" -> TipoPlazo.DIARIO;
                case "SEMANAL" -> TipoPlazo.SEMANAL;
                case "QUINCENAL" -> TipoPlazo.QUINCENAL;
                case "MENSUAL" -> TipoPlazo.MENSUAL;
                default -> throw new IllegalStateException("Unexpected value: " + datos[3]);
            };

            Plazo plazo = new Plazo(tipo, Integer.parseInt(datos[4]));
            objetivo.setPlazo(plazo);

            return true;

        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean cargarAhorros(){

        Path rutaArchivo = this.rutaBase.resolve("Historial.txt");

        if(!Files.exists(rutaArchivo)){
            return true;
        }

        try(BufferedReader bufferedReader = Files.newBufferedReader(rutaArchivo)){

            String linea;
            while((linea = bufferedReader.readLine()) != null){
                objetivo.agregarAhorro(leerAhorros(linea));
            }

            return true;

        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean cargarProximos(){

        Path rutaArchivo = this.rutaBase.resolve("Proximos.txt");

        if(!Files.exists(rutaArchivo)){
            return true;
        }

        try(BufferedReader bufferedReader = Files.newBufferedReader(rutaArchivo)){

            String linea;
            while((linea = bufferedReader.readLine()) != null){
                objetivo.agregarProximo(leerAhorros(linea));
            }

            return true;

        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private Ahorro leerAhorros(String linea){
        String[] datos  = linea.split(",");

        Ahorro ahorro = new Ahorro();
        ahorro.setCantidad(new BigDecimal(datos[0]));

        String fechaString = datos[1];
        if(fechaString.equals("No ahorrado")){
            ahorro.setFechaIngreso(null);
        } else{
            ahorro.setFechaIngreso(LocalDate.parse(datos[1]));
        }

        ahorro.setFechaProgramada(LocalDate.parse(datos[2]));

        TipoEstado estado = switch (datos[3].toUpperCase().trim()){
            case "PENDIENTE" -> TipoEstado.PENDIENTE;
            case "ATRASADO" -> TipoEstado.ATRASADO;
            case "A_TIEMPO" -> TipoEstado.A_TIEMPO;
            default -> throw new IllegalStateException("Unexpected value: " + datos[3].trim());
        };

        ahorro.setEstado(estado);

        return ahorro;
    }

    public void guardarObjetivo(Objetivo objetivo){

        Path rutaArchivo = this.rutaBase.resolve("Objetivo.txt");

        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(rutaArchivo)){

            bufferedWriter.write(objetivo.objetivoEnLinea());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void guardarAhorros(Objetivo objetivo){

        Path rutaArchivo = this.rutaBase.resolve("Historial.txt");

        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(rutaArchivo)) {

            objetivo.obtenerRealizados().forEach(ahorro -> {
                try {
                    bufferedWriter.write(ahorro.ahorroEnLinea());
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void guardarProximos(Objetivo objetivo){

        Path rutaArchivo = this.rutaBase.resolve("Proximos.txt");

        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(rutaArchivo)) {

            objetivo.obtenerProximos().forEach(ahorro -> {
                try {
                    bufferedWriter.write(ahorro.ahorroEnLinea());
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void borrarArchivos() {
        try {
            // Files.deleteIfExists() hace el trabajo de "exists()" y "delete()" en un solo paso.
            Files.deleteIfExists(rutaBase.resolve("Objetivo.txt"));
            Files.deleteIfExists(rutaBase.resolve("Historial.txt"));
            Files.deleteIfExists(rutaBase.resolve("Proximos.txt"));

        } catch (IOException e) {
            Mensajes.mostrarError("Ocurrió un error al intentar eliminar los archivos de datos: " + e.getMessage());
        }
    }

    //Metodos DEMO
    public Objetivo getObjetivo() {
        return objetivo;
    }
}
